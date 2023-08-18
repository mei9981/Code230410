package com.atguigu.mybatis.demos;

import com.atguigu.mybatis.beans.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Smexy on 2023/8/18
 *
 *              原生JDBC        Mybatis
 *   获取连接    Connection      SqlSession
 *
 *   -----------------------------------
 *      对象的创建:
 *          最简单:   A a = new A();
 *              前提： 构造器一般得是public
 *
 *          如果构造器不是public的，通常是无法去new。一般情况，会通过提供设计模式的方式，帮助创建对象。
 *
 *          工厂模式：
 *                   IPhone a =   new IPhoneFactory().getPhone();
 *
 *          建造者模式:
 *
 *                   House h = new HouseBuilder().setArea("500m2").build();
 *
 */
public class MybatisDemo1
{
    public static void main(String[] args) throws IOException {

        //读取配置文件
        String resource = "mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /*
            发送sql  查询某个id的员工
                selectOne(String statement, Object parameter)
                    statement: sql语句。使用 namespace.id的方式来引用
                    parameter： 语句中传入的参数
         */
        Object o = sqlSession.selectOne("feichangbang.a", 1);
        Employee employee = (Employee) o;
        System.out.println(employee);

        sqlSession.close();

    }
}
