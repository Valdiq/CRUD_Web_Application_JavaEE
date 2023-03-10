<%@ page import="com.example.projectpract.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profileStyle.css">
</head>
<body>
<% User user = (User) session.getAttribute("user");%>
<div class="row py-5 px-4">
    <div class="col-md-5 mx-auto">
        <div class="bg-white shadow rounded overflow-hidden">
            <div class="px-4 pt-0 pb-4 cover">
                <button class="log-out" name="logoutButt"><a href="logout">Log out</a></button>
                <div class="media align-items-end profile-head">
                    <div class="profile mr-3">
                        <%
                            String imageURL = "";
                            int rnd = (int)(Math.random()*2)+1;
                            if (rnd == 1) {
                                imageURL = "/resources/Images/avatar1.png";
                            } else if (rnd == 2) {
                                imageURL = "/resources/Images/avatar2.png";
                            }
                        %>
                        <img src="${pageContext.request.contextPath}<%=imageURL%>" alt="..." width="130" class="rounded mb-2 img-thumbnail">
                        <a class="btn btn-outline-dark btn-sm btn-block" href="edit-profile">Edit profile</a>
                    </div>
                    <div class="media-body mb-5 text-white">
                        <%
                            String str = "";
                            if (user.getAge() <= 0) {
                                str = user.getUsername();
                            } else {
                                str = user.getUsername() + ", " + user.getAge();
                            }
                        %>
                        <h4 class="mt-0 mb-0">
                            <%=str%>
                        </h4>
                        <p class="small mb-4">
                            <i class="fas fa-map-marker-alt mr-2">
                                <%=user.getEmail()%>
                            </i>
                        </p>
                    </div>
                </div>
            </div>
            <div class="px-4 py-3"><h5 class="mb-0">About</h5>
                <div class="p-4 rounded shadow-sm bg-light">
                    <p class="font-italic mb-0">
                        <%=user.getAbout()%>
                    </p>
                </div>
                <div class="py-4 px-4">
                    <div class="d-flex align-items-center justify-content-between mb-3">
                        <h5 class="mb-0">Video that describe you</h5>
                    </div>
                    <div class="video">
                        <iframe width="560" height="315" src="<%=user.modifyVideoURL(user.getVideo())%>"
                                title="YouTube video player" frameborder="0"
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
                        </iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
