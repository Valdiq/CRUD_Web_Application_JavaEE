package com.example.projectpract;

import com.google.protobuf.InvalidProtocolBufferException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;

@WebServlet(name = "SaveChangesServlet", value = "/save")
public class SaveChangesServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/project_pract";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String JOIN_USER = "SELECT * FROM user us INNER JOIN user_profile up WHERE us.id = up.user_id";
    private final String SELECT_USER_ID = "SELECT id FROM user WHERE email = ?";
    private final String UPDATE_USER = "UPDATE user SET username = ?, email = ?, password = ? WHERE email = ?";
    private final String UPDATE_USER_PROFILE = "UPDATE user_profile SET age = ?, about = ?, video_url = ? WHERE user_id = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //save
        //forvard to edit-profile page

        Cipher cipher = new Cipher();

        Cookie[] cookies = request.getCookies();
        LinkedHashMap<String, String> cookiesMap = new LinkedHashMap<>();
        for (Cookie ck : cookies) {
            cookiesMap.put(ck.getName(), ck.getValue());
        }


        response.setContentType("text/html");
        String saveUsername = request.getParameter("save-username");

        String saveEmail = request.getParameter("save-email");

        String savePaswd = request.getParameter("save-password");

        String saveAge = request.getParameter("save-age");

        String saveAbout = request.getParameter("save-about");

        String saveVideo = request.getParameter("save-video");


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement updateUserState = conn.prepareStatement(UPDATE_USER);
             PreparedStatement updateUserProfileState = conn.prepareStatement(UPDATE_USER_PROFILE);
             PreparedStatement selectUserIDState = conn.prepareStatement(SELECT_USER_ID);
             Statement joinUserState = conn.createStatement();) {

/*            ResultSet rs = joinUserState.executeQuery(JOIN_USER);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            String usernameColumn = rsMetaData.getColumnName(2);
            String emailColumn = rsMetaData.getColumnName(3);
            String passwordColumn = rsMetaData.getColumnName(4);
            String ageColumn = rsMetaData.getColumnName(6);
            String aboutColumn = rsMetaData.getColumnName(7);
            String videoColumn = rsMetaData.getColumnName(8);
*/

            selectUserIDState.setString(1, cipher.deCipher(cookiesMap.get("ue")));
            ResultSet rs = selectUserIDState.executeQuery();
            int userID = 0;
            while (rs.next()) {
                userID = rs.getInt(1);
            }

            updateUserState.setString(1, saveUsername);
            updateUserState.setString(2, saveEmail);
            updateUserState.setString(3, cipher.cipher(savePaswd));
            updateUserState.setString(4, cipher.deCipher(cookiesMap.get("ue")));
            updateUserState.executeUpdate();

            updateUserProfileState.setString(1, saveAge);
            updateUserProfileState.setString(2, saveAbout);
            updateUserProfileState.setString(3, saveVideo);
            updateUserProfileState.setInt(4, userID);
            updateUserProfileState.executeUpdate();

            HttpSession session = request.getSession();
            session.removeAttribute("user");
            User user = (User) session.getAttribute("user");
            if (user == null) {
                user = new User(saveUsername, saveEmail, cipher.cipher(savePaswd), Integer.parseInt(saveAge), saveAbout, saveVideo);
            }
            session.setAttribute("user", user);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }




        getServletContext().getRequestDispatcher("/edit-profile").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
