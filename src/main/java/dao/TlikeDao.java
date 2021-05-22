package dao;
import exceprion.ChatroomException;
import model.Tlike;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TlikeDao {
    /* 通过tlikeDao这个类来完成针对tlike表的基本操作*/

    // 1. 新增一个点赞记录（注册功能）
    public void add(Tlike tlike){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL语句
        // 第一个字段是tlikeId，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        // lastLogout默认就添加成当前时间now()
        // 演示见笔记
        String sql = "insert into tlike values(null,now(),?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+tlike.getTlike_user_id());
            statement.setString(2, ""+tlike.getTlike_v_id());
            statement.setString(3, tlike.getTlike_remark());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                // 我们可以主动抛出自定义异常来处理问题
                throw new ChatroomException("添加点赞记录失败");
            }
            System.out.println("添加点赞记录成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("添加点赞记录失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 按用户编号查找用户信息（登录功能）
    public Tlike selectByTlike_id(int tlike_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<Tlike> list = new ArrayList<Tlike>();
        // 2. 拼装SQL
        String sql = "select * from tlike where tlike_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+tlike_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (resultSet.next()) {
                Tlike tlike = new Tlike();
                tlike.setTlike_id(resultSet.getInt("tlike_id"));
                tlike.setTlike_create_time(resultSet.getTimestamp("tlike_create_time"));
                tlike.setTlike_user_id(resultSet.getInt("tlike_user_id"));
                tlike.setTlike_v_id(resultSet.getInt("tlike_v_id"));
                tlike.setTlike_remark(resultSet.getString("tlike_remark"));
                return tlike;
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按编号查找点赞记录失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }






    // 2. 按用户编号查找用户信息（登录功能）
    public void delete(int tlike_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "delete  from tlike where tlike_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+tlike_id);
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("删除点赞记录成功");
            }
            else{
                throw new ChatroomException("删除点赞记录失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("删除点赞记录失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }
    // 2. 按用户编号查找用户信息（登录功能）
    public void update(int tlike_id,int tlike_user_id,int tlike_v_id,String tlike_remark) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();

        String sql = "update tlike set tlike_create_time=now(),tlike_user_id=?,tlike_v_id=?,tlike_remark=? where tlike_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1,""+tlike_user_id);
            statement.setString(2,""+tlike_v_id);
            statement.setString(3,tlike_remark);
            statement.setString(4,""+tlike_id);
            
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("更新点赞记录成功");
            }
            else{
                throw new ChatroomException("更新点赞记录失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新点赞记录失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    public static void main(String[] args) {
            TlikeDao tlikeDao = new TlikeDao();
            // 针对上面的代码进行简单的验证
            // 1. 验证add方法
            /*Tlike tlike = new Tlike();
            tlike.setTlike_create_time(new Timestamp(System.currentTimeMillis()));
            tlike.setTlike_user_id(1);
            tlike.setTlike_v_id(1);
            tlike.setTlike_remark("udy");
  
            tlikeDao.add(tlike);
           // 2，按名字查找用户信息
            Tlike fens =  tlikeDao.selectByTlike_id(1);
            System.out.println(fens.toString());
            /*List<Advertisement> users =  AdvertisementDao.selectByAd_type(1);

            for(int i = 0; i < users.size();i++)
            {
                System.out.println(users.get(i).toString());
            }*/
            tlikeDao.delete(1);

        /*tlikeDao.update(1,1,2,"time");
            System.out.println(tlikeDao.selectByTlike_id(1).toString());
*/
        }
}
