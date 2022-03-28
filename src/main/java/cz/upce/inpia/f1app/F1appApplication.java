package cz.upce.inpia.f1app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "cz.upce.inpia.f1app" })
//@EnableJpaRepositories(entityManagerFactoryRef="factoryBean")
public class F1appApplication {

    public static void main(String[] args) {
        SpringApplication.run(F1appApplication.class, args);
    }

}
