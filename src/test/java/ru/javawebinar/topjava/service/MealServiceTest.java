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
import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final int USER = 100000;

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100002, USER);
        assertMatch(meal, meals.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(100002, USER);
        service.get(100002, USER);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER);
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDate startDate = LocalDate.of(2020, 2, 22);
        List<Meal> actual = service.getBetweenHalfOpen(startDate, null, USER);
        List<Meal> expected = Arrays.asList(meals.get(0), meals.get(1), meals.get(2));
        expected.sort(Comparator.comparing(Meal::getDateTime).reversed());
        for (int i = 0; i < actual.size(); i++) {
            assertMatch(actual.get(i), expected.get(i));
        }
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER);
        List<Meal> expected = Arrays.asList(meals.get(0), meals.get(1), meals.get(2), meals.get(3), meals.get(4), meals.get(5));
        expected.sort(Comparator.comparing(Meal::getDateTime).reversed());
        for (int i = 0; i < actual.size(); i++) {
            assertMatch(actual.get(i), expected.get(i));
        }
    }

    @Test
    public void update() {
        Meal meal = meals.get(0);
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
        service.delete(meals.get(0).getId(), USER+1);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUsersMeal(){
        service.get(meals.get(0).getId(), USER+1);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUsersMeal(){
        service.update(meals.get(0), USER+1);
    }
}