package dao;


import exceprion.ChatroomException;
import model.Contact;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author:yxl
 **/
public class ContactDao {

    //增加
    public void add(Contact contact){
        //获取数据库连接
        Connection connection = DBUtil.getConnection();
        //拼装SQL语句
        // 第一个字段是q_id，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        String sql = "insert into contact values( null, ?, ? , ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getC_type());
            statement.setString(2, contact.getC_imformation());
            statement.setString(3, contact.getC_remark());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                // 我们可以主动抛出自定义异常来处理问题
                throw new ChatroomException("插入联系方式失败");
            }
            System.out.println("插入联系方式成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("插入联系方式失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }


    //联系方式的精准查找
    public Contact select(int c_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "select * from contact where c_id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+c_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 查找结果有多条，需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (resultSet.next()) {
                Contact contact = new Contact();
                contact.setC_id(resultSet.getInt("c_id"));
                contact.setC_type(resultSet.getString("c_type"));
                contact.setC_imformation(resultSet.getString("c_imformation"));
                return contact;
            }
            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按联系方式查找失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    //修改
    public void update(Contact contact){
        // 获取连接
        Connection connection = DBUtil.getConnection();
        // 拼装SQL
        String sql = "update contact set c_imformation = ? where c_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getC_imformation());
            statement.setString(2, ""+ contact.getC_id());
            // 执行sql
            int ret = statement.executeUpdate();
            if (ret != 1) {
                throw new ChatroomException("更新联系方式失败");
            }
            System.out.println("更新联系方式成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新联系方式失败");
        } finally {
            // 释放连接
            DBUtil.close(connection, statement, null);
        }
    }


    //删除
    public void delete(int c_id){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼接SQL语句
        String sql = "delete from contact where c_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,c_id);
            // 3. 执行SQL语句
            int ret = statement.executeUpdate();
            if(ret != 1){
                throw new ChatroomException("删除联系方式失败");
            }
            System.out.println("删除联系方式成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("删除联系方式失败");
        }finally {
            // 4. 断开连接
            DBUtil.close(connection,statement,null);
        }
    }


    public static void main(String[] args) {
        ContactDao contactDao = new ContactDao();
//        //增
//        Contact contact = new Contact();
//        contact.setC_type("坠机");
//        contact.setC_imformation("13515484578");
//        contact.setC_remark("肃静！");
//        contactDao.add(contact);

        //查找
        Contact contact1 = contactDao.select(2);
        System.out.println(contact1);

        //修改
        Contact contact2 = new Contact();
        contact2.setC_id(2);
        contact2.setC_imformation("hahahahaha");
        contactDao.update(contact2);

        //删除
        contactDao.delete(1);
    }
}
