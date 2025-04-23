package com.yql.view;


import com.yql.model.Records;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/recordview")
public class RecordView extends HttpServlet {
    public void service (HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
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

            records.setUsername((String) session.getAttribute("username"));

//            String sql = String.format("SELECT * FROM records WHERE username='%s'", records.getUsername());
            String sql = "SELECT * FROM records WHERE username=" + "'" + records.getUsername() + "'";
            ResultSet resultSet = statement.executeQuery(sql);
//            System.out.println(sql);

            out.println("<html><head><title>" + records.getUsername() + "'s Records</title></head><body>");
            out.println("<h1 align=\"center\">" + records.getUsername() + "'s Records</h1>");
            out.println("<div align=\"center\">");
            out.println("<table align=\"center\" border=\"2\">");
            out.println("<tbody align=\"center\" valign=\"center\">");
            out.println("<tr>Guess Records<th>When you Succeed</th><th>Guess Times</th></tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("endtime") + "</td>");
                out.println("<td>" + resultSet.getInt("count") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</div></body></html>");
            resultSet.close();
            statement.close();
            connection.close();

            out.println("<p></p>");
            out.println("<div align=\"center\">");
            out.println("<form method='post' action='gameview'>");
            out.println("<input type='submit' value='Click to play number guess game'>");
            out.println("</form>");
            out.println("</div>");

//            response.sendRedirect(request.getContextPath() + "/gameview?username="
//                    + URLEncoder.encode(records.getUsername(), "UTF-8"));
//            request.getRequestDispatcher("/gameview").forward(request,response);
//            return;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
