<%@ page import="com.example.projectpract.User" %>
<%@ page import="com.example.projectpract.Cipher" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/editStyle.css">
</head>
<body>
<% User user = (User) session.getAttribute("user");%>
<% Cipher cipher = new Cipher();%>
<div class="bodydiv">
    <div class="container rounded bg-white mt-5">
        <div class="row">
            <div class="col-md-4 border-right">
                <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                    <%
                        String imageURL = "";
                        int rnd = (int)(Math.random()*2)+1;
                        if (rnd == 1) {
                            imageURL = "/resources/Images/avatar1.png";
                        } else if (rnd == 2) {
                            imageURL = "/resources/Images/avatar2.png";
                        }
                    %>
                    <img class="rounded-circle mt-5" src="${pageContext.request.contextPath}<%=imageURL%>" width="90">
                    <span class="font-weight-bold"><%=user.getUsername()%></span>
                    <span class="text-black-50"><%=user.getEmail()%></span>
                    <%
                        String str = "";
                        if (user.getAge() > 0) {
                            str = String.valueOf(user.getAge());
                        }
                    %>
                    <span><%=str%></span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="p-3 py-5">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div class="d-flex flex-row align-items-center back"><i
                                class="fa fa-long-arrow-left mr-1 mb-1"></i>
                            <a class="back_link" href="account"><h6>Back to home</h6></a>
                        </div>
                        <h6 class="text-right">Edit Profile</h6>
                    </div>
                    <form action="save" name="save-changes" method="get">
                    <div class="row mt-2">
                        <div class="col-md-6">
                            <input type="text" class="form-control" placeholder="first name" name="save-username" value="<%=user.getUsername()%>">
                        </div>
                        <div class="col-md-6">
                            <input type="password" class="form-control" name="save-password" value=<%=cipher.deCipher(user.getPassword())%> placeholder="Password">
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-md-6">
                            <input type="text" class="form-control" placeholder="Email" name="save-email" value=<%=user.getEmail()%>>
                        </div>
                        <div class="col-md-6">
                            <input type="text" class="form-control" name="save-age" value=<%=user.getAge()%> placeholder="Age">
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-md-6">
                            <input type="text" class="form-control" placeholder="About" name="save-about" value="<%=user.getAbout()%>">
                        </div>
                        <div class="col-md-6">
                            <input type="text" class="form-control" placeholder="Video that describe You" name="save-video" value=<%=user.getVideo()%>>
                        </div>
                    </div>
                    <div class="mt-5 text-right">
                        <button class="btn btn-primary profile-button" type="submit">Save Profile</button>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
