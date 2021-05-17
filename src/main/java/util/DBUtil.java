package util;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * 帮助我们管理连接，本质上是实现了Datasource类的单例版本
 * 对于一个应用程序来说，DataSource只需要有一个实例就可以了
 * 单例是面试中最常考的设计模式
 * 饿汉模式比较简单，但懒汉模式更常考
 * 注意线程安全问题：
 *   1. 合适的位置加锁
 *   2. 双重IF判定
 *   3. volatile*/
public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/xueba?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    //双重校验锁写法
    private static volatile DataSource DATA_SOURCE;

    public static void main(String[] args) {
        Connection c = getConnection();
        System.out.println(c);
    }

    private DBUtil() {
    }

    private static DataSource getDataSource() {
        if (DATA_SOURCE == null) {
            synchronized (DBUtil.class) {
                if (DATA_SOURCE == null) {
                    DATA_SOURCE = new MysqlDataSource();
                    ((MysqlDataSource) DATA_SOURCE).setURL(URL);
                    ((MysqlDataSource) DATA_SOURCE).setUser(USERNAME);
                    ((MysqlDataSource) DATA_SOURCE).setPassword(PASSWORD);
                }
            }
        }
        return DATA_SOURCE;
    }
/*    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static volatile DataSource dataSource = null;

    public static DataSource getDataSource() {
        // 获取到这个单例的DataSource实例
        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();
                    // 必须要告诉代码数据库是谁，以什么样的方式登陆上去
                    // 此处涉及到向下转型，因为DataSource本身不能设定url,user等信息
                    // 因为DataSource是Java标准库中的类，需要考虑到各种数据库的情况，有些数据库是不需要设置用户名密码url等信息的
                    // 为了保证通用性就把这些属性预留好，交给对应的connect子类（数据库驱动）完成
                    ((MysqlDataSource)dataSource).setURL(URL);
                    ((MysqlDataSource)dataSource).setUser(USERNAME);
                    ((MysqlDataSource)dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }*/

    public static Connection getConnection() {
        try {
            // 和数据库建立连接，本质上mysql是一个“客户端-服务器”结构
            // 数据库的本体就是服务器（数据的存储和管理都是在服务器端完成的
            // 现在写的JDBC代码，本质上就是在实现一个Mysql的客户端
            // MySQL也是通过TCP协议来进行通信的
            // 当此处建立数据库连接的过程中，其中有一个环节就是TCP的三次握手
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


