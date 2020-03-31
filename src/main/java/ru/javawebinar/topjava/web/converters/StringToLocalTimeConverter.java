package ru.javawebinar.topjava.web.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String s) {
        if (s.length() == 0) {
            return null;
        }
        return LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
