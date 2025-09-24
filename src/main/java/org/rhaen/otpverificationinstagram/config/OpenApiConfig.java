package org.rhaen.otpverificationinstagram.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Xavfsizlik sxemasining nomi

        return new OpenAPI()
                // 1. API haqida umumiy ma'lumot
                .info(new Info()
                        .title("Instagram OTP Verification API")
                        .version("1.0.0")
                        .description("Bu API foydalanuvchilarni email va Instagram direct message orqali tasdiqlash uchun mo'ljallangan.")
                        .contact(new Contact().name("Muhammadjon Saidov").email("javascript0222@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                // 2. Global xavfsizlik talabini qo'shish (hamma endpoint'lar uchun)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // 3. JWT (Bearer Token) uchun xavfsizlik sxemasini aniqlash
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}