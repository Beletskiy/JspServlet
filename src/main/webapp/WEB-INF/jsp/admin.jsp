 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <%@ taglib uri="/WEB-INF/tags/tags.tld" prefix="tags"%> 


<!DOCTYPE html>
<html>
<head>
    <title>Admin</title>
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
</head>

<body>


<div id="admin_panel">
    <div id="admin_name">
        Admin <c:out value="${user.firstName}" />
        (<a href="/JspServlet/log-out">Log out</a>)
    </div>
    <div id="add_user">
        <a href="/JspServlet/AddEditServlet">Add new user</a>
    </div>
     <div id="users_list">
         <table>
             <tags:usersList/>  
        </table> 
    </div> 
</div>

</body>
</html>