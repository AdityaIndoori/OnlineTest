package com.indoori.onlinetest.Controller;

import com.indoori.onlinetest.Service.DBFileParseService;
import com.indoori.onlinetest.Service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@Slf4j
public class TestController {
    @Autowired
    S3Service s3Factory;
    @Autowired
    DBFileParseService fileParseService;

    @GetMapping(path = "/questionfile")
    public String uploadFile(@RequestParam(value = "file") String file) throws IOException {
        int numberOfQuestionsAdded = fileParseService.SaveS3FileToH2(file);
        return "Added "+numberOfQuestionsAdded+" questions to database";
    }
}
