package com.linklingua.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI (Swagger) configuration.
 *
 * <p>Once configured, the following endpoints are available:
 * <ul>
 *   <li>Swagger UI: http://localhost:8080/swagger-ui.html</li>
 *   <li>API JSON : http://localhost:8080/v3/api-docs</li>
 * </ul>
 * </p>
 *
 * @author LinkLingua
 */
@Configuration
public class OpenApiConfig {

    /**
     * Builds the OpenAPI document metadata.
     */
    @Bean
    public OpenAPI linkLinguaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LinkLingua API Documentation")
                        .description("LinkLingua backend service API documentation")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("LinkLingua Team")
                                .email("dev@linklingua.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
