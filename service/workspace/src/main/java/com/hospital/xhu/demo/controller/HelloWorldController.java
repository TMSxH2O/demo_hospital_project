package com.hospital.xhu.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@RestController
public class HelloWorldController {

    @RequestMapping("hello_world")
    public String testHelloWorld() {
        return "Hello World";
    }
}
