<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><c:out value="${userInfo['page-name']}" /></title>
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
</head>
<body>

<div id="header">
    <div id="action_name">
        <c:out value="${userInfo['page-name']}" />&nbsp;user
    </div>
    <div id="admin_name">
        Admin&nbsp;<c:out value="${user.firstName}" />&nbsp;
        (<a href="/JspServlet/log-out">Log&nbspout</a>)
    </div>
    <div class="no_float"></div>
</div>

<div id="user_form">
    <form method="post" action="AddEditServlet">

        <input type="text" hidden name="page-name"
               value="${userInfo['page-name']}">

        <c:choose>
            <c:when test="${userInfo['page-name'] eq 'Add'}">
                <div class="fields">
                    <label for="login">Login</label>
                    <input type="text" id="login" name="login" size="50"
                           value="${userInfo['login']}">
                    <span>*</span>
                    <c:if test="${not empty errors['login']}">
                        <div class="error">
                                ${errors['login']}
                        </div>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <input type="text" hidden name="login"
                       value="${userInfo['login']}">
            </c:otherwise>
        </c:choose>

        <div class="fields">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" size="50">
            <span>*</span>

            <c:if test="${not empty errors['password']}">
                <div class="error">
                        ${errors['password']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="password_again">Password again</label>
            <input type="password" id="password_again" name="password-again"
                   size="50">
            <span>*</span>
            <c:if test="${not empty errors['password-again']}">
                <div class="error">
                        ${errors['password-again']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="email">Email</label>
            <input type="text" id="email" name="email" size="50"
                   value="${userInfo['email']}">
            <span>*</span>

            <c:if test="${not empty errors['email']}">
                <div class="error">
                        ${errors['email']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="first_name">First name</label>
            <input type="text" id="first_name" name="first-name" size="50"
                   value="${userInfo['first-name']}">
            <span>*</span>

            <c:if test="${not empty errors['first-name']}">
                <div class="error">
                        ${errors['first-name']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="last_name">Last name</label>
            <input type="text" id="last_name" name="last-name" size="50"
                   value="${userInfo['last-name']}">
            <span>*</span>

            <c:if test="${not empty errors['last-name']}">
                <div class="error">
                        ${errors['last-name']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="birthday">Birthday</label>
            <input type="text" id="birthday" name="birthday" size="50"
                   value="${userInfo['birthday']}">
            <span>*</span>

            <c:if test="${not empty errors['birthday']}">
                <div class="error">
                        ${errors['birthday']}
                </div>
            </c:if>
        </div>

        <div class="fields">
            <label for="role">Role</label>
            <select size="1" id="role" name="role">
                <option selected value="user">user</option>
                <option value="admin">admin</option>
            </select>
        </div>

        <div id="buttons">
            <div id="ok">
                <input type="submit" name="submit" value="OK">
            </div>

            <div id="cancel">
                <input type="button" value="Cancel" name="Cancel"
                       onclick="document.location = 'AdminServlet';
                    return true;" />
            </div>
        </div>

    </form>
</div>
</body>
</html>