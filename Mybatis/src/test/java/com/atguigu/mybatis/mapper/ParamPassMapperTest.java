package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.MyBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Smexy on 2023/8/18
 */
public class ParamPassMapperTest
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
    public void query1() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            ParamPassMapper mapper = session.getMapper(ParamPassMapper.class);

            // 3  hahaha     female  xixixi
            Map<String,Object> param = new HashMap<>();
            param.put("a",3);
            param.put("b","hahaha");
            param.put("c","female");
            System.err.println(mapper.query1(param));
        } finally {
            session.close();
        }
    }

    @Test
    public void query2() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            ParamPassMapper mapper = session.getMapper(ParamPassMapper.class);

            // 3  hahaha     female  xixixi
            MyBean myBean = new MyBean(3, "female", "hahaha");
            System.err.println(mapper.query2(myBean));
        } finally {
            session.close();
        }
    }

    @Test
    public void query3() {
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            ParamPassMapper mapper = session.getMapper(ParamPassMapper.class);

           // System.err.println(mapper.query3(3, "hahaha","female"));
            System.err.println(mapper.query4(3, "hahaha","female"));
        } finally {
            session.close();
        }
    }
}