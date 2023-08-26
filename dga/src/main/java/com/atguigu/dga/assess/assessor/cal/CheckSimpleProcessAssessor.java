package com.atguigu.dga.assess.assessor.cal;

import com.alibaba.fastjson.JSON;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.Field;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.config.MetaInfoUtil;
import com.atguigu.dga.config.SqlParser;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.hadoop.hive.ql.lib.Dispatcher;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.HiveParser;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/8/26
 *
 *  sql语句没有任何join | groupby | union all，且where过滤中没有非分区字段。符合以上情况给0分，其余给10分。
 *
 *      分析sql语句。 一些特殊的表是没有sql语句。
 *          ods层的表，直接使用一个脚本load。
 *
 *      ①拿到每一个Task运行的sql
 *      ②判断这个sql中是否有 join| groupby | union all 操作
 *          有，就是10分，不是简单加工
 *          没有，继续判断。
 *
 *              可以借助正则表达式。
 *
 *      ③where过滤中没有非分区字段
 *          获取where过滤的所有字段。[dt,name]
 *          查询当前的sql查询表的分区信息。
 *              select xx from a where xxxx
 *                  a表的分区字段由 [dt]
 *
 *               [dt,name] 和 [dt] 进行比对，取差集。
 *                  如果差集有数据，就说明where条件过滤中除了表的分区字段，还使用了其他字段(非分区字段)
 *                  当前不是一个简单加工。
 *
 *          反之，没有join | groupby | union all 操作， where过滤的字段集合 和 表的分区字段集合一样，
 *          意味，是一个简单加工，打0分。
 *
 *   ------------------------------------
 *      sql语法树。
 *          一个sql语句可以解析为一个sql语法树。这个sql的所有信息都会提现在语法树上。
 *          通过遍历语法树，就可以获取到sql所有相关的信息(复杂运算，where过滤的字段，from查询的表名)
 *
 */
@Component("SIMPLE_PROCESS")
public class CheckSimpleProcessAssessor extends AssessorTemplate
{
    @Autowired
    private MetaInfoUtil metaInfoUtil;
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws Exception {
        String dwLevel = param.getTableMetaInfo().getTableMetaInfoExtra().getDwLevel();
        String tableName = param.getTableMetaInfo().getTableName();
        //判断是不是ods层的表
        if (MetaConstant.DW_LEVEL_ODS.equals(dwLevel) || tableName.contains("dim_date")){
            return ;
        }

        //获取当前表的库名
        String schemaName = param.getTableMetaInfo().getSchemaName();
        //获取到Sql
        String sql = "";
        //解析sql
        MyDispather myDispather = new MyDispather();
        SqlParser.parseSql(sql,myDispather);
        //结果全在MyDispather的属性中保存着，取出来，再判断
        Set<String> complexOperator = myDispather.getComplexOperator();
        Set<String> whereFileds = myDispather.getWhereFileds();
        Set<String> tableNames = myDispather.getTableNames();

        //进行判断
        if (complexOperator.isEmpty()){
            //进一步判断 where过滤是否有非分区字段
            Set<String> tablePartitionFieldNames = new HashSet<>();
            //获取所查询表的分区信息
            tableNames.stream()
                .forEach(tname -> {
                    String partitionColNameJson = metaInfoUtil.tableMetaInfoMap.get(schemaName + "." + tname).getPartitionColNameJson();
                    List<Field> fields = JSON.parseArray(partitionColNameJson, Field.class);
                    Set<String> fieldNames = fields.stream().map(f -> f.getName()).collect(Collectors.toSet());
                    tablePartitionFieldNames.addAll(fieldNames);
                });
            /*
                差集运算
                 set集合交集: retainAll
                 set集合差集: removeAll
                    把差集运算的结果存放在前一个set中
             */
            whereFileds.removeAll(tablePartitionFieldNames);

            if (whereFileds.isEmpty()){
                //where过滤的字段，和所查询表的分区字段一模一样，一个不差，此时就符合简单查询
                assessScore(BigDecimal.ZERO,"简单查询","",detail,false,null);
            }

        }

        //不管是什么查询，都把细节信息附上
        detail.setAssessComment("复杂查询运算符:"+complexOperator+",where过滤的字段:"+whereFileds +",查询的表:"+tableNames);
    }

    /*
        如果当前节点是复杂查询(join,union,groupby)的节点，收集复杂操作
        如果当前节点是where过滤的节点，收集where过滤的字段
        如果当前节点是查询表的节点，收集查询的表名
     */
    @Data
    public static class MyDispather implements Dispatcher{

        //收集复杂操作
        private Set<String> complexOperator = new HashSet<>();
        //收集where过滤的字段
        private Set<String> whereFileds = new HashSet<>();
        //收集查询的表名
        private Set<String> tableNames = new HashSet<>();

        //把常见的所有的复杂查询的标识符，先列出来，方便比对
        Set<Integer> complexProcessSet= Sets.newHashSet(
            HiveParser.TOK_JOIN,  //join 包含通过where 连接的情况
            HiveParser.TOK_GROUPBY,       //  group by
            HiveParser.TOK_LEFTOUTERJOIN,       //  left join
            HiveParser.TOK_RIGHTOUTERJOIN,     //   right join
            HiveParser.TOK_FULLOUTERJOIN,     // full join
            HiveParser.TOK_FUNCTION,     //count(1)
            HiveParser.TOK_FUNCTIONDI,  //count(distinct xx)
            HiveParser.TOK_FUNCTIONSTAR, // count(*)
            HiveParser.TOK_SELECTDI,  // distinct
            HiveParser.TOK_UNIONALL   // union
        );

        //为了方便判断当前节点是不是比较运算符
        Set<String> operators= Sets.newHashSet("=",">","<",">=","<=" ,"<>"  ,"like","not like"); // in / not in 属于函数计算


        /*
            对每个节点都执行运算
         */
        @Override
        public Object dispatch(Node nd, Stack<Node> stack, Object... nodeOutputs) throws SemanticException {
            ASTNode astNode = (ASTNode) nd;

            //判断是不是复杂查询
            if (complexProcessSet.contains(astNode.getType())){
                complexOperator.add(astNode.getText());
            }

            //当前是一个where节点
            if (astNode.getType() == HiveParser.TOK_WHERE){
                //抽取where过滤的字段名
                extractWhereFileds(astNode);
            }

            //如果当前是一个表查询的节点，获取查询的表名
            if (astNode.getType() == HiveParser.TOK_TABNAME){
                ArrayList<Node> children = astNode.getChildren();
                //当前是 库名.表名格式
                if (children.size() == 2){
                    ASTNode astNodeChild = (ASTNode) astNode.getChild(1);
                    tableNames.add(astNodeChild.getText());
                }else {
                    ASTNode astNodeChild = (ASTNode) astNode.getChild(0);
                    tableNames.add(astNodeChild.getText());
                }
            }

            return null;
        }

        private void extractWhereFileds(ASTNode astNode) {
            //获取where节点下的所有子节点
            ArrayList<Node> children = astNode.getChildren();

            //如果已经是最后一层，是没有孩子
            if (children == null || children.isEmpty()){
                return ;
            }

            //判断子节点是不是 比较运算符
            for (Node child : children) {
                ASTNode childNode = (ASTNode) child;
                //是比较运算符
                if (operators.contains(childNode.getName())){
                    //比较运算符的子节点
                    ArrayList<Node> nodes = childNode.getChildren();
                    for (Node node : nodes) {
                        //判断当前节点是不是.
                        ASTNode n1 = (ASTNode) node;
                        if (n1.getType() == HiveParser.DOT){
                            whereFileds.add(((ASTNode)n1.getChild(1)).getName());
                        }else if (n1.getType() == HiveParser.TOK_TABLE_OR_COL){
                            whereFileds.add(((ASTNode)n1.getChild(0)).getName());
                        }
                    }
                }else {
                    //当前节点不是比较运算符，向下递归探测
                    extractWhereFileds(childNode);
                }
            }
        }
    }
}
