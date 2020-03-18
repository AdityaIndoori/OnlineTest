package com.indoori.onlinetest.Bean;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "TEST")
public class TestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    private String userName;
    @ElementCollection
    private List<Long> questionIdList;
    @ElementCollection
    private List<Integer> selectedOptionList;
    private Integer timeTaken;
    private Integer correctQuestions;
    private Double percentage;
}