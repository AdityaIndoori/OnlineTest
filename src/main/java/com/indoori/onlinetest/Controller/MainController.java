package com.indoori.onlinetest.Controller;

import com.indoori.onlinetest.Bean.NavigateTo;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
//TODO: Handle refersh, back and forward browser navigation
//TODO: Put all the logic into services and call them in the controllers
//TODO: Add answers to the result page
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
            selectedOptionList.add(-1);
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
        model.addAttribute("questionIndex", firstQuestionIndex);
        model.addAttribute("question", firstQuestionRecord.getQuestion());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("option1", firstQuestionRecord.getOption1());
        model.addAttribute("option2", firstQuestionRecord.getOption2());
        model.addAttribute("option3", firstQuestionRecord.getOption3());
        model.addAttribute("option4", firstQuestionRecord.getOption4());
        model.addAttribute("existingSelectedOption", null);
        return "question";
    }

    @GetMapping("question")
    public String question(Model model) {
        //todo: Remove question - GET method
        //Add attributes to HTML
        model.addAttribute("userName", "TestUser");
        model.addAttribute("remainingTime", totalTime);
        model.addAttribute("testId", 123);
        model.addAttribute("questionIndex", 5);
        model.addAttribute("question", "Test Question");
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("option1", "TestOption1");
        model.addAttribute("option2", "TestOption2");
        model.addAttribute("option3", "TestOption3");
        model.addAttribute("option4", "TestOption4");
        return "question";
    }

    @PostMapping("question")
    public String question(Model model, Integer currentQuestionIndex, Integer selectedOption, Long testId, Integer remainingTime, NavigateTo navigateTo) {
        Integer newQuestionIndex = null;
        TestRecord testRecord = null;
        QuestionRecord newQuestionRecord = null;
        Integer existingSelectedOption = null;
        if (selectedOption==null)
            selectedOption = -1;

        //START: IF A USER WANTS TO NAVIGATE TO A CERTAIN QUESTION FROM CONFIRM.HTML:
        if (navigateTo.equals(NavigateTo.CUST)){
            //Set the value of the new question's index
            newQuestionIndex = currentQuestionIndex;
            //Get the latest testRecord
            testRecord = testRepo.getOne(testId);
            //Get the required question record
            newQuestionRecord = questionRepo.getOne(testRecord.getQuestionIdList().get(newQuestionIndex));
            //Get the option that was selected by the user previously:
            existingSelectedOption = testRecord.getSelectedOptionList().get(newQuestionIndex);
        }
        //END
        else {
            //START: UPDATE DATABASE WITH SELECTED OPTION
            //Get the testRecord from db:
            testRecord = testRepo.getOne(testId);
            //Get the testRecord's selectedOptionList
            List<Integer> selectedOptionsList = testRecord.getSelectedOptionList();
            //Set the selected option
            selectedOptionsList.set(currentQuestionIndex, selectedOption);
            testRecord.setSelectedOptionList(selectedOptionsList);
            //Save to db:
            testRepo.save(testRecord);
            //END

            //START: IF USER CLICKED NEXT
            //if we want to goto a valid next question:
            if (navigateTo.equals(NavigateTo.NEXT) && currentQuestionIndex < totalQuestions - 1) {
                //Set the value of the new question's index
                newQuestionIndex = currentQuestionIndex + 1;
                //Get the latest testRecord
                testRecord = testRepo.getOne(testId);
                //Get the required question record
                newQuestionRecord = questionRepo.getOne(testRecord.getQuestionIdList().get(newQuestionIndex));
                //Get the option that was selected by the user previously:
                existingSelectedOption = testRecord.getSelectedOptionList().get(newQuestionIndex);
            }
            //END

            //START: IF USER CLICKED PREV
            //if we want to goto a valid next question:
            if (navigateTo.equals(NavigateTo.PREV) && currentQuestionIndex > firstQuestionIndex) {
                //Set the value of the new question's index
                newQuestionIndex = currentQuestionIndex - 1;
                //Get the latest testRecord
                testRecord = testRepo.getOne(testId);
                //Get the required question record
                newQuestionRecord = questionRepo.getOne(testRecord.getQuestionIdList().get(newQuestionIndex));
                //Get the option that was selected by the user previously:
                existingSelectedOption = testRecord.getSelectedOptionList().get(newQuestionIndex);
            }
            //END

            //START: IF USER CLICKED NEXT at last question or PREV at first question: GOTO Confirmation Page
            if ((navigateTo.equals(NavigateTo.NEXT) && currentQuestionIndex == totalQuestions - 1) || (navigateTo.equals(NavigateTo.PREV) && currentQuestionIndex == firstQuestionIndex)) {
                model.addAttribute("userName", testRecord.getUserName());
                model.addAttribute("remainingTime", remainingTime);
                model.addAttribute("testId", testRecord.getTestId());
                return "confirm";
            }
            //END
        }
        //todo: properly implement question method
        log.info("currentQuestionIndex: "+currentQuestionIndex + " selectedOption: "+selectedOption + " testId: "+testId + "remainingTime: "+remainingTime + "navigateTo: "+navigateTo);
        //Add attributes to HTML
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("remainingTime", remainingTime);
        model.addAttribute("testId", testRecord.getTestId());
        model.addAttribute("questionIndex", newQuestionIndex);
        model.addAttribute("question", newQuestionRecord.getQuestion());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("option1", newQuestionRecord.getOption1());
        model.addAttribute("option2", newQuestionRecord.getOption2());
        model.addAttribute("option3", newQuestionRecord.getOption3());
        model.addAttribute("option4", newQuestionRecord.getOption4());
        model.addAttribute("existingSelectedOption", existingSelectedOption);

        return "question";
    }

    @PostMapping("confirm")
    public String confirm(Model model, String userName, Long testId, Integer remainingTime) {
        //todo: convert confirm to a post method
        model.addAttribute("userName", userName);
        model.addAttribute("remainingTime", remainingTime);
        model.addAttribute("testId", testId);
        return "confirm";
    }

    @PostMapping("result")
    public String result(Model model, Long testId, Integer remainingTime) {
        //todo: properly implement result method
        TestRecord testRecord = testRepo.getOne(testId);
        Integer timeTaken = totalTime - remainingTime;
        testRecord.setTimeTaken(timeTaken);
        Integer correctAnswers = 0;
        List<Long> questionIdList = testRecord.getQuestionIdList();
        List<Integer> selectedOptionList = testRecord.getSelectedOptionList();
        for (int i = 0; i < totalQuestions; i++){
            if(questionRepo.getOne(questionIdList.get(i)).getCorrect().equals(selectedOptionList.get(i)))
                correctAnswers++;
        }
        testRecord.setCorrectQuestions(correctAnswers);
        DecimalFormat df = new DecimalFormat("0.00");
        Double percentage = Double.valueOf(df.format(((double)correctAnswers/(double)totalQuestions)*100));
        testRecord.setPercentage(percentage);
        testRecord = testRepo.save(testRecord);
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("timeTaken", testRecord.getTimeTaken());
        model.addAttribute("correctAnswers", testRecord.getCorrectQuestions());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("percentage", testRecord.getPercentage());
        return "result";
    }
}