package com.linklingua.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String description;

    private String location;

    private LocalDateTime eventStartTime;

    private Integer maxParticipant;

    private Integer cityId;

    private String cityName;

    private Integer langId;

    private String langName;

    private Integer publishUserId;

    //0 upcoming, 1 ongoing, 2 closed
    private Integer eventStatus;

    private String image;
}
