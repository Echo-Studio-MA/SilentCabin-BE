package io.echo.silentcabin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SilentCabinApplication {

    static void main(String[] args) {
        SpringApplication.run(SilentCabinApplication.class, args);
    }

}
