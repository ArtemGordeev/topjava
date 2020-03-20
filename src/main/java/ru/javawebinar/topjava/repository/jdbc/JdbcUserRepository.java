package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.groups.Default;
import java.sql.*;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends JdbcRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super();
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    private static List<User> extractData(ResultSet resultSet) throws SQLException {
        Map<Integer, User> map = new HashMap<>();
        while (resultSet.next()) {
            Integer userId = resultSet.getInt("id");
            final User user = ROW_MAPPER.mapRow(resultSet, resultSet.getRow());
            user.setRoles(new HashSet<>());
            map.putIfAbsent(userId, user);
            User userFromMap = map.get(userId);
            Role role = Role.valueOf(resultSet.getString("role"));
            userFromMap.getRoles().add(role);
        }
        return new ArrayList<User>(map.values());
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user, Default.class);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                    "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            namedParameterJdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:id", parameterSource);
        }
        final List<Role> roles = new ArrayList<>(user.getRoles());
        final Integer id = user.getId();
        jdbcTemplate.batchUpdate(
                "insert into user_roles(USER_ID, ROLE)  VALUES (?, ?)",
                getBatchPreparedStatementSetter(roles, id));
        return user;
    }

    private BatchPreparedStatementSetter getBatchPreparedStatementSetter(List<Role> roles, Integer id) {
        return new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setString(2, roles.get(i).toString());
            }

            public int getBatchSize() {
                return roles.size();
            }
        };
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
