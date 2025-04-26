package com.example.mybank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sayHello")
public class HelloWorldController {

    @RequestMapping("/")
    public ModelAndView helloworld() {
        Map<String, String> modelData = new HashMap<>();
        modelData.put("message", "Hello World!");
        return new ModelAndView("helloworld", modelData);
    }
}
