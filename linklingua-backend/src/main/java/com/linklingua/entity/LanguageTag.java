package com.linklingua.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Language tag dictionary entity, mapped to the database table {@code language_tag}.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Language tag dictionary")
public class LanguageTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @Schema(description = "Primary key ID", example = "1")
    private Integer id;

    /** Language name */
    @Schema(description = "Language name", example = "English")
    private String langName;
}
