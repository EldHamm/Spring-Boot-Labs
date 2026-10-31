package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.model.Request;
import com.hammatov.MySecondTestAppSpringBoot.model.Response;
import com.hammatov.MySecondTestAppSpringBoot.model.Systems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ModifySystemNameRequestService implements ModifyRequestService {

    // Адрес Сервиса 2
    private static final String SERVICE_2_URL = "http://localhost:8084/feedback";

    private final RestTemplate restTemplate;

    public ModifySystemNameRequestService() {
        // Таймауты, чтобы Сервис 1 не зависал навсегда, если Сервис 2 не отвечает
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public void modify(Request request) {
        Systems oldSystemName = request.getSystemName();
        request.setSystemName(Systems.SERVICE_1);
        log.info("ModifySystemNameRequestService: systemName изменён: '{}' -> '{}'",
                oldSystemName, request.getSystemName());

        HttpEntity<Request> httpEntity = new HttpEntity<>(request);

        log.info("ModifySystemNameRequestService: отправка request на Сервис 2 ({}): {}", SERVICE_2_URL, request);
        ResponseEntity<Response> answer = restTemplate.exchange(
                SERVICE_2_URL,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<Response>() {
                });

        log.info("ModifySystemNameRequestService: ответ Сервиса 2: HTTP {}, body: {}",
                answer.getStatusCode(), answer.getBody());
    }

}
