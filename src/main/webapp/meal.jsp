<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%= request.getParameter("action").equals("insert") ? "Add meal" : "Update meal" %></title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form method="POST" action='meals' name="fromAddMeal">
    <input type="hidden" readonly="readonly" name="id" value="${meal.id}"  /> <br />
    Description:
    <input type="text" name="description" value="${meal.description}" /> <br />
    Calories:
    <input type="text" name="calories" value="${meal.calories}" /> <br />
    Date:
    <fmt:parseDate value="${meal.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
    <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm" var="formattedDate"/>
    <input type="dateTime" name="dateTime" value="${formattedDate}"  /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>
