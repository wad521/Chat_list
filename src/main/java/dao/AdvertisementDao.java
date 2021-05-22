package dao;

import exceprion.ChatroomException;
import model.Advertisement;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementDao {


     /* 通过AdvertisementDao这个类来完成针对Advertisement表的基本操作*/

        // 1. 新增一个广告（注册功能）
        public void add(Advertisement advertisement){
            // 1. 获取数据库连接
            Connection connection = DBUtil.getConnection();
            // 2. 拼装SQL语句
            // 第一个字段是AdvertisementId，因为设置的是自增，所以添加成Null,系统会自动给他添加序号
            // lastLogout默认就添加成当前时间now()
            // 演示见笔记
            String sql = "insert into Advertisement values(null,?, ? , ? ,?)";
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, advertisement.getAd_description());
                statement.setString(2, advertisement.getAd_url());
                statement.setString(3, ""+advertisement.getAd_type());
                statement.setString(4, advertisement.getAd_remark());
                // 3. 执行SQL语句
                // 插入，删除，修改数据都叫executeUpdate
                // 如果是查找数据是executeQuery
                // 返回结果表示“影响到的行数”
                int ret = statement.executeUpdate();
                if (ret != 1) {
                    // 此处影响到的行数应该为1，所以不为1的画说明出现问题了
                    // 我们可以主动抛出自定义异常来处理问题
                    throw new ChatroomException("新增广告失败");
                }
                System.out.println("新增广告成功");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ChatroomException("新增广告失败");
            } finally {
                // 4. 释放连接
                DBUtil.close(connection, statement, null);
            }
        }

        // 2. 按用户编号查找用户信息（登录功能）
        public Advertisement selectByAd_id(int ad_id) {
            // 1. 获取到连接
            Connection connection = DBUtil.getConnection();
            List<Advertisement> list = new ArrayList<Advertisement>();
            // 2. 拼装SQL
            String sql = "select * from Advertisement where ad_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, ""+ad_id);
                // 3. 执行SQL
                resultSet = statement.executeQuery();
                // 4. 遍历结果集合（执行查找操作，必须要有这一步）
                // 由于查找结果最多只有一条记录，所以直接使用if判定即可
                // 如果查找结果有多条，就需要使用while循环来获取
                // 如果resultSet.next直接为false,说明该用户名不存在
                if (resultSet.next()) {
                    Advertisement advertisement = new Advertisement();
                    advertisement.setAd_id(resultSet.getInt("ad_id"));
                    advertisement.setAd_description(resultSet.getString("ad_description"));
                    advertisement.setAd_url(resultSet.getString("ad_url"));
                    advertisement.setAd_type(resultSet.getInt("ad_type"));
                    advertisement.setAd_remark(resultSet.getString("ad_remark"));
                    return advertisement;
                }


                // throw new ChatroomException("按用户名查找失败");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ChatroomException("按编号查找广告失败");
            } finally {
                // 5. 释放连接
                DBUtil.close(connection, statement, resultSet);
            }
            return null;
        }



        public List<Advertisement> selectByAd_type(int ad_type) {
            // 1. 获取到连接
            Connection connection = DBUtil.getConnection();
            List<Advertisement> list = new ArrayList<Advertisement>();
            // 2. 拼装SQL
            String sql = "select * from Advertisement where ad_type = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, ""+ad_type);
                // 3. 执行SQL
                resultSet = statement.executeQuery();
                // 4. 遍历结果集合（执行查找操作，必须要有这一步）
                // 由于查找结果最多只有一条记录，所以直接使用if判定即可
                // 如果查找结果有多条，就需要使用while循环来获取
                // 如果resultSet.next直接为false,说明该用户名不存在
                while (resultSet.next()) {
                    Advertisement advertisement = new Advertisement();
                    advertisement.setAd_id(resultSet.getInt("ad_id"));
                    advertisement.setAd_description(resultSet.getString("ad_description"));
                    advertisement.setAd_url(resultSet.getString("ad_url"));
                    advertisement.setAd_type(resultSet.getInt("ad_type"));
                    advertisement.setAd_remark(resultSet.getString("ad_remark"));
                    list.add(advertisement);
                }
                if(list != null) {
                    return list;
                }

                // throw new ChatroomException("按用户名查找失败");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ChatroomException("按类型查找广告失败");
            } finally {
                // 5. 释放连接
                DBUtil.close(connection, statement, resultSet);
            }
            return null;
        }


    // 2. 按用户编号查找用户信息（登录功能）
    public Advertisement delete(int ad_id) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<Advertisement> list = new ArrayList<Advertisement>();
        // 2. 拼装SQL
        String sql = "delete  from Advertisement where ad_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ""+ad_id);
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("删除广告成功");
            }
            else{
                throw new ChatroomException("删除广告失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("删除广告失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
        return null;
    }
    // 2. 按用户编号查找用户信息（登录功能）
    public void update(int ad_id,String ad_description,String ad_url,int ad_type,String ad_remark) {
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        List<Advertisement> list = new ArrayList<Advertisement>();
        // 2. 拼装SQL
        String sql = "update Advertisement set ad_description=?,ad_url=?,ad_type=?,ad_remark=? where ad_id = ?";//因为name设置的是不重复的，所以查找操作最终最多返回1条记录
        PreparedStatement statement = null;
        int num = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ad_description);
            statement.setString(2, ad_url);
            statement.setString(3, ""+ad_type);
            statement.setString(4, ad_remark);
            statement.setString(5, ""+ad_id);
            // 3. 执行SQL
            num = statement.executeUpdate();
            // 4. 遍历结果集合（执行查找操作，必须要有这一步）
            // 由于查找结果最多只有一条记录，所以直接使用if判定即可
            // 如果查找结果有多条，就需要使用while循环来获取
            // 如果resultSet.next直接为false,说明该用户名不存在
            if (num != 0) {
                System.out.println("更新广告成功");
            }
            else{
                throw new ChatroomException("更新广告失败");
            }


            // throw new ChatroomException("按用户名查找失败");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatroomException("更新广告失败");
        } finally {
            // 5. 释放连接
            DBUtil.close(connection, statement, null);
        }
    }
        public static void main(String[] args) {
            AdvertisementDao AdvertisementDao = new AdvertisementDao();
            /*// 针对上面的代码进行简单的验证
            // 1. 验证add方法
            Advertisement advertisement = new Advertisement();
            advertisement.setAd_description("hello world");
            advertisement.setAd_url("www.baidu.com");
            advertisement.setAd_type(1);
            advertisement.setAd_remark("two");
   /*         ad_id
ad_description
ad_url
ad_type
ad_remark
            AdvertisementDao.add(advertisement);
           // 2，按名字查找用户信息
            Advertisement fens =  AdvertisementDao.selectByAd_id(2);
            System.out.println(fens.toString());
            List<Advertisement> users =  AdvertisementDao.selectByAd_type(1);

            for(int i = 0; i < users.size(); i++)
            {
                System.out.println(users.get(i).toString());
            }*/
            //AdvertisementDao.delete(2);

            AdvertisementDao.update(3,"apple","7k7k.com",2,"last");
            AdvertisementDao.selectByAd_id(3);

        }

}
