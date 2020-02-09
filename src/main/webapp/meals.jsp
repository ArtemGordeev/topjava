<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsTo}" var="meal">
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>
                <fmt:parseDate value="${meal.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&mealId=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add meal</a></p>
</body>
</html>