package com.bank.controller.web;

import com.bank.util.annotation.AutoLogging;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// using thymeleaf as html template render instead of JSP

@Controller
public class OperationController {
    @AutoLogging
    @GetMapping(value = {"/web/ops"})
    public String get(@RequestParam String site, Model model) {
        var userDetails = (UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("name", userDetails.getUsername());
        model.addAttribute("site", site);
        return "ops"; // refer to resources/templates/ops.html
    }
}
