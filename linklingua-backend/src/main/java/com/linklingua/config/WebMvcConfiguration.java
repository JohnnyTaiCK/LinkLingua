package com.linklingua.config;

import com.linklingua.interceptor.JwtTokenInterceptor;
import com.linklingua.json.JacksonObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC configuration.
 *
 * <p>Implements {@link WebMvcConfigurer} (instead of extending {@code WebMvcConfigurationSupport})
 * so Spring Boot's MVC auto-configuration, including the Swagger UI resource handlers, keeps
 * working. It registers a custom Jackson converter for date/time formatting and exposes the
 * local upload directory as static resources.</p>
 *
 * @author LinkLingua
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    /** Local directory where uploaded files are stored. */
    @Value("${linklingua.upload.dir}")
    private String uploadDir;

    /** Public URL prefix mapped to the upload directory. */
    @Value("${linklingua.upload.url-prefix}")
    private String uploadUrlPrefix;

    /**
     * Registers the JWT interceptor on the event endpoints. The interceptor itself only enforces
     * authentication for write operations (publish / update / delete), letting reads pass through.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering JWT authentication interceptor for event write operations...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/events", "/api/events/**");
    }

    /**
     * Registers a Jackson message converter using the project date/time formats and gives it the
     * highest priority.
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Registering custom Jackson message converter...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }

    /**
     * Exposes uploaded files as static resources, e.g. {@code /uploads/**}.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pattern = uploadUrlPrefix.endsWith("/") ? uploadUrlPrefix + "**" : uploadUrlPrefix + "/**";
        registry.addResourceHandler(pattern)
                .addResourceLocations("file:" + uploadDir + "/");
    }

    // Cross-origin (CORS) configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // allow the frontend dev server
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
