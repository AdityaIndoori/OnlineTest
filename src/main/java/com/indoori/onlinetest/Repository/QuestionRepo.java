package com.indoori.onlinetest.Repository;

import com.indoori.onlinetest.Bean.QuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<QuestionRecord, Long> {
}
