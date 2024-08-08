package ru.demyan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.demyan.exception.WrongUserException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DynamicTableService {
    private final JdbcTemplate jdbcTemplate;
    private static final Map<String, String>USERS = new HashMap<>();
    private static final int PAGE_SIZE = 10;

    static {
        USERS.put("autoexpress", "pass1");
        USERS.put("d2_insurance", "pass2");
        USERS.put("expobank", "pass3");
        USERS.put("expocar", "pass4");
        USERS.put("leasing_1", "pass5");
        USERS.put("leasing_2", "pass6");
        USERS.put("leasing_3", "pass7");
        USERS.put("leasing_4", "pass8");
        USERS.put("leasing_5", "pass9");
        USERS.put("park_hotel_khvoya", "pass10");
    }

    public List<Map<String, Object>>getDataForUserByUsername(String username, int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        String sql = String.format("SELECT * FROM %s LIMIT %d OFFSET %d", username, PAGE_SIZE, offset);

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getDataForUser(String username, String password, int pageNumber) throws WrongUserException {
        String tableName = determineTableNameForUser(username, password);
        int offset = (pageNumber - 1) * PAGE_SIZE;
        String sql = String.format("SELECT * FROM %s LIMIT %d OFFSET %d", tableName, PAGE_SIZE, offset);

        return jdbcTemplate.queryForList(sql);
    }

    private String determineTableNameForUser(String username, String password) {
        if (USERS.containsKey(username)) {
            if (USERS.get(username).equals(password)) {
                return username;
            } else {
                throw new WrongUserException("Неверный пароль!");
            }
        } else {
            throw new WrongUserException("Такого пользователя нет! Логин: " + username);
        }
    }
}
