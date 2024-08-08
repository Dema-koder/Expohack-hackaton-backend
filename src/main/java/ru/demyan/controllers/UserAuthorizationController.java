package ru.demyan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.demyan.exception.WrongUserException;
import ru.demyan.service.DynamicTableService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "User Authorization Endpoint", description = "API для авторизации пользователя по логину и паролю и получения соответствующей таблицы.")
public class UserAuthorizationController {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя по логину и паролю и возвращает таблицу с данными, связанными с этим пользователем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация и получение таблицы",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден или неверные учетные данные",
                    content = @Content)
    })
    @GetMapping("/login")
    public ResponseEntity<List<Map<String, Object>>> login(@Parameter(description = "Логин пользователя", required = true, example = "autoexpress")
                                                               @RequestParam("login") String login,
                                                           @Parameter(description = "Пароль пользователя", required = true, example = "pass1")
                                                           @RequestParam("password") String password,
                                                           @Parameter(description = "Номер страницы для постраничного вывода данных", required = true, example = "1")
                                                               @RequestParam("page-number") int pageNumber) {
        List<Map<String, Object>> table = new ArrayList<>();
        try {
            table = dynamicTableService.getDataForUser(login, password, pageNumber);
        } catch (WrongUserException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(table);
        }
        log.info("Возвращаю таблицу");
        return ResponseEntity.ok(table);
    }
}
