package com.hospital.xhu.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@SpringBootApplication
@MapperScan("com.hospital.xhu.demo.dao")
public class ProjectMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMainApplication.class, args);
    }
}
