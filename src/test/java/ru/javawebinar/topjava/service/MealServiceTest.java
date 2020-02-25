package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_BREAKFAST_22_FEB.getId(), USER);
        assertMatch(meal, USER_BREAKFAST_22_FEB);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(USER_BREAKFAST_22_FEB.getId(), USER);
        service.get(USER_BREAKFAST_22_FEB.getId(), USER);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER);
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDate startDate = LocalDate.of(2020, 2, 22);
        List<Meal> actual = service.getBetweenHalfOpen(startDate, null, USER);
        List<Meal> expected = Arrays.asList(
                USER_DINNER_22_FEB,
                USER_LUNCH_22_FEB,
                USER_BREAKFAST_22_FEB
               );
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER);
        List<Meal> expected = Arrays.asList(
                USER_DINNER_22_FEB,
                USER_LUNCH_22_FEB,
                USER_BREAKFAST_22_FEB,
                USER_DINNER_21_FEB,
                USER_LUNCH_21_FEB,
                USER_BREAKFAST_21_FEB);
        assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal meal = USER_BREAKFAST_22_FEB;
        Meal updated = getUpdated(meal);
        service.update(updated, USER);
        assertMatch(service.get(updated.getId(), USER), updated);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId,USER), newMeal);

    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUsersMeal(){
        service.delete(USER_BREAKFAST_22_FEB.getId(), USER+1);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUsersMeal(){
        service.get(USER_BREAKFAST_22_FEB.getId(), USER+1);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUsersMeal(){
        service.update(USER_BREAKFAST_22_FEB, USER+1);
    }
}