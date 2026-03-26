package no.nav.oebs.kontoplan_api.config;

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
                        .description("API for henting av segmenter i økonomimodellen fra OeBS")
                        .version("v1"));
    }
}
