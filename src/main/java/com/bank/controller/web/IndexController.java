package com.bank.controller.web;

import com.bank.util.annotation.AutoLogging;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// using thymeleaf as html template render instead of JSP

@Controller
public class IndexController {
    @AutoLogging
    @GetMapping(value = {"/", "/index.html", "index.htm"})
    public String get() {
        return "index"; // refer to resources/templates/index.html
    }
}
