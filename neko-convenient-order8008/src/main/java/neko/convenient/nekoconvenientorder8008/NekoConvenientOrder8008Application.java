package neko.convenient.nekoconvenientorder8008;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NekoConvenientOrder8008Application {

    public static void main(String[] args) {
        SpringApplication.run(NekoConvenientOrder8008Application.class, args);
    }

}
