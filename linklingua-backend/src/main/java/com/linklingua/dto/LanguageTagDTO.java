package com.linklingua.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * Request DTO for creating or updating a language tag.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Language tag create/update request")
public class LanguageTagDTO implements Serializable {

    /** Language name */
    @Schema(description = "Language name", example = "English", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Language name must not be blank")
    @Size(max = 50, message = "Language name must not exceed 50 characters")
    private String langName;
}
