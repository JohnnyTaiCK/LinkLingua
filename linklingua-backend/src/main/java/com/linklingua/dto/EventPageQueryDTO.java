package com.linklingua.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Query parameters for the paginated event search.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Event pagination query")
public class EventPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Page number, 1-based. */
    @Schema(description = "Page number (1-based)", example = "1")
    private Integer page;

    /** Page size. */
    @Schema(description = "Number of records per page", example = "10")
    private Integer pageSize;

    /** Optional city-name filter. */
    @Schema(description = "Filter by city name", example = "Shenzhen")
    private String city;

    /** Optional language-name filter. */
    @Schema(description = "Filter by language name", example = "Cantonese")
    private String language;

    /** Optional filter: only events starting at or after this time. */
    @Schema(description = "Only events starting at or after this time")
    private String timeRange;

    /** Only events with names containing the following keyword */
    @Schema(description = "events with keywords in the title", example = "language exchange")
    private String keyword;

    @Schema(description = "the start time of the event, used for filtering events that start at or after this time")
    private LocalDateTime startTime;

    @Schema(description = "the end time of the event, used for filtering events that end at or before this time")
    private LocalDateTime endTime;
}
