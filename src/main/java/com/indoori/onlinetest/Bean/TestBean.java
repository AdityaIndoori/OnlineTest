package com.indoori.onlinetest.Bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class TestBean implements Serializable {
    private Long testId;
    private Integer currentQuestionIndex;
}