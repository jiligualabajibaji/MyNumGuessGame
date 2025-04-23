package com.yql.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yql.model.UserInfo;
import com.yql.utils.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/registerservlet")
public class RegisterServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user = new UserInfo();
        user.setUsername(request.getParameter("username"));
        // 然后对password进行MD5散列
        user.setPassword(MD5Util.crypt(request.getParameter("password")));

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
            String sql = "INSERT INTO usernamepswd VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "')";
//            System.out.println(sql);
            int i = statement.executeUpdate(sql);
            if (i > 0) {
                System.out.println("Insert Success!");
            }

            statement.close();
            connection.close();

            // 注册成功后重定向到登录页面
            response.sendRedirect("index.html?register=success");

        } catch (SQLException e) {
            // 重复注册
            response.sendRedirect("index.html");
//            throw new RuntimeException(e);
        }

    }
}
