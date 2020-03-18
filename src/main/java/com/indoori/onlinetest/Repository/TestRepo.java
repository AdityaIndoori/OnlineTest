package com.indoori.onlinetest.Repository;

import com.indoori.onlinetest.Bean.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepo extends JpaRepository<TestRecord,Long> {
}
