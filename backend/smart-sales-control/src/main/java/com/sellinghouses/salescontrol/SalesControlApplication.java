package com.sellinghouses.salescontrol;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.sellinghouses.salescontrol.module.*.mapper")
@SpringBootApplication
public class SalesControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesControlApplication.class, args);
    }
}
