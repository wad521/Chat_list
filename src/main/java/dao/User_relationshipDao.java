package dao;

import exceprion.ChatroomException;
import model.User_relationship;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * 通过user_relationshipDao这个类来完成针对user_relationship表的基本操作*/
public class User_relationshipDao {
    // 1. 新增一个用户（注册功能）
    public static void add(User_relationship user_relationship){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL语句
        // 第一个字段是user_relationshipId，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        // lastLogout默认就添加成当前时间now()
        // 演示见笔记
        String sql = "insert into user_relationship values(?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+user_relationship.getUser_id());
            statement.setString(2, ""+user_relationship.getFans_id());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                // 我们可以主动抛出自定义异常来处理问题
                throw new ChatroomException("插入用户关系失败");
            }
            System.out.println("插入用户关系成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("插入用户关系失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 按用户编号查找用户信息（登录功能）
    public List<User_relationship> selectByUser_id(int user_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<User_relationship> list = new ArrayList<User_relationship>();
        // 2. 拼装SQL
        String sql = "select * from user_relationship where user_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+user_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            while (resultSet.next()) {
                User_relationship user_relationship = new User_relationship();
                user_relationship.setUser_id(resultSet.getInt("user_id"));
                user_relationship.setFans_id(resultSet.getInt("fans_id"));
                list.add(user_relationship);
            }
            if(list != null) {
                return list;
            }

            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按编号查找用户失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }



    public List<User_relationship> selectByFans_id(int fans_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<User_relationship> list = new ArrayList<User_relationship>();
        // 2. 拼装SQL
        String sql = "select * from user_relationship where fans_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+fans_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            while (resultSet.next()) {
                User_relationship user_relationship = new User_relationship();
                user_relationship.setUser_id(resultSet.getInt("user_id"));
                user_relationship.setFans_id(resultSet.getInt("fans_id"));
                list.add(user_relationship);
            }
            if(list != null) {
                return list;
            }

            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按编号查找用户失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }



    public static void main(String[] args) {
        User_relationshipDao user_relationshipDao = new User_relationshipDao();
        // 针对上面的代码进行简单的验证
        // 1. 验证add方法
        /*User_relationship user_relationship = new User_relationship();
        user_relationship.setUser_id(2);
        user_relationship.setFans_id(3);
        User_relationshipDao.add(user_relationship);
// 2，按名字查找用户信息
        List<User_relationship> fens =  user_relationshipDao.selectByUser_id(1);

        
        for(int i = 0 ; i < fens.size(); i++)
        {
            System.out.println(fens.get(i).toString());
        }
        List<User_relationship> users =  user_relationshipDao.selectByFans_id(2);
         for(int i = 0 ; i < fens.size(); i++)
        {
            System.out.println(fens.get(i).toString());
        }*/




    }
}
