package com.example.projectpract;

import java.awt.*;

public class User {
    private String username;
    private String email;
    private String password;
    private int age;
    private String about;
    private String video;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, int age, String about, String video) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.about = about;
        this.video = video;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String chooseAvatar() {
        int random = (int) (Math.random() + 1) * 2;
        if (random == 1) {
            return "${pageContext.request.contextPath}/resources/Images/avatar1.png";
        } else {
            return "${pageContext.request.contextPath}/resources/Images/avatar2.png";
        }
    }
    public String modifyVideoURL(String url) {
        if (this.getVideo() == null || this.getVideo().equals("")) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= 23; i++) {
                stringBuilder.append(url.charAt(i));
            }
            stringBuilder.append("embed/");
            for (int i = 32; i <= url.length() - 1; i++) {
                stringBuilder.append(url.charAt(i));
            }
            return stringBuilder.toString();
        }
    }

}
