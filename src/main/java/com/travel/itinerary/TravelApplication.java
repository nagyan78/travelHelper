package com.travel.itinerary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 旅游攻略系统启动类
 * 
 * @author Travel Team
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.travel.itinerary.module.*.mapper")
public class TravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
        System.out.println("========================================");
        System.out.println("   旅游攻略系统启动成功!");
        System.out.println("   Swagger文档: http://localhost:8080/doc.html");
        System.out.println("========================================");
    }
}
