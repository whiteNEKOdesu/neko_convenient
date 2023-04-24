package neko.convenient.nekoconvenientproduct8005;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@EnableFeignClients
public class NekoConvenientProduct8005Application {

    public static void main(String[] args) {
        SpringApplication.run(NekoConvenientProduct8005Application.class, args);
    }

}
