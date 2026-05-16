package com.example.compuservicessoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class CompuServicesSoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompuServicesSoftApplication.class, args);
    }

    // Configuración para el entorno de desarrollo
    @Configuration
    @Profile("dev")
    public static class DevCorsConfiguration {
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://127.0.0.1:5501", "http://localhost:5501")
                            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            };
        }
    }


    // Configuración para el entorno de producción
    @Configuration
    @Profile("prod") // Solo se activa en el perfil "prod"
    public static class ProdCorsConfiguration {
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("https://compuservicessoft.com", "https://css-fronted-production.up.railway.app") // Permitir solo tu dominio en producción
                            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            };
        }
    }
}

