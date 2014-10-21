<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">
</head>
<body>

<div id="info">

    <div id="error_message">
        ${errorMessage}
    </div>

    <div>
        <a href="/JspServlet/index.jsp">Login page</a>
    </div>

</div>

</body>
</html>