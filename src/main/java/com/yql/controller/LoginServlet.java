// 在这里要处理收集上来的用于登录的表单数据，然后提交给数据库，检查有没有这个用户
package com.yql.controller;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.alibaba.fastjson2.JSONObject;
import com.yql.utils.MD5Util;
import com.yql.model.UserInfo;

import java.sql.Connection;
import java.sql.*;




@WebServlet("/loginservlet")
public class LoginServlet extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserInfo user = new UserInfo();
        user.setUsername(request.getParameter("username"));
//        System.out.println(user.getUsername() + "321123");
        user.setPassword(MD5Util.crypt(request.getParameter("password")));

        // 然后与数据库存的散列值对比
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String url = "jdbc:mysql://localhost:3306/myj2eegame";
        String username="root";
        String password="root1";

        // 连接成功，数据库对象 Connection
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            // 执行SQL对象Statement，执行SQL的对象
            Statement statement = connection.createStatement();
            // 执行SQL的对象去执行SQL，返回结果集
            String sql = "SELECT password FROM usernamepswd WHERE username='"+request.getParameter("username")+"'";
            ResultSet resultSet = statement.executeQuery(sql);
            String pswd = null; // 数据库中的密码

            if (resultSet.next()) {
                pswd = resultSet.getString("password");

                if (pswd.equals(user.getPassword())) {
                    System.out.println("Success Sign in!");
                    // 登陆成功后 转发给玩游戏view处理
                    // 获取Session对象
                    HttpSession session = request.getSession();
                    // 在Session中存放属性
                    session.setAttribute("username", user.getUsername());
                    System.out.println("Login user.getUsername(): " + user.getUsername());
                    System.out.println("Login request.getAttribute: " + request.getAttribute("username"));
                    System.out.println("Login request.getParameter(): " + request.getParameter("username"));
                    request.getRequestDispatcher("/gameview").forward(request,response);
                    return;
                } else {
                    // name对 密码错
                    response.setHeader("content-type","text/html;charset=utf-8");
                    response.sendRedirect("index.html");
                }
            } else {
                // name没找到 转登录界面
                response.sendRedirect("index.html");
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

