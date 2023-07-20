package ru.practicum.servicies.logicservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.dto.CreateEndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatClient statClient;

    @Override
    public void saveStat(HttpServletRequest request) {

        CreateEndpointHitDto createEndpointHitDto = CreateEndpointHitDto.builder()
                .app("ExploreWithMe")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        statClient.saveStat(createEndpointHitDto);
    }
}
