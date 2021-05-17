package dao;


import exceprion.ChatroomException;
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
public class QuetionDao {
    //新增一个question
    public void add(Question question){
        //获取数据库连接
        Connection connection = DBUtil.getConnection();
        //拼装SQL语句
        // 第一个字段是q_id，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        String sql = "insert into question values(null, ?, ?, ? , now() , ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1,question.getQ_conntent());
            statement.setString(2, ""+question.getQ_user_id());
            statement.setString(3, ""+question.getParent_q_id());
            statement.setString(4, question.getQ_remark());
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
            throw new ChatroomException("插入问题失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    //查找问题根据问题id
    public Question selectByq_id(int q_id){
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        //因为主键为user_id,不会重复
        String sql = "select * from question where q_id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+q_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (resultSet.next()) {
                Question question = new Question();
                question.setQ_id(resultSet.getInt("q_id"));
                question.setQ_conntent(resultSet.getString("q_conntent"));
                question.setQ_user_id(resultSet.getInt("q_user_id"));
                question.setParent_q_id(resultSet.getInt("parent_q_id"));
                question.setQ_creat_time(resultSet.getTimestamp("q_creat_time"));
                question.setQ_remark(resultSet.getString("q_remark"));
                return question;
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

    //模糊查询问答内容
    public List<Question> selectByq_conntent(String q_conntent){
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "select * from question where q_content Like '%' ? '%'";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Question> questions = new ArrayList<>();
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, q_conntent);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果拖条记录，用while
            // 如果resultSet.next直接为false,说明该用户名不存在
           while (resultSet.next()) {
                Question question = new Question();
                question.setQ_id(resultSet.getInt("q_id"));
                question.setQ_conntent(resultSet.getString("q_content"));
                question.setQ_user_id(resultSet.getInt("q_user_id"));
                question.setParent_q_id(resultSet.getInt("parent_q_id"));
                question.setQ_creat_time(resultSet.getTimestamp("q_create_time"));
                question.setQ_remark(resultSet.getString("q_remark"));
                questions.add(question);
            }
           return questions;
            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按问答内容查找用户失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
    }

    //根据q_id删除question
    public void delete(int q_id){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼接SQL语句
        String sql = "delete from question where q_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,q_id);
            // 3. 执行SQL语句
            int ret = statement.executeUpdate();
            if(ret != 1){
                throw new ChatroomException("删除问题失败");
            }
            System.out.println("删除问题成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("删除问题失败");
        }finally {
            // 4. 断开连接
            DBUtil.close(connection,statement,null);
        }
    }

    //查看问题列表
    // 3. 查看频道列表
    public List<Question> selectAll(){
        List<Question> questions = new ArrayList<>();
        // 1，建立连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "select * from question";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果
            while (resultSet.next()){
                Question question = new Question();
                question.setQ_id(resultSet.getInt("q_id"));
                question.setQ_conntent(resultSet.getString("q_content"));
                question.setQ_user_id(resultSet.getInt("q_user_id"));
                question.setParent_q_id(resultSet.getInt("parent_q_id"));
                question.setQ_creat_time(resultSet.getTimestamp("q_create_time"));
                question.setQ_remark(resultSet.getString("q_remark"));
                questions.add(question);
            }
            return questions;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }


    // 针对这个类进行简单的单元测试
//    public static void main(String[] args) {
//        // 创建一个channelDao实例
//        QuetionDao questionDao = new QuetionDao();
//        // 验证add
//        Question question = new Question();
//        question.setQ_conntent("关于C++类的构建");
//        question.setQ_user_id(5);
//        questionDao.add(question);
//
//        //验证selectByq_id
//        Question question1 = questionDao.selectByq_id(1);
//        System.out.println(question1);
//
//        //验证selectByq_conntent
//        List<Question> question2 = questionDao.selectByq_conntent("java");
//        System.out.println(question2);
//
//        // 验证selecetAll
//        List<Question> questions = questionDao.selectAll();
//        System.out.println(questions);
//
//        // 验证delect
//        questionDao.delete(6);
//    }
}
