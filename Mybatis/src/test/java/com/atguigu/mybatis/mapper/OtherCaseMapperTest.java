package com.atguigu.mybatis.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by Smexy on 2023/8/18
 */
public class OtherCaseMapperTest
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
    @Test
    public void getEmpById() {

        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            OtherCaseMapper mapper = session.getMapper(OtherCaseMapper.class);

            //System.err.println(mapper.getEmpById("Tom"));
            System.err.println(mapper.getEmpById2("employee"));
        } finally {
            session.close();
        }
    }

    //测试sql注入
    @Test
    public void testSqlInjection() {

        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            OtherCaseMapper mapper = session.getMapper(OtherCaseMapper.class);

            System.err.println(mapper.getEmps("'male' or id > 0"));
        } finally {
            session.close();
        }
    }
}