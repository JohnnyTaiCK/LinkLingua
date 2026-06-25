package com.linklingua.interceptor;

import com.linklingua.common.BusinessException;
import com.linklingua.common.ResultCode;
import com.linklingua.context.BaseContext;
import com.linklingua.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor that enforces a valid, non-expired JWT on protected endpoints.
 *
 * <p>Read-only requests ({@code GET}) and CORS preflight ({@code OPTIONS}) are allowed through;
 * write requests (e.g. publishing, updating or deleting an event) require a valid token.
 * On failure it throws {@link BusinessException}, which the global handler converts into a
 * standard {@code Result} with the {@code 401} code.</p>
 *
 * @author LinkLingua
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    /** Name of the request header that carries the token. */
    @Value("${linklingua.jwt.token-header}")
    private String tokenHeader;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Only guard actual controller methods (skip static resources, etc.).
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // Reads and CORS preflight do not require authentication.
        String method = request.getMethod();
        if (HttpMethod.GET.matches(method) || HttpMethod.OPTIONS.matches(method)) {
            return true;
        }

        String token = resolveToken(request);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Missing login token, please log in first");
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            Object userId = claims.get("userId");
            BaseContext.setCurrentId(userId == null ? null : Long.valueOf(userId.toString()));
            log.debug("JWT verified for request {} {}, userId={}", method, request.getRequestURI(), userId);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Login token has expired, please log in again");
        } catch (Exception e) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Invalid login token");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Always clear the thread-local to avoid leaking identity across pooled threads.
        BaseContext.removeCurrentId();
    }

    /**
     * Extracts the token from the configured header, stripping a leading {@code Bearer } if present.
     */
    private String resolveToken(HttpServletRequest request) {
        String value = request.getHeader(tokenHeader);
        if (StringUtils.hasText(value) && value.startsWith(BEARER_PREFIX)) {
            return value.substring(BEARER_PREFIX.length());
        }
        return value;
    }
}
