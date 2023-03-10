package com.example.projectpract;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@WebServlet(name = "AccountServlet", value = "/account")
public class AccountServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/project_pract";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String INSERT_USER = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
    private final String INSERT_USER_INFO = "INSERT INTO user_profile (user_id, age, about, video_url) VALUES (?, ?, ?, ?)";
    private final String CHECK_USER = "SELECT COUNT(*) AS 'count' FROM user WHERE email = ? AND password = ?";
    private final String FIND_USER = "SELECT * FROM user us INNER JOIN user_profile up WHERE us.id = up.user_id AND us.email = ?";
    private final char[] ALF = {'A', 'V', 'l', 'g', 'F', 'W', 'T', 'y', 'p', 't', 'n', 'o', 'P', 'J', 'a', 'v', 'N', 'Q', 'O', 'E', 'e', 'q', 'm',
            'R', 'w', 'B', 'G', 'b', 's', 'Y', 'f', 'z', 'S', 'X', 'x', 'Z', 'j', 'C', 'c', 'L', 'd', 'H', 'h', 'D', 'I', 'K', 'M', 'k', 'i',
            'r', 'U', 'u', 'n'};
    private final int[] NUMS = {4, 9, 6, 1, 0, 8, 2, 5, 3, 7};

    private final String CIPHER_FROG = "]s";
    private final String CIPHER_DOT = "r8";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

/////////////////////////////////////////////////////////////////////////////
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("JavaScript");
//
//        // JavaScript code in a String
//       // String script1 = "function hello(name) {alert(\"name\");}";
//        //String script2 = "function getValue(a,b) { if (a===\"Number\") return 1;else return b;}";
//        // evaluate script
//        try {
//           // engine.eval(script1);
//            engine.eval(Files.newBufferedReader(Paths.get("D:\\Study\\Programming\\JAVA\\JAVA EE\\ProjectPract\\src\\main\\webapp\\resources\\js\\dynamic.js"), StandardCharsets.UTF_8));
//         //   engine.eval(script2);
//        } catch (ScriptException e) {
//            throw new RuntimeException(e);
//        }
//
//        Invocable inv = (Invocable) engine;
//
//        try {
//            inv.invokeFunction("test", "TEST!!!");  //This one works.
//        } catch (ScriptException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        //////////////////////////////////////////////////
        // HttpSession session = req.getSession();


        HttpSession session = req.getSession();
        Cipher cipher = new Cipher();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement insertUserStat = conn.prepareStatement(INSERT_USER);
             PreparedStatement insertUserInfoState = conn.prepareStatement(INSERT_USER_INFO);
             PreparedStatement checkUserStat = conn.prepareStatement(CHECK_USER);
             PreparedStatement findUserStat = conn.prepareStatement(FIND_USER);
             PreparedStatement selectIdStat = conn.prepareStatement("SELECT id FROM user WHERE email = ?");) {

            resp.setContentType("text/html");
            String regUsername = req.getParameter("reg_username");
            String regEmail = req.getParameter("reg_email");
            String regPswd = req.getParameter("reg_pswd");
            String logEmail = req.getParameter("log_email");
            String logPaswd = req.getParameter("log_pswd");


            // deploy Maven -> НА ХОСТІНГ ЗА 2 КОПІЙКИ -> НА ХОСТІНГУ ТЕСТИТИ ЯК ЮЗЕР


            if ((regUsername != null && regEmail != null && regPswd != null) && (logEmail == null && logPaswd == null)) {

                insertUserStat.setString(1, regUsername);
                insertUserStat.setString(2, regEmail);
                insertUserStat.setString(3, cipher.cipher(regPswd));
                insertUserStat.executeUpdate();

                selectIdStat.setString(1, regEmail);
                ResultSet rs = selectIdStat.executeQuery();
                int userID = 0;
                while (rs.next()) {
                    userID = rs.getInt(1);
                }

                insertUserInfoState.setInt(1, userID);
                insertUserInfoState.setInt(2, 0);
                insertUserInfoState.setString(3, "");
                insertUserInfoState.setString(4, "");
                insertUserInfoState.executeUpdate();


                session.removeAttribute("user");
                User user = (User) session.getAttribute("user");

                if (user == null) {
                    user = new User(regUsername, regEmail, cipher.cipher(regPswd), 0, "", "");
                }

                session.setAttribute("user", user);

                Cookie emailCookie = new Cookie("ue", cipher.cipher(regEmail));
                Cookie pswdCookie = new Cookie("up", cipher.cipher(regPswd));
                resp.addCookie(emailCookie);
                resp.addCookie(pswdCookie);


            } else if ((regUsername == null && regEmail == null && regPswd == null) && (logEmail != null && logPaswd != null)) {
                checkUserStat.setString(1, logEmail);
                checkUserStat.setString(2, cipher.cipher(logPaswd));
                findUserStat.setString(1, logEmail);
                ResultSet resultSet = checkUserStat.executeQuery();
                ResultSet rs = findUserStat.executeQuery();

                while (resultSet.next()) {
                    int countOfRecords = resultSet.getInt("count");
                    if (countOfRecords == 1) {
                        if (rs.next()) {
                            String username = rs.getString("username");
                            String email = rs.getString("email");
                            String pswd = rs.getString("password");
                            int age = rs.getInt("age");
                            String about = rs.getString("about");
                            String video = rs.getString("video_url");
                            session.removeAttribute("user");
                            User user = (User) session.getAttribute("user");
                            if (user == null) {
                                user = new User(username, email, cipher.cipher(pswd), age, about, video);
                            }
                            session.setAttribute("user", user);
                        }
                        Cookie emailCookie = new Cookie("ue", cipher.cipher(logEmail));
                        Cookie pswdCookie = new Cookie("up", cipher.cipher(logPaswd));
                        emailCookie.setMaxAge(7 * (24 * 60 * 60));
                        pswdCookie.setMaxAge(7 * (24 * 60 * 60));
                        resp.addCookie(emailCookie);
                        resp.addCookie(pswdCookie);

                    } else {
                        getServletContext().getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
                    }
                }

            }

            getServletContext().getRequestDispatcher("/profile").forward(req, resp);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            getServletContext().getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }




   /* private String pswdCipher(String password) {
        StringBuilder ciperedPass = new StringBuilder();

        for (int i = 0; i < ALF.length - 1; i++) {
            for (int j = 0; j < password.length() - 1; j++) {
                //  for (char ch : ALF) {

                //   }
                if (password.charAt(j) == ALF[i]) {
                    if (i <= ALF.length - 3) {
                        ciperedPass.append(ALF[i + 2]);
                    } else {
                        switch (i) {
                            case 50:
                                ciperedPass.append(ALF[0]);
                                break;
                            case 51:
                                ciperedPass.append(ALF[1]);
                                break;
                        }
                    }
                } else {
                    for (int k = 0; k < NUMS.length - 1; k++) {
                        if (password.charAt(j) == NUMS[k]) {
                            if (k <= NUMS.length - 3) {
                                ciperedPass.append(NUMS[k + 2]);
                            } else {
                                switch (k) {
                                    case 8:
                                        ciperedPass.append(NUMS[0]);
                                        break;
                                    case 9:
                                        ciperedPass.append(NUMS[1]);
                                        break;
                                }
                            }
                        } else {
                            ciperedPass.append(password.charAt(j));
                        }
                    }
                }
            }
        }

        return ciperedPass.toString();
    }

    private String emailCipher(String email) {
        StringBuilder ciperedEmail = new StringBuilder();

        for (int i = 0; i < ALF.length - 1; i++) {
            for (int j = 0; j < email.length() - 1; j++) {
                switch (email.charAt(j)) {
                    case '@':
                        ciperedEmail.append(CIPHER_FROG);
                        break;
                    case '.':
                        ciperedEmail.append(CIPHER_DOT);
                        break;
                }
                if (email.charAt(j) == ALF[i]) {
                    if (i <= ALF.length - 3) {
                        ciperedEmail.append(ALF[i + 2]);
                    } else {
                        switch (i) {
                            case 50:
                                ciperedEmail.append(ALF[0]);
                                break;
                            case 51:
                                ciperedEmail.append(ALF[1]);
                                break;
                        }
                    }
                } else {
                    for (int k = 0; k < NUMS.length - 1; k++) {
                        if (email.charAt(j) == NUMS[k]) {
                            if (k <= NUMS.length - 3) {
                                ciperedEmail.append(NUMS[k + 2]);
                            } else {
                                switch (k) {
                                    case 8:
                                        ciperedEmail.append(NUMS[0]);
                                        break;
                                    case 9:
                                        ciperedEmail.append(NUMS[1]);
                                        break;
                                }
                            }
                        } else {
                            ciperedEmail.append(email.charAt(j));
                        }
                    }
                }
            }
        }

        return ciperedEmail.toString();
    }

    private String decipher(String val) {
        StringBuilder deciphered = new StringBuilder();


        return deciphered.toString();
    }*/
}