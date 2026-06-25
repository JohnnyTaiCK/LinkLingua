package com.linklingua.config;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis configuration.
 *
 * <p>Scanning of Mapper interfaces is handled by {@code @MapperScan("com.linklingua.mapper")}
 * on the application entry point. Here we use a {@link ConfigurationCustomizer} to augment
 * MyBatis core settings programmatically, working together with the settings in
 * {@code application.properties}.</p>
 *
 * @author LinkLingua
 */
@Configuration
public class MyBatisConfig {

    /**
     * Customizes the global MyBatis configuration.
     *
     * <ul>
     *   <li>Enables automatic underscore-to-camelCase mapping</li>
     *   <li>Enables full automatic mapping behavior</li>
     *   <li>Invokes setters even when a column value is null</li>
     * </ul>
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // Map underscore-separated DB columns to camelCase Java properties
            configuration.setMapUnderscoreToCamelCase(true);
            // Auto-mapping behavior: FULL maps all columns in the result set
            configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
            // Call setters on null values (adjust as needed)
            configuration.setCallSettersOnNulls(true);
        };
    }
}
