<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%--<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">--%>
    <%--<link type="text/css"--%>
          <%--href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />--%>
    <%--<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>--%>
    <%--<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>--%>
    <title>Add new meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form method="POST" action='meals' name="fromAddMeal">
    <input type="hidden" readonly="readonly" name="id"
           value="<c:out value="${meal.id}"  />" /> <br />
    Description:
    <input type="text" name="description"
           value="<c:out value="${meal.description}" />" /> <br />
    Calories:
    <input type="text" name="calories"
           value="<c:out value="${meal.calories}" />" /> <br />
    Date:
    <fmt:parseDate value="${meal.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
    <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm" var="formattedDate"/>
    <input type="dateTime" name="dateTime"
           value="<c:out value="${formattedDate}" />" /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>
