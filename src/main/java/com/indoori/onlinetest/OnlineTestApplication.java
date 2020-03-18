package com.indoori.onlinetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.indoori.onlinetest.Repository")
@EntityScan("com.indoori.onlinetest.Bean")
@SpringBootApplication
public class OnlineTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTestApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner demoQuestions(QuestionRepo repo) {
		return args -> {
			//'What is the capital of India?',('Telangana','Delhi','Punjab','Gujarat'),1
			QuestionRecord questionRecord;
			//Q1:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q2: 'Who is the father of India?',('Nehru','Gandhi','Bhagat Singh','Ambedkar'),1
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q3:'Where is Hyderabad located in?',('Telangana','Delhi','Punjab','Gujarat'),0
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q4:Grand Central Terminal, Park Avenue, New York is the world\'s',('largest railway station','	highest railway station','longest railway station','	None of the above'),0
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q5:'Entomology is the science that studies',('Behavior of human beings','Insects','The origin and history of technical and scientific terms','The formation of rocks'),1);
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q6:'For which of the following disciplines is Nobel Prize awarded?',('Physics and Chemistry','Physiology or Medicine','Literature, Peace and Economics','All of the above'),3);
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q7:'Hitler party which came into power in 1933 is known as',('Labour Party','Nazi Party','Ku-Klux-Klan','Democratic Party'),1);
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q8:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q9:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q10:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q11:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q12:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q13:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q14:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
			//Q15:
			questionRecord = new QuestionRecord();
			questionRecord.setQuestion("What is the capital of India");
			questionRecord.setOptionList(new ArrayList<>(Arrays.asList("Telangana", "Delhi", "Punjab", "Gujarat")));
			questionRecord.setCorrectAnswerIndex(1);
			repo.save(questionRecord);
		};
	}*/
}