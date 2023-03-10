<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Slide Navbar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/loginStyle.css">
    <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
</head>
<body>
<div class="main">
    <input type="checkbox" id="chk" aria-hidden="true">
    <div class="signup">
        <label class="signup_label" for="chk" aria-hidden="true"><a class="sign_up_button" href="/ProjectPract/sign-up">Sign up</a></label>
    </div>
    <div class="login">
        <form>
            <label for="chk" aria-hidden="true"><span class="login_button">Login</span></label>
            <input type="email" name="email" placeholder="Email" required="">
            <input type="password" name="pswd" placeholder="Password" required="">
            <button >Login</button>
        </form>
    </div>
</div>
</body>
</html>