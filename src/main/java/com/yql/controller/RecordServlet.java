package com.yql.controller;



import com.yql.model.Records;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/recordservlet")
public class RecordServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 从session获取信息
        HttpSession session = request.getSession();
        Records records = new Records();

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

            records.setUsername((String) request.getSession().getAttribute("username"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母
            Date date = new Date();//获得当前时间
            records.setTimenow(df.format(date)); //将当前时间转换成特定格式的时间字符串，这样便可以插入到数据库中
            records.setCnt((int)request.getSession().getAttribute("cnt"));

            String sql = String.format("INSERT INTO records VALUES ('%s', '%s', '%d')",
                                        records.getUsername(), records.getTimenow(), records.getCnt());

            int i = statement.executeUpdate(sql);
            if (i > 0) {
                System.out.println("Insert Success!");
            }
            statement.close();
            connection.close();

            response.sendRedirect(request.getContextPath() + "/gameview?isRedirect=true&username="
                    + URLEncoder.encode(records.getUsername(), "UTF-8"));
//            request.getRequestDispatcher("/gameview").forward(request,response);
//            return;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
