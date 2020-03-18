package com.indoori.onlinetest.Controller;

import com.indoori.onlinetest.Bean.QuestionRecord;
import com.indoori.onlinetest.Bean.TestRecord;
import com.indoori.onlinetest.Repository.QuestionRepo;
import com.indoori.onlinetest.Repository.TestRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    @Value("${test.total.questions}")
    private int totalQuestions;
    @Value("${test.total.time}")
    private int totalTime;
    private static final Integer firstQuestionIndex = 0;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private TestRepo testRepo;

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @PostMapping("begintest")
    public String beginTest(Model model, String userName) {
        //Create a new test record
        TestRecord newTestRecord = new TestRecord();
        //Set the userName
        newTestRecord.setUserName(userName);
        //Get list of all questions from database:
        List<QuestionRecord> questionRecordList = questionRepo.findAll();
        //Shuffle the list to pick 10 questions:
        Collections.shuffle(questionRecordList);
        //Add question Ids to TestRecord:
        List<Long> questionIdList = new ArrayList<>();
        for(int i = 0; i < totalQuestions; i++)
            questionIdList.add(questionRecordList.get(i).getQuestionId());
        //Set the list of QuestionIds
        newTestRecord.setQuestionIdList(questionIdList);
        //Initialize a selectedOptionList to NULL;
        List<Integer> selectedOptionList = new ArrayList<>();
        for(int i = 0; i < totalQuestions; i++)
            selectedOptionList.add(null);
        //Set the optionList
        newTestRecord.setSelectedOptionList(selectedOptionList);
        //SAVE TO DATABASE
        TestRecord testRecord = testRepo.save(newTestRecord);
        log.info("Database Record: " + testRecord.toString());
        //Get the first question record
        QuestionRecord firstQuestionRecord = questionRepo.getOne(testRecord.getQuestionIdList().get(firstQuestionIndex));
        //Add attributes to HTML
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("remainingTime", totalTime);
        model.addAttribute("testId", testRecord.getTestId());
        model.addAttribute("questionIndex", 0);
        model.addAttribute("question", firstQuestionRecord.getQuestion());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("option1", firstQuestionRecord.getOption1());
        model.addAttribute("option2", firstQuestionRecord.getOption2());
        model.addAttribute("option3", firstQuestionRecord.getOption3());
        model.addAttribute("option4", firstQuestionRecord.getOption4());
        return "question";
    }

    @GetMapping("question")
    public String question() {
        //todo: Remove question - GET method
        return "question";
    }

    @PostMapping("question")
    public String question(Model model, String name, int age) {
        //todo: properly implement question method
        log.info("Name: " + name + ", age: " + age);
        return "question";
    }

    @GetMapping("confirm")
    public String confirm() {
        //todo: convert confirm to a post method
        return "confirm";
    }

    @GetMapping("result")
    public String result(Model model) {
        //todo: properly implement result method
        model.addAttribute("userName", "Aditya");
        model.addAttribute("timeTaken", 188);
        model.addAttribute("correctAnswers", 5);
        model.addAttribute("totalQuestions", 10);
        model.addAttribute("percentage", 50);
        return "result";
    }
}