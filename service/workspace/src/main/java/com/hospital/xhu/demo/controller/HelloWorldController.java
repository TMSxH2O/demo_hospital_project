package com.hospital.xhu.demo.controller;

import com.hospital.xhu.demo.utils.annotation.PassToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/2
 */
@RestController
@Slf4j
public class HelloWorldController {
    @PassToken
    @RequestMapping("hello_world")
    public String helloWorld() {
        return "Hello World";
    }
}
