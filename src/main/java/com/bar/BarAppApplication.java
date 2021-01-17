package com.bar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bar.product.request"})
public class BarAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarAppApplication.class, args);
    }

}
