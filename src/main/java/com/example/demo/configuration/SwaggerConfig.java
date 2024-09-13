package com.example.demo.configuration;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value(("${open.api.title}"))
    private String title;

    @Value(("${open.api.version}"))
    private String version;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description("Sample description")
                        .license(new License().name("Apache"))
                );
    }

    @Bean
    public GroupedOpenApi allApi(){
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi(){
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi studentApi(){
        return GroupedOpenApi.builder()
                .group("student")
                .pathsToMatch("/api/student/**")
                .build();
    }
}
