<%--
  Created by IntelliJ IDEA.
  User: seulkian
  Date: 4/15/24
  Time: 1:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>register</title>
</head>
<body>
    <form action="/todo/register" method="post">
        <div>
            title: <input type="text" name="title" />
        </div>
        <div>
            DueDate: <input type="date" name="dueDate" value="2022-12-25" />
        </div>
        <div>
            Writer: <input type="text" name="writer" />
        </div>
        <div>
            Finished: <input type="checkbox" name="finished" />
        </div>
        <div>
            <button type="submit">Register</button>
        </div>
    </form>
</body>
</html>
