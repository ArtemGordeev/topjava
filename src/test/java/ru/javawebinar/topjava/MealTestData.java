package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final Meal USER_BREAKFAST_22_FEB = new Meal(100002, LocalDateTime.of(2020,2,22, 8, 0),"завтрак", 1000);
    public static final Meal USER_LUNCH_22_FEB = new Meal(100003, LocalDateTime.of(2020,2,22, 14, 0),"обед", 1000);
    public static final Meal USER_DINNER_22_FEB = new Meal(100004, LocalDateTime.of(2020,2,22, 20, 0),"ужин", 1000);
    public static final Meal USER_BREAKFAST_21_FEB = new Meal(100005, LocalDateTime.of(2020,2,21, 8, 0),"завтрак", 1000);
    public static final Meal USER_LUNCH_21_FEB = new Meal(100006, LocalDateTime.of(2020,2,21, 14, 0),"обед", 1000);
    public static final Meal USER_DINNER_21_FEB = new Meal(100007, LocalDateTime.of(2020,2,21, 20, 0),"ужин", 1000);
    public static final Meal ADMIN_DINNER_22_FEB = new Meal(100008, LocalDateTime.of(2020,2,22, 12, 0),"ужин", 800);

    public static final int USER = 100000;

    public static Meal getNew(){
        return new Meal(null, LocalDateTime.now(), "еда", 500);
    }

    public static Meal getUpdated(Meal meal){
        Meal updated = new Meal(meal);
        updated.setDescription("updated");
        updated.setCalories(500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
