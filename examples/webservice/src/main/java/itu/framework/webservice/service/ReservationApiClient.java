package itu.framework.webservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import itu.framework.webservice.dto.ReservationDto;

@Service
public class ReservationApiClient {
    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl = "http://127.0.0.1:8082/Avion/api";

    public ReservationDto getReservationById(Integer id) {
        String url = baseUrl + "/reservation/" + id;
        return rest.getForObject(url, ReservationDto.class);
    }
}
