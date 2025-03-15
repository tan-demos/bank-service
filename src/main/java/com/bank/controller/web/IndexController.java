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
    @GetMapping(value = {"/", "", "/home"})
    public String get(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
}
