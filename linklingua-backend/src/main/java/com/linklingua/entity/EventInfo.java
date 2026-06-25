package com.linklingua.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Core language exchange event entity, mapped to the database table {@code event_info}.
 *
 * <p>This is the core table of the project.</p>
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Language exchange event")
public class EventInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique event id */
    @Schema(description = "Unique event id", example = "1")
    private Integer id;

    /** Event title */
    @Schema(description = "Event title", example = "Cantonese Coffee Meetup")
    private String title;

    /** Detailed event introduction */
    @Schema(description = "Detailed event introduction")
    private String description;

    /** Specific offline address */
    @Schema(description = "Specific offline address", example = "Coco Park, Futian District")
    private String location;

    /** Event start datetime */
    @Schema(description = "Event start datetime")
    private LocalDateTime eventStartTime;

    /** Max join people count */
    @Schema(description = "Max join people count", example = "50")
    private Integer maxParticipant;

    /** Foreign key -> city.id */
    @Schema(description = "City id (foreign key -> city.id)", example = "1")
    private Integer cityId;

    /** Foreign key -> language_tag.id */
    @Schema(description = "Language id (foreign key -> language_tag.id)", example = "3")
    private Integer langId;

    /** Foreign key -> user.id (publisher) */
    @Schema(description = "Publisher user id (foreign key -> user.id)", example = "1")
    private Integer publishUserId;

    /** Event status: 1=Upcoming, 2=Ongoing, 3=Finished */
    @Schema(description = "Event status: 1=Upcoming, 2=Ongoing, 3=Finished", example = "1")
    private Integer eventStatus;

    /** Event cover image URL */
    @Schema(description = "Event cover image URL", example = "https://example.com/cover.png")
    private String image;

    /** Event publish time */
    @Schema(description = "Event publish time")
    private LocalDateTime createTime;

    /** Event last updated time */
    @Schema(description = "Event last updated time")
    private LocalDateTime updateTime;

    /** The user id of the user who created this event */
    @Schema(description = "Id of the user who created this event", example = "1")
    private Integer createdBy;

    /** The user id of the user who updated this event */
    @Schema(description = "Id of the user who updated this event", example = "1")
    private Integer updatedBy;
}
