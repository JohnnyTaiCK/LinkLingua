package com.linklingua.context;

/**
 * Thread-local holder for the currently authenticated user id.
 *
 * <p>Populated by the JWT interceptor for each request and cleared after the request completes,
 * so downstream code (services, audit fields, etc.) can access the caller's identity.</p>
 *
 * @author LinkLingua
 */
public final class BaseContext {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    private BaseContext() {
        // Utility holder, must not be instantiated
    }

    /**
     * Stores the current user id for this request thread.
     */
    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    /**
     * Returns the current user id, or {@code null} if not authenticated.
     */
    public static Long getCurrentId() {
        return THREAD_LOCAL.get();
    }

    /**
     * Removes the stored user id; must be called when the request finishes to avoid leaks.
     */
    public static void removeCurrentId() {
        THREAD_LOCAL.remove();
    }
}
