package ru.demyan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.demyan.model.Client;
import ru.demyan.repository.ClientRepository;
import ru.demyan.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakePredictionService {
    private final RestTemplate restTemplate;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Value("${recomendation_model_url}")
    String recomendationModelUrl;

    public void addPredictionToDB(Map<String, String> clientInfo) {
        log.info("Add prediction to DB");
        var predictions = makePrediction(clientInfo);
        Client client = new Client(clientInfo);
        List<Long>serviceIds = new ArrayList<>();
        for (var prediction : predictions) {
            ru.demyan.model.Service service = new ru.demyan.model.Service(prediction);
            Long id = serviceRepository.save(service).getId();
            serviceIds.add(id);
        }
        client.setServices(serviceIds);
        clientRepository.save(client);
    }

    private List<Map<String, String>> makePrediction(Map<String, String> clientInfo) {
        log.info("Make prediction");
        String baseUrl = recomendationModelUrl;
        log.info("baseUrl: {}", baseUrl);

        log.info(clientInfo.get("service"));
        String queryParams = String.format("service=%s&family_status=%s&children=%s&income=%s&add_income=%s&age=%s",
                clientInfo.get("service"),
                clientInfo.get("family_status"),
                clientInfo.get("children"),
                clientInfo.get("income"),
                clientInfo.get("add_income"),
                clientInfo.get("age")
        );
        String url = baseUrl + "?" + queryParams;
        log.info("URL: {}", url);
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Successfully retrieved predictions");
            log.info(response.getBody().toString());
            return response.getBody();
        } else {
            log.error("Ошибка при вызове внешнего сервиса: {}", response.getStatusCode());
            return null;
        }
    }
}
