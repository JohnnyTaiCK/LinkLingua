package com.linklingua.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * Request DTO for creating or updating a system user.
 *
 * @author LinkLingua
 */
@Data
@Schema(description = "User create/update request")
public class UserDTO implements Serializable {

    /** Login account */
    @Schema(description = "Login account", example = "alice", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
    private String username;

    /** Login password */
    @Schema(description = "Login password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password length must be between 6 and 100 characters")
    private String password;
}
