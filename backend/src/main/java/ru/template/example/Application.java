package ru.template.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.template.example.*"
})
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
