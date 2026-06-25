package com.linklingua.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Request DTO for creating or updating an event.
 *
 * <p>Audit fields (create time, created by, etc.) are managed by the server and are not
 * part of this request payload.</p>
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Event create/update request")
public class EventInfoDTO implements Serializable {

    /** Event id (required only when updating). */
    @Schema(description = "Event id (required for update, ignored on create)", example = "1")
    private Integer id;

    /** Event title */
    @Schema(description = "Event title", example = "Cantonese Coffee Meetup", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Event title must not be blank")
    @Size(max = 200, message = "Event title must not exceed 200 characters")
    private String title;

    /** Detailed event introduction */
    @Schema(description = "Detailed event introduction", example = "A relaxed meetup to practice Cantonese over coffee.")
    private String description;

    /** Specific offline address */
    @Schema(description = "Specific offline address", example = "Coco Park, Futian District", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Location must not be blank")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    /** Event start datetime */
    @Schema(description = "Event start datetime", example = "2026-07-01T19:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Event start time must not be null")
    private LocalDateTime eventStartTime;

    /** Max join people count */
    @Schema(description = "Max join people count", example = "50")
    @Min(value = 1, message = "Max participant must be at least 1")
    private Integer maxParticipant;

    /** Foreign key -> city.id */
    @Schema(description = "City id (foreign key -> city.id)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "City id must not be null")
    private Integer cityId;

    /** Foreign key -> language_tag.id */
    @Schema(description = "Language id (foreign key -> language_tag.id)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Language id must not be null")
    private Integer langId;

    /** Foreign key -> user.id (publisher) */
    @Schema(description = "Publisher user id (foreign key -> user.id)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Publisher user id must not be null")
    private Integer publishUserId;

    /** Event status: 1=Upcoming, 2=Ongoing, 3=Finished (defaults to 1 when omitted) */
    @Schema(description = "Event status: 1=Upcoming, 2=Ongoing, 3=Finished", example = "1")
    @Min(value = 1, message = "Event status must be between 1 and 3")
    private Integer eventStatus;

    /** Event cover image URL */
    @Schema(description = "Event cover image URL", example = "https://example.com/cover.png")
    @Size(max = 250, message = "Image URL must not exceed 250 characters")
    private String image;
}
