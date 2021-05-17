package dao;

import exceprion.ChatroomException;
import model.Comment;
import model.Question;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author:yxl
 **/
public class CommmentDao {

    //新增一个Connect
    public void add(Comment comment){
        //获取数据库连接
        Connection connection = DBUtil.getConnection();
        //拼装SQL语句
        // 第一个字段是q_id，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        String sql = "insert into comment values(null,  now(),?, ?, ? , ? , ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1,comment.getCom_content());
            statement.setString(2, ""+comment.getCom_v_id());
            statement.setString(3, ""+comment.getCom_user_id());
            statement.setString(4, ""+comment.getParent_com_id());
            statement.setString(5, comment.getCom_remark());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                // 我们可以主动抛出自定义异常来处理问题
                throw new ChatroomException("插入评论失败");
            }
            System.out.println("插入评论成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("插入评论失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }


    //删除评论
    //根据com_id删除
    public void delete(int com_id){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼接SQL语句
        String sql = "delete from comment where com_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,com_id);
            // 3. 执行SQL语句
            int ret = statement.executeUpdate();
            if(ret != 1){
                throw new ChatroomException("删除评论失败");
            }
            System.out.println("删除评论成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("删除问题失败");
        }finally {
            // 4. 断开连接
            DBUtil.close(connection,statement,null);
        }
    }

    //通过com_user_id获取用户所有评论
    public List<Comment> selectAll(int com_user_id){
        List<Comment> comments = new ArrayList<>();
        // 1，建立连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "select * from comment where com_user_id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+com_user_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果
            while (resultSet.next()){
                Comment comment = new Comment();
                comment.setCom_id(resultSet.getInt("com_id"));
                comment.setCom_create_time(resultSet.getTimestamp("com_create_time"));
                comment.setCom_content(resultSet.getString("com_content"));
                comment.setCom_user_id(resultSet.getInt("com_user_id"));
                comment.setCom_v_id(resultSet.getInt("com_v_id"));
                comment.setParent_com_id(resultSet.getInt("parent_com_id"));
                comment.setCom_remark(resultSet.getString("com_remark"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

//
//    public static void main(String[] args) {
//        CommmentDao commmentDao = new CommmentDao();
//
//        //验证添加功能add
//        Comment comment = new Comment();
//        comment.setCom_user_id(5);
//        comment.setCom_v_id(1);
//        commmentDao.add(comment);
//
//        //查找
//        List<Comment> list = commmentDao.selectAll(5);
//        System.out.println(list);
//
//        //删除
//        commmentDao.delete(3);
//    }
}
