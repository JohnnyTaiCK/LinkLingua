package com.linklingua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * LinkLingua backend service entry point.
 *
 * <p>{@code @SpringBootApplication} enables auto-configuration and component scanning;
 * {@code @MapperScan} declares the package of MyBatis Mapper interfaces, so there is no
 * need to annotate each interface with {@code @Mapper}.</p>
 *
 * @author LinkLingua
 */
@SpringBootApplication
@MapperScan("com.linklingua.mapper")
public class LinkLinguaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkLinguaApplication.class, args);
    }
}
