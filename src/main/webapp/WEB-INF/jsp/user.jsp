<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>User</title>
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">
</head>

<body>

<div id="info">

    <div id="user_name">
        Hello,
     <%--   <jsp:useBean id="user" scope="session"  class="entity.User"/> --%>
        <c:out value="${user.firstName}" />!
    </div>

    <div>
        Click
        <a href="/JspServlet/log-out">here</a>
        to log out
    </div>

</div>

</body>

</html>