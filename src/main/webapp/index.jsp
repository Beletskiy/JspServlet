<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>

<head>
    <title>Login</title>
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">
</head>

<body>

<div id="sign_in">

    <form method="post" action="login">

        <div class="fields">
            <label for="login">Login:</label>
            <input type="text" id="login" name="login" size="30"
                   value="${login}">

            <c:if test="${not empty errors ['login']}">
                <div class="error">${errors ['login']}</div>
            </c:if>
        </div>

        <div class="fields">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" size="30">

            <c:if test="${not empty errors['password']}">
                <div class="error">${errors['password']}</div>
            </c:if>
        </div>

        <div class="buttons">
            <input type="submit" name="submit" value="Sign in">
        </div>

    </form>

</div>

</body>
</html>
