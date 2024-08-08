package ru.demyan.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.demyan.repository.ClientRepository;
import ru.demyan.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Get Prediction DataBase Controller", description = "Получение базы данных предсказаний")
public class GetPredictionDBController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/get-prediction")
    public ResponseEntity<Map<String, List<String>>> getPrediction(@RequestParam("client-number") int clientNumber) {
        var clients = clientRepository.findAll();
        Map<String, List<String>> predictions = new HashMap<>();
        for (var client : clients) {
            String clientInfo = client.toString();
            List<String>services = new ArrayList<>();
            for (var serviceId: client.getServices()) {
                var service = serviceRepository.findById(serviceId);
                service.ifPresent(value -> services.add(value.toString()));
            }
            predictions.put(clientInfo, services);
            clientNumber -= 1;
            if (clientNumber <= 0) break;
        }
        return ResponseEntity.ok(predictions);
    }
}
