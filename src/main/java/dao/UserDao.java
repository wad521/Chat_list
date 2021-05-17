package dao;

import exceprion.ChatroomException;
import model.User;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author:yxl
 **/

/*
 * 通过UserDao这个类来完成针对User表的基本操作*/
public class UserDao {
    // 1. 新增一个用户（注册功能）
    public void add(User user) {
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL语句
        // 第一个字段是userId，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        // lastLogout默认就添加成当前时间now()
        String sql = "insert into user values(null, ?, ?, ?,?,?,?,now(),now(),?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUser_name());
            statement.setString(2, user.getGender());
            statement.setString(3, user.getAcatar_url());
            statement.setString(4, user.getPwd());
            statement.setString(5, ""+user.getUser_state());
            statement.setString(6, ""+user.getContact_id());
            statement.setString(7, user.getUser_remark());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                // 我们可以主动抛出自定义异常来处理问题
                throw new ChatroomException("插入用户失败");
            }
            System.out.println("插入用户成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("插入用户失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 按用户名查找用户信息
    public User selectByName(String user_name) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "select * from user where user_name = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user_name);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 查找结果有多条，需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
           while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUser_name(resultSet.getString("user_name"));
                user.setGender(resultSet.getString("gender"));
                user.setPwd(resultSet.getString("pwd"));
                user.setUser_state(resultSet.getInt("user_state"));
                user.setContact_id(resultSet.getInt("contact_id"));
                user.setCreate_time(resultSet.getTimestamp("create_time"));
                user.setLastLogout(resultSet.getTimestamp("lastLogout"));
                user.setUser_remark(resultSet.getString("user_remark"));
                return user;
            }
            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按用户id查找用户失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 3. 按用户id查找用户信息（登录功能）
    //id唯一
    public  User selectById(int id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        //因为主键为user_id,不会重复
        String sql = "select * from user where id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUser_name(resultSet.getString("user_name"));
                user.setGender(resultSet.getString("gender"));
                user.setPwd(resultSet.getString("pwd"));
                user.setUser_state(resultSet.getInt("user_state"));
                user.setContact_id(resultSet.getInt("contact_id"));
                user.setCreate_time(resultSet.getTimestamp("create_time"));
                user.setLastLogout(resultSet.getTimestamp("lastLogout"));
                user.setUser_remark(resultSet.getString("user_remark"));
                return user;
            }
            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按用户id查找用户失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4. 更新用户的lastLogout时间（用户下线时更新，为了实现历史记录功能）
    public void updateLogout(int id) {
        // 哪个用户下线了，就更新哪个
        // 获取连接
        Connection connection = DBUtil.getConnection();
        // 拼装SQL
        String sql = "update user set lastLogout = now() where id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 执行sql
            int ret = statement.executeUpdate();
            if (ret != 1) {
                throw new ChatroomException("更新退出时间失败");
            }
            System.out.println("更新退出时间成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新退出时间失败");
        } finally {
            // 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    public void updatepwd(User user) {
        // 哪个用户下线了，就更新哪个
        // 获取连接
        Connection connection = DBUtil.getConnection();
        // 拼装SQL
        String sql = "update user set pwd = ? where id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+user.getPwd());
            statement.setString(2, ""+user.getId());
            // 执行sql
            int ret = statement.executeUpdate();
            if (ret != 1) {
                throw new ChatroomException("更新密码失败");
            }
            System.out.println("更新密码成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新密码失败");
        } finally {
            // 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

//    public static void main(String[] args) {
//        UserDao userDao = new UserDao();
//        // 针对上面的代码进行简单的验证
//        // 1. 验证add方法
//        User user = new User();
//        user.setUser_name("yxl");
//        user.setAcatar_url("comer/sort");
//        user.setPwd("123456");
//        userDao.add(user);
//// 2，按名字查找用户信息
//        User user2 = userDao.selectByName("yxl");
//        System.out.println(user2.toString());
//// 3.按用户id查找用户信息
//        User user3 = userDao.selectById(1);
//        System.out.println(user3);
//
//// 4. 更新用户的退出时间
//        userDao.updateLogout(6);
//    }
}
