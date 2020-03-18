package com.indoori.onlinetest.Bean;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "QUESTION")
public class QuestionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer correct;
}