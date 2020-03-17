package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.AbstractMealController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getFiltered(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> meals = getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable("id") int id) {
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String updateMeal(@PathVariable("id") int id, Model model) {
        final Meal meal = get(id);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @PostMapping("/update/{id}")
    public String updateMeal(HttpServletRequest request) throws IOException {
        update(getMeal(request), getId(request));
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String createMeal(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @PostMapping("/create")
    public String createMeal(HttpServletRequest request) throws IOException {
        create(getMeal(request));
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal getMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
