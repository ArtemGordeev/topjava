package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    MealDao dao = new MealDaoMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
        if (action != null) {
            switch (action) {
                case "delete": {
                    int mealId = Integer.parseInt(request.getParameter("mealId"));
                    dao.deleteMeal(mealId);
                    response.sendRedirect("meals");
                    return;
                }
                case "edit": {
                    forward = "/meal.jsp";
                    int mealId = Integer.parseInt(request.getParameter("mealId"));
                    Meal meal = dao.getMealById(mealId);
                    request.setAttribute("meal", meal);
                    break;
                }
                case "insert":
                    forward = "/meal.jsp";
                    break;
                default:
                    forward = "/meals.jsp";
                    request.setAttribute("mealsTo", mealsTo);
            }
        } else {
            forward = "/meals.jsp";
            request.setAttribute("mealsTo", mealsTo);
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        meal.setDateTime(dateTime);
        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty()) {
            dao.addMeal(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.updateMeal(meal);
        }
        response.sendRedirect("meals");
    }
}
