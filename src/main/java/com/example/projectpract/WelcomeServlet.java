package com.example.projectpract;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;

@WebServlet(name = "WelcomeServlet", value = "/welcome-page")
public class WelcomeServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/project_pract";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String CHECK_COOKIES = "SELECT COUNT(*) as 'count' FROM user WHERE email = ? AND password = ?";
    private final String FIND_USER = "SELECT * FROM user us INNER JOIN user_profile up WHERE us.id = up.user_id AND us.email = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                                            //  АВТОЛОГІН ЧЕРЕЗ КУКІ
        Cipher cipher = new Cipher();

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        LinkedHashMap<String, String> cookiesMap = new LinkedHashMap<>();
        for (Cookie ck : cookies) {
            cookiesMap.put(ck.getName(),ck.getValue());
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement checkCookiesState = conn.prepareStatement(CHECK_COOKIES);
            PreparedStatement findUserStat = conn.prepareStatement(FIND_USER);) {

            if (cookiesMap.containsKey("ue") && cookiesMap.containsKey("up")) {
                checkCookiesState.setString(1, cipher.deCipher(cookiesMap.get("ue")));
                checkCookiesState.setString(2, cookiesMap.get("up"));
                ResultSet rs = checkCookiesState.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                if (count == 1) {
                    findUserStat.setString(1, cipher.deCipher(cookiesMap.get("ue")));
                    ResultSet resultSet = findUserStat.executeQuery();
                    while (resultSet.next()) {
                        session.removeAttribute("user");
                        User user = (User) session.getAttribute("user");
                        if (user == null) {
                            user = new User(resultSet.getString("username"), resultSet.getString("email"), cipher.cipher(resultSet.getString("password")),
                                    resultSet.getInt("age"), resultSet.getString("about"), resultSet.getString("video_url"));
                        }
                        session.setAttribute("user", user);
                    }
                    getServletContext().getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
                }
            } else {
                getServletContext().getRequestDispatcher("/jsp/index.jsp").forward(request, response);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
