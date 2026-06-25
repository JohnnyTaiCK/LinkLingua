package com.linklingua.common;

import lombok.Getter;

/**
 * Enumeration of unified response status codes.
 *
 * <p>Centralizes business status codes and their messages to avoid scattered magic values.</p>
 *
 * @author LinkLingua
 */
@Getter
public enum ResultCode {

    /** Operation succeeded */
    SUCCESS(200, "Operation succeeded"),

    /** Operation failed (generic) */
    FAILED(500, "Operation failed"),

    /** Parameter validation failed */
    VALIDATE_FAILED(400, "Parameter validation failed"),

    /** Unauthorized / not logged in */
    UNAUTHORIZED(401, "Not logged in or the session has expired"),

    /** No permission to access */
    FORBIDDEN(403, "No permission to access this resource"),

    /** Resource not found */
    NOT_FOUND(404, "The requested resource does not exist"),

    /** Business error */
    BUSINESS_ERROR(600, "Business processing error");

    /** Status code */
    private final Integer code;

    /** Message */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
