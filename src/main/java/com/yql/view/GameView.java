package com.yql.view;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.yql.model.NumberGuessBean;


@WebServlet("/gameview")
public class GameView extends HttpServlet{
    public void service (HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 从session获取或创建NumberGuessBean
        HttpSession session = request.getSession();

        // 使用session获得 numberGuessBean 不然就每次提交会更改答案
        NumberGuessBean numberGuessBean = (NumberGuessBean)session.getAttribute("numberGuessBean");
        if (numberGuessBean == null) {
            numberGuessBean = new NumberGuessBean();
            session.setAttribute("numberGuessBean", numberGuessBean);
        }

        // 获取用户猜测
        String guess = request.getParameter("guess");
        // 只在有猜测时才设置
        if (guess != null && !guess.isEmpty()) {
            numberGuessBean.setGuess(guess);
        }


        out.println("<html>");
        out.println("<head>");
        out.println("<title>Number Guess Game</title>");
        out.println("</head>");

        out.println("<body>");
        System.out.println("request.getAttribute: " + request.getAttribute("username"));
        System.out.println("request.getParameter: " + request.getParameter("username"));
        System.out.println(numberGuessBean.getAnswer());

        String username = (String) request.getSession().getAttribute("username");
//        String username = request.getParameter("username");

        // 查询records
        out.println("<p align=\"center\">Welcome, " + username + "</p>");
        out.println("<div align=\"center\">");
        out.println("<form method='post' action='recordview'>");
//        out.println("<input type='hidden' name='username' value='" + username + "'>");
        out.println("<input type='submit' value='Click to view records'>");
        out.println("</form>");
//        out.println("<a href=RecordView.java\">Click to view records.</a>");
        out.println("</div>");

        // 修改游戏状态判断部分：
        boolean isRedirect = "true".equals(request.getParameter("isRedirect"));
        if (isRedirect) {
            // 从RecordServlet重定向回来后显示"Try again"
            out.println("<h1>Congratulations! You got it. And after just " +
                    session.getAttribute("cnt") + " tries.</h1>");
            out.println("<h2>Care to try again?</h2>");
            out.println("<form method='post' action='gameview'>");
            out.println("<input type='hidden' name='username' value='" + username + "'>");
            out.println("<input type='submit' value='Try Again'>");
            out.println("</form>");

            System.out.println("session username: " + session.getAttribute("username"));
            System.out.println("session cnt: " + session.getAttribute("cnt"));

        } else if (numberGuessBean.getSuccess()) {
            // 猜对了
            // 然后需要记录到record表格里
            session.setAttribute("cnt", numberGuessBean.getCnt());
            numberGuessBean.reset();
            request.getRequestDispatcher("/recordservlet").forward(request,response);
            return;
            // 提问 是否要进行新的一轮 改到上面了
//            out.println("<h2>Care to try again?</h2>");
//            out.println("Care to <a href=\"GameView.java\">try again</a>?");
//            request.setAttribute("username", username);
//            request.getRequestDispatcher("/recordservlet").forward(request, response);
//            return;

        } else if (0 == numberGuessBean.getCnt()){
            // 第一次猜
            out.println("<h1>Welcome to the Number Guess game.</h1>");
            out.println("I'm thinking of a number between 1 and 100.");
            StringBuilder html = new StringBuilder();
            html.append("<form method='post' action='gameview'>");
            html.append("What's your guess? <input type='text' name='guess'>");
            html.append("<input type='submit' value='Submit'>");
            html.append("</form>");
            out.println(html);
        } else {
            // 没猜对
            if (numberGuessBean.getCnt() == 1) {
                out.println("<h2>Good guess, but nope. Try " + numberGuessBean.getHint() +
                        ". You have made " + numberGuessBean.getCnt() + " guess.</h2>");
            } else if (numberGuessBean.getCnt() > 1) {
                out.println("<h2>Good guess, but nope. Try " + numberGuessBean.getHint() +
                        ". You have made " + numberGuessBean.getCnt() + " guesses.</h2>");
            }
            out.println("I'm thinking of a number between 1 and 100.");
            StringBuilder html = new StringBuilder();
            html.append("<form method='post' action='gameview'>");
            html.append("What's your guess? <input type='text' name='guess'>");
            html.append("<input type='submit' value='Submit'>");
            html.append("</form>");
            out.println(html);
        }

        out.println("</body>");
        out.println("</html>");
    }

}
