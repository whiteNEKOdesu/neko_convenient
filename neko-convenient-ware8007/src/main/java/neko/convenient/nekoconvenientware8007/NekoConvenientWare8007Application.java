package neko.convenient.nekoconvenientware8007;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NekoConvenientWare8007Application {

    public static void main(String[] args) {
        SpringApplication.run(NekoConvenientWare8007Application.class, args);
    }

}
