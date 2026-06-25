package com.linklingua.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Platform system user entity, mapped to the database table {@code user}.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "Platform system user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** User unique id */
    @Schema(description = "User unique id", example = "1")
    private Integer id;

    /** Login account */
    @Schema(description = "Login account", example = "alice")
    private String username;

    /**
     * Login password.
     *
     * <p>Write-only: it is accepted in requests but never serialized back in responses.</p>
     */
    @Schema(description = "Login password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    /** Register time */
    @Schema(description = "Register time")
    private LocalDateTime createTime;
}
