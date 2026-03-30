package no.nav.oebs.okonomimodell.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kontoplanOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kontoplan API")
                        .description("API for fetching segments in the OeBS accounting model")
                        .version("v1"));
    }
}
