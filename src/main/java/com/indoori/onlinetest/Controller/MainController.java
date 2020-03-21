package com.indoori.onlinetest.Controller;

import com.indoori.onlinetest.Bean.NavigateTo;
import com.indoori.onlinetest.Bean.QuestionRecord;
import com.indoori.onlinetest.Bean.TestBean;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@Slf4j
//TODO: Handle refersh, back and forward browser navigation
//TODO: Put all the logic into services and call them in the controllers
//TODO: Add answers to the result page
//TODO: Add logic when time runs out
public class MainController {
    @Value("${spring.application.name}")
    private String ApplicationName;
    @Value("${test.total.questions}")
    private int totalQuestions;
    @Value("${test.total.time}")
    private int totalTime;
    private static final Integer firstQuestionIndex = 0;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private TestRepo testRepo;
    @Resource(name = "TestBean")
    private TestBean testBean;
    @Resource(name = "RemainingTimeMapBean")
    private Map<Long,Integer> remainingTimeMapBean;
    @Resource(name = "TimerMapBean")
    private Map<Long, Timer> timerMapBean;

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("ApplicationName", ApplicationName);
        return "welcome";
    }

    @PostMapping("begintest")
    public String beginTest(RedirectAttributes model, String userName) {
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
        //Put the new test data into the Session Bean: testBean
        testBean.setTestId(testRecord.getTestId());
        testBean.setCurrentQuestionIndex(firstQuestionIndex);
        //Put the currentRemainingTime into the Application Scoped bean: remainingTimeMapBean
        remainingTimeMapBean.put(testRecord.getTestId(), totalTime);
        beginCountDown(testRecord.getTestId());
        return "redirect:question";
    }

    @GetMapping("question")
    public String question(Model model) {
        //If time runs out, always show the results page:
        if(remainingTimeMapBean.get(testBean.getTestId())<0)
            return "redirect:result";
        //Get testRecord:
        TestRecord testRecord = testRepo.getOne(testBean.getTestId());
        //Pause the countDown timer
        pauseCountDown(testRecord.getTestId());
        //Get the currentQuestion ID:
        Long currentQuestionId = testRecord.getQuestionIdList().get(testBean.getCurrentQuestionIndex());
        //Get currentQuestion Record:
        QuestionRecord newQuestionRecord = questionRepo.getOne(currentQuestionId);
        //Get existingSelectedOption:
        Integer existingSelectedOption = testRecord.getSelectedOptionList().get(testBean.getCurrentQuestionIndex());
        //Add attributes to HTML
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("remainingTime", remainingTimeMapBean.get(testRecord.getTestId()));
        model.addAttribute("testId", testRecord.getTestId());
        model.addAttribute("questionIndex", testBean.getCurrentQuestionIndex());
        model.addAttribute("question", newQuestionRecord.getQuestion());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("option1", newQuestionRecord.getOption1());
        model.addAttribute("option2", newQuestionRecord.getOption2());
        model.addAttribute("option3", newQuestionRecord.getOption3());
        model.addAttribute("option4", newQuestionRecord.getOption4());
        model.addAttribute("existingSelectedOption", existingSelectedOption);
        beginCountDown(testRecord.getTestId());
        return "question";
    }

    @PostMapping("question")
    public String question(Model model, Integer currentQuestionIndex, Integer selectedOption, Long testId, Integer remainingTime, NavigateTo navigateTo) {
        //Pause the countDown timer
        pauseCountDown(testId);
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
                return "redirect:confirm";
            }
            //END
        }
        //todo: Remove unnecessary stuff from this method such as the model attributes, and unnecessary logic
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
        //Set the session testBean's new question to the new question
        testBean.setCurrentQuestionIndex(newQuestionIndex);
        //Begin the countdown timer for the user
        beginCountDown(testRecord.getTestId());
        return "redirect:question";
    }

    @GetMapping("confirm")
    public String confirm(Model model) {
        //Get testRecord:
        TestRecord testRecord = testRepo.getOne(testBean.getTestId());
        //Add HTML Model Attributes
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("remainingTime", remainingTimeMapBean.get(testRecord.getTestId()));
        model.addAttribute("testId", testRecord.getTestId());
        model.addAttribute("totalQuestions", totalQuestions);
        return "confirm";
    }

    @PostMapping("confirm")
    public String confirm(Model model, String userName, Long testId, Integer remainingTime) {
        //todo: convert confirm to a post method
        /*model.addAttribute("userName", userName);
        model.addAttribute("remainingTime", remainingTime);
        model.addAttribute("testId", testId);*/
        return "redirect:confirm";
    }

    @GetMapping("result")
    public String result(Model model){
        //Stop the countDown for the current test:
        pauseCountDown(testBean.getTestId());
        //Get the testRecord:
        TestRecord testRecord = testRepo.getOne(testBean.getTestId());
        //Get the totalTimeTaken:
        Integer timeTaken = totalTime - remainingTimeMapBean.get(testRecord.getTestId()) -1;
        //Set the testRecords totalTimeTaken:
        testRecord.setTimeTaken(timeTaken);
        //Initalize the number of correctly answered questions
        Integer correctQuestions = 0;
        //Get list of question IDs for the current test:
        List<Long> questionIdList = testRecord.getQuestionIdList();
        //Get list of selected options:
        List<Integer> selectedOptionList = testRecord.getSelectedOptionList();
        //Create a List of Wrongly Answered Questions:
        List<String> wrongQuestionList = new ArrayList<>();
        //Create a List of Selected Answers for the Wrongly Answered Questions:
        List<String> selectedAnswerList = new ArrayList<>();
        //Create a List of Correct Answers for the Wrongly Answred Questions:
        List<String> correctAnswerList = new ArrayList<>();
        //Create a questionRecord for checking the correct answer:
        QuestionRecord questionRecord = null;
        //Increase the correctly answered questions:
        for (int i = 0; i < totalQuestions; i++){
            //Get the questionRecord:
            questionRecord = questionRepo.getOne(questionIdList.get(i));
            //If correctly answered, increase the correctQuestions count:
            if(questionRecord.getCorrect().equals(selectedOptionList.get(i)))
                correctQuestions++;
            else{
                String wrongQuestion = questionRecord.getQuestion();
                Integer correctOptionIndex = questionRecord.getCorrect();
                Integer selectedOptionIndex = selectedOptionList.get(i);
                String correctAnswer = null;
                String selectedAnswer = null;
                switch (correctOptionIndex){
                    case 0: correctAnswer = questionRecord.getOption1();break;
                    case 1: correctAnswer = questionRecord.getOption2();break;
                    case 2: correctAnswer = questionRecord.getOption3();break;
                    case 3: correctAnswer = questionRecord.getOption4();break;
                    default: correctAnswer = null;break;
                }
                switch (selectedOptionIndex){
                    case 0: selectedAnswer = questionRecord.getOption1();break;
                    case 1: selectedAnswer = questionRecord.getOption2();break;
                    case 2: selectedAnswer = questionRecord.getOption3();break;
                    case 3: selectedAnswer = questionRecord.getOption4();break;
                    default: selectedAnswer = null;break;
                }
                wrongQuestionList.add(wrongQuestion);
                selectedAnswerList.add(selectedAnswer);
                correctAnswerList.add(correctAnswer);
            }
        }
        //Set the test record's correctly answered question to the one just calculated:
        testRecord.setCorrectQuestions(correctQuestions);
        //Calculate the percentage:
        DecimalFormat df = new DecimalFormat("0.00");
        Double percentage = Double.valueOf(df.format(((double)correctQuestions/(double)totalQuestions)*100));
        //Set the percentage for the test record:
        testRecord.setPercentage(percentage);
        //Save the test record to the db:
        testRecord = testRepo.save(testRecord);
        //Add HTMl model attributes:
        model.addAttribute("userName", testRecord.getUserName());
        model.addAttribute("timeTaken", testRecord.getTimeTaken());
        model.addAttribute("correctAnswers", testRecord.getCorrectQuestions());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("percentage", testRecord.getPercentage());
        model.addAttribute("wrongQuestionList", wrongQuestionList);
        model.addAttribute("selectedAnswerList", selectedAnswerList);
        model.addAttribute("correctAnswerList", correctAnswerList);
        return "result";
    }

    @PostMapping("result")
    public String result() {
        return "redirect:result";
    }

    private void decreaseRemainingTime(Long testId) {
        remainingTimeMapBean.put(testId, remainingTimeMapBean.get(testId)-1);
    }

    public void beginCountDown(Long testId){
        timerMapBean.put(testId, new Timer());
        Timer timer = timerMapBean.get(testId);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                decreaseRemainingTime(testId);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void pauseCountDown(Long testId){
        Timer timer = timerMapBean.get(testId);
        timer.cancel();
    }
}