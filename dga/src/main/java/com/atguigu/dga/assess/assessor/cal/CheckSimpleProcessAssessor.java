package com.atguigu.dga.assess.assessor.cal;

/**
 * Created by Smexy on 2023/8/26
 *
 *  sql语句没有任何join | groupby | union all，且where过滤中没有非分区字段。符合以上情况给0分，其余给10分。
 *
 *      分析sql语句。 一些特殊的表是没有sql语句。
 *          ods层不表，直接使用一个脚本load。
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
public class CheckSimpleProcessAssessor
{
}
