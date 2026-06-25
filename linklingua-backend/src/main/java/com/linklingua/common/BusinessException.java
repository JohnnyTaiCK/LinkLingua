package com.linklingua.common;

import lombok.Getter;

/**
 * Custom business exception.
 *
 * <p>Thrown deliberately when an expected error occurs in business logic, and handled
 * uniformly by {@link GlobalExceptionHandler}.</p>
 *
 * @author LinkLingua
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Business status code */
    private final Integer code;

    /**
     * Constructs the exception with a custom message (using the default business error code).
     *
     * @param message the error message
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    /**
     * Constructs the exception from a status code enum.
     *
     * @param resultCode the status code enum
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * Constructs the exception with a custom status code and message.
     *
     * @param code    the business status code
     * @param message the error message
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
