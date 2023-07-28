package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
    Dto лайка
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LikeDto {

    private Long userId;

    private Long eventId;

    private Integer status;

}
