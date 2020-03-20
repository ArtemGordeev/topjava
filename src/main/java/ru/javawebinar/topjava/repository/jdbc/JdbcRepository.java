package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public abstract class JdbcRepository {
    private final Validator validator;

    public JdbcRepository() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public <T> void validate(T var1, Class<?> var2) {
        Set<ConstraintViolation<T>> validate = validator.validate(var1, var2);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }
}
