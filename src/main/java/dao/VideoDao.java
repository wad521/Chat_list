package dao;
import exceprion.ChatroomException;
import model.Video;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDao {
    /* 通过videoDao这个类来完成针对video表的基本操作*/

    // 1. 新增一个视频（注册功能）
    public void add(Video video){
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL语句
        // 第一个字段是videoId，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
        // lastLogout默认就添加成当前时间now()
        // 演示见笔记
        String sql = "insert into video values(null,now(),?,?, ? ,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, video.getSpec());
            statement.setString(2, ""+video.getV_user_id());
            statement.setString(3, video.getV_url());
            statement.setString(4, video.getV_remark());
            // 3. 执行SQL语句
            // 插入，删除，修改数据都叫executeUpdate
            // 如果是查找数据是executeQuery
            // 返回结果表示“影响到的行数”
            int ret = statement.executeUpdate();
            if (ret != 1) {
                // 此处影响到的行数应该为1，所以不为1的画说明出现视频了
                // 我们可以主动抛出自定义异常来处理视频
                throw new ChatroomException("添加视频失败");
            }
            System.out.println("添加视频成功");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("添加视频失败");
        } finally {
            // 4. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 按用户编号查找用户信息（登录功能）
    public Video selectByV_id(int v_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<Video> list = new ArrayList<Video>();
        // 2. 拼装SQL
        String sql = "select * from video where v_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+v_id);
            // 3. 执行SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (resultSet.next()) {
                Video video = new Video();
                video.setV_id(resultSet.getInt("v_id"));
                video.setV_create_time(resultSet.getTimestamp("v_create_time"));
                video.setSpec(resultSet.getString("spec"));
                video.setV_user_id(resultSet.getInt("v_user_id"));
                video.setV_url(resultSet.getString("v_url"));
                video.setV_remark(resultSet.getString("v_remark"));
                return video;
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("按编号查找视频失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }






    // 2. 按用户编号查找用户信息（登录功能）
    public void delete(int v_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装SQL
        String sql = "delete  from video where v_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+v_id);
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("删除视频成功");
            }
            else{
                throw new ChatroomException("删除视频失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("删除视频失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }
    // 2. 按用户编号查找用户信息（登录功能）
    public void update(int v_id,String spec,int v_user_id,String v_url,String v_remark) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();

        String sql = "update video set v_create_time=now(),spec=?,v_user_id=?,v_url=?,v_remark=? where v_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1,spec);
            statement.setString(2,""+v_user_id);
            statement.setString(3,v_url);
            statement.setString(4,v_remark);
            statement.setString(5,""+v_id);
            
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("更新视频成功");
            }
            else{
                throw new ChatroomException("更新视频失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新视频失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }

    public static void main(String[] args) {
            VideoDao videoDao = new VideoDao();
            // 针对上面的代码进行简单的验证
            // 1. 验证add方法
           /* Video video = new Video();
            video.setV_create_time(new Timestamp(System.currentTimeMillis()));
            video.setSpec("1");
            video.setV_user_id(1);
            video.setV_url("udy");
            video.setV_remark("udy");
            videoDao.add(video);
           // 2，按名字查找用户信息
            Video fens =  videoDao.selectByv_id(1);
            System.out.println(fens.toString());
            /*List<Advertisement> users =  AdvertisementDao.selectByAd_type(1);
*/
            /*for(int i = 0; i < users.size();i++)
            {
                System.out.println(users.get(i).toString());
            }*/
            //videoDao.delete(1);

       // videoDao.update(1,"hello",2,"com","edu");
          //  System.out.println(videoDao.selectByV_id(1).toString());

        }
}
