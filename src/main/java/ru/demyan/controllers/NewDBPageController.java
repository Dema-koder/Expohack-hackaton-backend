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
import ru.demyan.service.DynamicTableService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "New database page endpoint", description = "Отобразить страницу номер page-number базы данных")
public class NewDBPageController {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Operation(
            summary = "Получение данных из таблицы по логину и номеру страницы",
            description = "Возвращает данные из таблицы, соответствующей пользователю, на основе логина и номера страницы."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение данных",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/page")
    public ResponseEntity<List<Map<String, Object>>> newDBPage(
            @Parameter(description = "Логин пользователя", required = true, example = "autoexpress")
            @RequestParam("login") String login,
            @Parameter(description = "Номер страницы для отображения", required = true, example = "1")
            @RequestParam("page-number") int pageNumber) {

        var table = dynamicTableService.getDataForUserByUsername(login, pageNumber);
        return ResponseEntity.ok(table);
    }
}
