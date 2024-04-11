<%--
  Created by IntelliJ IDEA.
  User: seulkian
  Date: 4/11/24
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo Register</title>
</head>
<body>
    <form action="/todo/register" method="post">
        <div>
            <input type="text" name="title" placeholder="INSERT TITLE"/>
        </div>

        <div>
            <input type="date" name="dueDate" />
        </div>

        <div>
            <button type="reset">reset</button>
            <button type="submit">REGISTER</button>
        </div>
    </form>
</body>
</html>
