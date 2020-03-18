package com.indoori.onlinetest.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class MainController {
    @Value("${test.total.questions}")
    private int totalQuestions;
    @Value("${test.total.time}")
    private int totalTime;

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @PostMapping("begintest")
    public String beginTest(Model model,String userName) {
        //todo: randomize questions
        //todo: save to database
        model.addAttribute("userName",userName);
        model.addAttribute("remainingTime",totalTime);
        model.addAttribute("testId","A121");
        model.addAttribute("questionIndex",totalQuestions);
        model.addAttribute("question","What is the capital of India?");
        model.addAttribute("totalQuestions",10);
        model.addAttribute("option1","Delhi");
        model.addAttribute("option2","Mumbai");
        model.addAttribute("option3","Telangana");
        model.addAttribute("option4","Punjab");
        return "question";
    }

    @GetMapping("question")
    public String question() {
        return "question";
    }

    @PostMapping("question")
    public String question(Model model, String name, int age) {
        log.info("Name: "+name+", age: "+ age);
        return "question";
    }

    @GetMapping("confirm")
    public String confirm() {
        return "confirm";
    }

    @GetMapping("result")
    public String result(Model model) {
        model.addAttribute("userName","Aditya");
        model.addAttribute("timeTaken",188);
        model.addAttribute("correctAnswers",5);
        model.addAttribute("totalQuestions",10);
        model.addAttribute("percentage",50);
        return "result";
    }
}