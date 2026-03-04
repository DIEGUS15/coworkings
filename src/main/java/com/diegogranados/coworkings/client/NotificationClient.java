package com.diegogranados.coworkings.client;

import com.diegogranados.coworkings.dto.NotificacionRequest;
import com.diegogranados.coworkings.dto.NotificacionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${notification.service.url:http://localhost:8081/api/notificaciones/enviar}")
    private String notificationUrl;

    public void enviar(String email, String documento, String mensaje, String sede) {
        NotificacionRequest request = NotificacionRequest.builder()
                .email(email)
                .documento(documento)
                .mensaje(mensaje)
                .sede(sede)
                .build();
        try {
            NotificacionResponse response = restTemplate.postForObject(
                    notificationUrl, request, NotificacionResponse.class);
            log.info("[NotificationClient] Respuesta del microservicio: {}", response);
        } catch (RestClientException ex) {
            log.error("[NotificationClient] Error al contactar el microservicio de notificaciones: {}",
                    ex.getMessage());
        }
    }
}
