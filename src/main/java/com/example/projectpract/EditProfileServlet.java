package com.example.projectpract;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;

@WebServlet(name = "EditProfileServlet", value = "/edit-profile")
public class EditProfileServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/project_pract";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String FIND_USER = "SELECT * FROM user us INNER JOIN user_profile up WHERE us.id = up.user_id AND us.email = ?";
    private final String UPDATE_USER = "UPDATE user SET ? = ? WHERE email = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/jsp/editProfile.jsp").forward(request, response);
        Cipher cipher = new Cipher();
        boolean isRefreshed = false;
        if (!isRefreshed) {
            response.setIntHeader("Refresh", 1);
            isRefreshed = true;
        }
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        LinkedHashMap<String, String> cookiesMap = new LinkedHashMap<>();
        for (Cookie ck : cookies) {
            cookiesMap.put(ck.getName(), ck.getValue());
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement findUserStat = conn.prepareStatement(FIND_USER);
             PreparedStatement updateUserStat = conn.prepareStatement(UPDATE_USER);) {
            findUserStat.setString(1, cipher.deCipher(cookiesMap.get("ue").toString()));
            ResultSet resultSet = findUserStat.executeQuery();
            if (resultSet.next()) {
                session.removeAttribute("user");
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    user = new User(resultSet.getString("username"), resultSet.getString("email"), cipher.cipher(resultSet.getString("password")),
                            resultSet.getInt("age"), resultSet.getString("about"), resultSet.getString("video_url"));
                }
                session.setAttribute("user", user);
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
