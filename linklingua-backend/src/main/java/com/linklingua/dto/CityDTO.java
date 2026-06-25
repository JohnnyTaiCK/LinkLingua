package com.linklingua.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * Request DTO for creating or updating a city.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "City create/update request")
public class CityDTO implements Serializable {

    /** City name */
    @Schema(description = "City name", example = "Shenzhen", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "City name must not be blank")
    @Size(max = 50, message = "City name must not exceed 50 characters")
    private String cityName;
}
