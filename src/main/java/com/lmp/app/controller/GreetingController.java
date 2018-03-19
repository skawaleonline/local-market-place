package com.lmp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        return "greeting";
    }

}