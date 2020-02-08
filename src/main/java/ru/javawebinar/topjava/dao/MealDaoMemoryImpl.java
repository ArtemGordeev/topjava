package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMemoryImpl implements MealDao {
    private static Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    static {
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(AUTO_ID.get(), new Meal(AUTO_ID.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }


    @Override
    public void addMeal(Meal meal) {
        meal.setId(AUTO_ID.getAndIncrement());
        meals.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        meals.remove(mealId);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getMealById(int mealId) {
        return meals.get(mealId);
    }
}