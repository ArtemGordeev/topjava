package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void changeRoles() throws Exception{
        User user = service.get(USER_ID);
        user.getRoles().remove(Role.ROLE_USER);
        service.update(user);
        User userUpdated = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, userUpdated);
    }
}