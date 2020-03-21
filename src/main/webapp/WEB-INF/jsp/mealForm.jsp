<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <hr>
    <c:set var="user" value="Sergey"/>
    <h2>
        <c:set var="createMeal"><spring:message code="mealForm.createMeal"/></c:set>
        <c:set var="editMeal"><spring:message code="mealForm.editMeal"/></c:set>
        ${param.action == 'create' ? createMeal : editMeal}
    </h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="mealForm.dateTime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="mealForm.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="mealForm.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
