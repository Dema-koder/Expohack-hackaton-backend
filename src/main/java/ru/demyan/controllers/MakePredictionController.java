package ru.demyan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.demyan.service.MakePredictionService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Make Prediction Controller", description = "Эндпоинт для предсказания и занесения информации в БД предсказаний")
public class MakePredictionController {

    @Autowired
    private MakePredictionService makePredictionService;

    @Operation(summary = "Создание предсказания", description = "Выполняет предсказание на основе переданных параметров и сохраняет результат в БД.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Предсказание успешно выполнено и сохранено в БД"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса", content = @Content)
    })
    @GetMapping("/predict")
    public ResponseEntity<String> makePrediction(
            @Parameter(description = "Имя клиента", example = "Дмитрий")
            @RequestParam("first_name") String firstName,
            @Parameter(description = "Отчество клиента", example = "Дмитриевич")
            @RequestParam("middle_name") String middleName,
            @Parameter(description = "Фамилия клиента", example = "Дмитриев")
            @RequestParam("last_name") String lastName,
            @Parameter(description = "Номер клиента", example = "+79054124542")
            @RequestParam("phone") String phone,
            @Parameter(description = "Email клиента", example = "example@gmail.com")
            @RequestParam("email") String email,
            @Parameter(description = "Последняя оказанная услуга", example = "Лизинг 4_юридические_продажа и лизинг грузовых транспортных средств")
            @RequestParam("service") String service,
            @Parameter(description = "Семейный статус", example = "Никогда не состоял")
            @RequestParam("family_status") String familyStatus,
            @Parameter(description = "Количество детей", example = "0")
            @RequestParam("children") String children,
            @Parameter(description = "Доход", example = "60000")
            @RequestParam("income") String income,
            @Parameter(description = "add_income", example = "0")
            @RequestParam("add_income") String addIncome,
            @Parameter(description = "Возраст", example = "23")
            @RequestParam("age") String age) {

        try {
            Map<String, String> clientInfo = new HashMap<>();
            clientInfo.put("first_name", firstName);
            clientInfo.put("middle_name", middleName);
            clientInfo.put("last_name", lastName);
            clientInfo.put("phone", phone);
            clientInfo.put("email", email);
            clientInfo.put("service", service);
            clientInfo.put("family_status", familyStatus);
            clientInfo.put("children", children);
            clientInfo.put("income", income);
            clientInfo.put("add_income", addIncome);
            clientInfo.put("age", age);

            makePredictionService.addPredictionToDB(clientInfo);

            return ResponseEntity.ok("Prediction successfully added to the database");

        } catch (Exception e) {
            log.error("Error occurred during prediction: {}", e.getMessage());
            return ResponseEntity.status(404).body("Error occurred during prediction");
        }
    }
}