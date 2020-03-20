package com.indoori.onlinetest.Config;

import com.indoori.onlinetest.Bean.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@Configuration
public class BeanConfiguration {
    @Bean(name = "TestBean")
    @SessionScope
    public TestBean getTestBean(){
        return new TestBean();
    }

    @Bean(name = "RemainingTimeMapBean")
    @ApplicationScope
    public Map<Long,Integer> getRemainingTimerMap(){
        return new HashMap<>();
    }

    @Bean(name = "TimerMapBean")
    @ApplicationScope
    public Map<Long, Timer> getTimerMap(){
        return new HashMap<>();
    }
}
