package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.util.List;
import java.util.Map;

@Component
public class StatClient {

    private final RestTemplate restTemplate;

    private final String serverUrl;

    @Autowired
    public StatClient(@Value("${stat-server}") String serverUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<EndpointHitDto> saveStat(CreateEndpointHitDto createEndpointHitDto) {
        HttpEntity<CreateEndpointHitDto> request =
                new HttpEntity<CreateEndpointHitDto>(createEndpointHitDto);
        return restTemplate.postForEntity(
                serverUrl + "/hit", request, EndpointHitDto.class);
    }

    public ResponseEntity<List<ViewStats>> getViewStats(Map<String, Object> parameters) {

        String url = serverUrl + "/stats?start={start}&end={end}";

        if (parameters.containsKey("unique")) {
            url = url + "&unique={unique}";
        }

        if (parameters.containsKey("uris")) {
            url = url + "&uris={uris}";
        }

        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ViewStats>>() {
                }, parameters);
    }
}
