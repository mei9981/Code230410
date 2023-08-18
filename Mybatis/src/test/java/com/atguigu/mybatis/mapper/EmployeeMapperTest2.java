package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Smexy on 2023/8/18
 *
 *  套路:
 *      SqlSession 不是线程安全的。不建议在属性(不管是实例还是静态)中声明SqlSession。
 *      在每一个方法中，都拥有自己的SqlSession。
 *
 *
 *  读： select
 *          无关事务
 *  写: delete,update,insert
 *          需要在最终去提交事务。
 *
 */
public class EmployeeMapperTest2
{
    private SqlSessionFactory sqlSessionFactory;

    {
        String resource = "mybatis.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //不行
    //private SqlSession sqlSession = sqlSessionFactory.openSession();

    @Test
    public void getAll() {
        SqlSession session = sqlSessionFactory.openSession();

        try {
            //com.sun.proxy.$Proxy5 implements com.atguigu.mybatis.mapper.EmployeeMapper2
            //使用Mybatis提供的动态代理技术，获取接口的一个实例
            EmployeeMapper2 mapper = session.getMapper(EmployeeMapper2.class);
            //class com.sun.proxy.$Proxy5
            System.out.println(mapper.getClass());
            //[interface com.atguigu.mybatis.mapper.EmployeeMapper2]
            System.out.println(Arrays.toString(mapper.getClass().getInterfaces()));
            //进行CRUD
            System.out.println(mapper.getAll());

        } finally {
            session.close();
        }
    }

    @Test
    public void selectOne() {
        SqlSession session = sqlSessionFactory.openSession();

        try {

            EmployeeMapper2 mapper = session.getMapper(EmployeeMapper2.class);
            //CRUD
            Employee emp = mapper.getEmpById(1);

            System.out.println(emp);

        } finally {
            session.close();
        }
    }

    @Test
    public void delete() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            EmployeeMapper2 mapper = session.getMapper(EmployeeMapper2.class);
            //CRUD
            mapper.deleteEmpById(2);
            //手动提交事务
            //session.commit();
        } finally {
            session.close();
        }
    }

    @Test
    public void update() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            EmployeeMapper2 mapper = session.getMapper(EmployeeMapper2.class);
            //CRUD
            Employee e = mapper.getEmpById(3);
            e.setLastName("hahaha");
            e.setEmail("xixixi");
            mapper.updateEmp(e);
        } finally {
            session.close();
        }
    }

    @Test
    public void insert() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            EmployeeMapper2 mapper = session.getMapper(EmployeeMapper2.class);
            //CRUD
            mapper.insertEmp(new Employee(null, "jack", "a", "b"));
        } finally {
            session.close();
        }
    }
}