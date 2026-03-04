package com.diegogranados.coworkings.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Coworking Access Control API")
                        .version("1.0.0")
                        .description(
                                """
                                        API REST para el control de ingreso y salida de personas en múltiples sedes de coworking.

                                        **Roles disponibles:**
                                        - `ADMIN`: gestión de sedes, operadores e indicadores globales.
                                        - `OPERADOR`: registro de ingresos/salidas e indicadores de sus sedes.

                                        **Credenciales de prueba (ADMIN):**
                                        - Email: `admin@mail.com`
                                        - Password: `admin`

                                        **Autenticación:** Usa el endpoint `/api/auth/login` para obtener el token JWT,
                                        luego haz clic en el botón **Authorize** e ingresa: `Bearer <tu_token>`.
                                        """))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido en /api/auth/login")));
    }
}
