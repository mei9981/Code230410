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
 *     -------------------------------------
 *      lastName无法封装的原因解释:
 *          封装的时候，列名和Bean的setter方法无法对应！
 *
 *      ORM的流程:
 *          sqlSession.selectOne("feichangbang.a", 1);
 *
 *         mysql服务器执行: select * from employee where id = 1
 *         返回结果:
 *              id  last_name  gender  email
 *              ------  ---------  ------  -------------
 *               1  Tom        male    Tom@163.com
 *
 *         根据语句声明的resultType="com.atguigu.mybatis.beans.Employee"，封装对象。
 *          使用反射，构造一个Employee
 *            Employee a =  new Employee();
 *          将查询的一行的每个列的值，赋值到bean的属性上。
 *
 *          赋值的套路:  对象.setXxx(yyy);
 *                  Xxx: 是查询的列名
 *                  yyy:  查询的列值
 *                  a.setId(1);
 *                  a.setLast_name(Tom);
 *                  a.setGender(male);
 *                  a.setEmail( Tom@163.com);
 *
 *
 *        解决:
 *              方式一:在bean中提供对应的setter即可
 *              方式二:为无法对应的列，起别名，使和Bean的setter可以对应
 *                          实现方式： 手动改sql（麻烦）
 *                                   希望框架，自动把 下划线命名风格，转换为驼峰式命名风格
 *                                          帮我们把 last_name  转换为  lastName
 *                                          进行配置即可。
 *
 *   ------------------------
 *      调用是Mybatis官方提供的SqlSession类中封装的方法，造成不够自由！
 *          1.selectOne(String statement, Object parameter)
 *              参数类型无法检查的！
 *          2.返回值类型，不够灵活，也无法自动定义。
 *
 *          解决:  不调用Mybatis提供的方法，而是自己写！
 *                  称为Dao层接口式编程。
 *
 *
 *
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
       /* Object o = sqlSession.selectOne("feichangbang.a", inputStream);
        Employee employee = (Employee) o;*/

        // 泛型方法的调用:  对象.<泛型类型>方法()
        Employee employee = sqlSession.<Employee>selectOne("feichangbang.a", 1);
        System.out.println(employee);

        sqlSession.close();

    }
}
