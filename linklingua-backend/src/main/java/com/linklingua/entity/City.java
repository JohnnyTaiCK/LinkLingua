package com.linklingua.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * City dictionary entity, mapped to the database table {@code city}.
 *
 * <p>Used as the basic city dictionary for the cross-border filter.</p>
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "City dictionary")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @Schema(description = "Primary key ID", example = "1")
    private Integer id;

    /** City name, e.g. Shenzhen / Hong Kong */
    @Schema(description = "City name", example = "Shenzhen")
    private String cityName;
}
