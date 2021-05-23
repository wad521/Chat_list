package servlet; /**
 * @author:yxl
 **/

import dao.UserDao;
import exceprion.ChatroomException;
import model.User;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import util.Util;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;


@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().create();

    static class Request{
        public int id;
        public String password;
    }


    static class Response{
        public int ok;
        public String reason;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    // 真的实现登录
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 显示的告诉servlet按照utf-8这样的编码方式来处理请求
        req.setCharacterEncoding("utf-8");
        Response response = new Response();
        try {
            // 1. 读取body中的数据
            String body = Util.readBody(req);
            // 2. 把读到的数据转换成Request对象
            Request request = gson.fromJson(body, Request.class);
            // 3. 按用户名在数据库中查找，匹配密码是否正确
            UserDao userDao = new UserDao();
            User user = userDao.selectById(request.id);
            // 4. 如果登陆失败，则给出提示
            //      登陆失败有两种情况：a. 用户名没找到   b. 密码不匹配
            if (user == null || !request.password.equals(user.getPwd())) {
                throw new ChatroomException("用户名或密码错误");
            }
            // 5. 如果登录成功，创建一个session对象
            // 在后面登陆上之后直接访问用户信息
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("user", user);
            // 6. 跳转至主页
            req.getRequestDispatcher(" ").forward(req,resp);

        } catch (JsonSyntaxException | ChatroomException e) {
            e.printStackTrace();
            response.ok = 0;
            response.reason = e.getMessage();
        } finally {
            resp.setContentType("application/json; charset=utf-8");
            String jsonString = gson.toJson(response);
            resp.getWriter().write(jsonString);
        }
    }
}
