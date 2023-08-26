package com.atguigu.dga.config;

import org.antlr.runtime.tree.Tree;
import org.apache.hadoop.hive.ql.lib.DefaultGraphWalker;
import org.apache.hadoop.hive.ql.lib.Dispatcher;
import org.apache.hadoop.hive.ql.lib.GraphWalker;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.*;

import java.util.Collections;
import java.util.Stack;

/**
 * Created by Smexy on 2023/8/26
 *  工具类，用于解析sql
 */
public class SqlParser
{
    /*
        TOK_XXX: 代表当前节点的类型。

        结论:
            ①sql中的所有信息(运算符，操作符，表名，列名)都可以在树上找到

            ②希望获取从哪些表进行了查询
                遍历树，读取TOK_TABNAME节点的子节点。
                    库名.表名: TOK_TABNAME有两个孩子，取右侧的就是表名。
                    表名: TOK_TABNAME有1个孩子，就是表名。

            ③如何知道当前sql是否有group by,union,join等复杂查询
                遍历树，看是否有TOK_LEFTOUTJOIN，说明有left join
                        有TOK_UNIONALL，说明有union all
                        .....
            ④如何读取sql语句where过滤的字段?
                找到where节点(标识 TOK_WHERE)，在这个节点下，遍历找TOK_TABLE_OR_COL节点的子节点，就是过滤的列名。
                    where节点TOK_WHERE 下的子节点，可能是 比较运算符， = ,>之类
                    也可能是一个逻辑运算符: and,or之类。

        ---------------
            hive为每一个类型都提供一个int类型的编码。
                例如: TOK_QUERY = 954

     */
    public static void  parseSql(String sql, Dispatcher dispatcher) throws Exception {

        //解析器
        ParseDriver parseDriver = new ParseDriver();
        //语法树
        ASTNode astNode = parseDriver.parse(sql);
        //找到 根节点的TOK_QUERY节点
        ASTNode tokQueryNode = (ASTNode) astNode.getChild(0);

        // 用遍历器遍历整个语法树
        GraphWalker ogw = new DefaultGraphWalker(dispatcher);
        ogw.startWalking(Collections.singletonList(tokQueryNode), null);

        //System.out.println(tokQueryNode.getName());

    }

    public static class MyDispather implements  Dispatcher{

        /*
            遍历每一个树中的节点时，执行的程序。
                Node nd： 当前遍历到的一个节点
         */
        @Override
        public Object dispatch(Node nd, Stack<Node> stack, Object... nodeOutputs) throws SemanticException {
            //把每个节点的名字输出
            ASTNode node = (ASTNode) nd;
            System.out.println("name:"+node.getName()+",type:"+node.getType()+",text:"+node.getText());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        String sql = "  with tmp1 as (" +
            "  select " +
            "  t1.name,t1.age,t2.score " +
            "  from student t1 left join score t2 " +
            "  on t1.id = t2.id " +
            ") " +
            "insert overwrite table result " +
            "select " +
            "   name,sum(score) totalScore " +
            "from tmp1 " +
            "group by name " +
            "union all " +
            "select " +
            "   age,sum(score) totalScore " +
            "from tmp1 " +
            " where age > 10 " +
            "group by age " ;

        String sql2 = " with t1 as (select aa(a),b,c,dt as dd from tt1,  tt2 \n" +
            "             where tt1.a=tt2.b and dt='2023-05-11'  )\n" +
            "  insert overwrite table tt9  \n" +
            "  select a,b,c \n" +
            "  from t1 \n" +
            "  where    dt = date_add('${xxx}',-4 )    \n" +
            "  union \n" +
            "  select a,b,c \n" +
            "  from t2\n" +
            "   where    dt = date_add('${xxx} ',-7 )  ";

        String sql3 = "select * from gmall.dim_user_zip t1  where  (dt = xxxx and a = b) or c > d";

        String sql4 = "select * from dim_user_zip t1  where t1.name = 'jack' ";

        parseSql(sql3,new MyDispather());


    }
}
