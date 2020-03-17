package com.indoori.onlinetest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("question")
    public String question() {
        return "question";
    }

    @GetMapping("confirm")
    public String confirm() {
        return "confirm";
    }

    @GetMapping("result")
    public String result() {
        return "result";
    }
}