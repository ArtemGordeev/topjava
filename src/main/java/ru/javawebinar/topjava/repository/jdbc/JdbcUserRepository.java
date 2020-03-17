package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.*;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final Validator validator;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static List<User> extractData(ResultSet resultSet) throws SQLException {
        Map<Integer, User> map = new HashMap<>();
        while (resultSet.next()) {
            Integer userId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            int calories_per_day = resultSet.getInt("calories_per_day");
            boolean enabled = resultSet.getBoolean("enabled");
            Timestamp registered = resultSet.getTimestamp("registered");
            map.computeIfAbsent(userId, a ->
                    new User(userId, name, email, password, calories_per_day, enabled, registered, new HashSet<Role>()));
            User user = map.get(userId);
            String role = resultSet.getString("role");
            Set<Role> roles = user.getRoles();
            roles.add(Role.valueOf(role));
        }
        return new ArrayList<User>(map.values());
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            final List<Role> roles = new ArrayList<>(user.getRoles());
            final Integer id = user.getId();
            jdbcTemplate.batchUpdate(
                    //"UPDATE user_roles set role = ? where user_id = ?",
                    "insert into user_roles(USER_ID, ROLE)  VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, id);
                            ps.setString(2, roles.get(i).toString());
                        }

                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT t1.*, t2.role from users t1 left join (select * from user_roles) t2 ON t1.id = t2.user_id where id=?",
                JdbcUserRepository::extractData,
                id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT t1.*, t2.role from users t1 left join (select * from user_roles) t2 ON t1.id = t2.user_id where email=?",
                JdbcUserRepository::extractData,
                email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT t1.*, t2.role from users t1 left join (select * from user_roles) t2 ON t1.id = t2.user_id  ORDER BY name, email",
                JdbcUserRepository::extractData);
    }
}
