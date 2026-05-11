package br.com.wanderlei.keycloakestudo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LaboratorioKeycloakApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaboratorioKeycloakApplication.class, args);
    }
}
