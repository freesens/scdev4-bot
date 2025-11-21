package kr.co.pionnet.scdev4.bot.api.v1.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.api-docs.version}") String appVersion) {
        Info info = new Info()
                .title("PIONNET SCDev4 - BOT API")
                .version(appVersion)
                .description("잡다한 알림 전송을 담당합니다.")
                .termsOfService("http://swagger.io/terms/")
                .license(new License()
                            .name("Apache License Version 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0")
                );

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
