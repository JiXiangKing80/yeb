package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试
 * @author: 吉祥
 * @created: 2021/10/22 15:24
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello(){
        return "hello,world";
    }

    @GetMapping("/personnel/emp/hello1")
    public String hello1(){return "/personnel/emp/**";}

    @GetMapping("/employee/basic/hello2")
    public String hello2(){return "/employee/basic/**";}

}

