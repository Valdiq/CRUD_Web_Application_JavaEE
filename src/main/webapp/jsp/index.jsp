<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
</head>
<body>
<div class="main">
    <input type="checkbox" id="chk" aria-hidden="true">

    <div id="sgup" class="signup">
        <form action="account">
            <label id="signUpText" for="chk" aria-hidden="true">Sign up</label>
            <input type="text" name="reg_username" placeholder="User name" required="">
            <input type="email" name="reg_email" placeholder="Email" required="">
            <input type="password" name="reg_pswd" placeholder="Password" required="">
            <button type="submit" name="reg_button">Sign up</button>
        </form>
    </div>

    <div class="login">
        <form action="account">
            <label for="chk" aria-hidden="true">Login</label>
            <input type="email" name="log_email" placeholder="Email" required="">
            <input type="password" name="log_pswd" placeholder="Password" required="">
            <button type="submit" name="log_button">Login</button>
        </form>
    </div>
</div>
</body>
</html>

