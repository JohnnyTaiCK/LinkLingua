package com.linklingua.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Unified response wrapper.
 *
 * <p>All endpoints return this structure so the frontend can handle responses consistently.</p>
 *
 * @param <T> the business data type
 * @author LinkLingua
 */
@Data
@Schema(description = "Unified response result")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Status code */
    @Schema(description = "Status code", example = "200")
    private Integer code;

    /** Message */
    @Schema(description = "Message", example = "Operation succeeded")
    private String message;

    /** Business data */
    @Schema(description = "Business data")
    private T data;

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Success response without data.
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * Success response with data.
     *
     * @param data the business data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * Success response with a custom message and data.
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * Failure response with the default generic failure code.
     */
    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * Failure response with a custom message.
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * Failure response built from a status code enum.
     */
    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * Failure response with a custom status code and message.
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
