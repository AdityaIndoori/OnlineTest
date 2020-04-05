package com.indoori.onlinetest.Service;

import com.indoori.onlinetest.Bean.QuestionRecord;
import com.indoori.onlinetest.Repository.QuestionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DBFileParseService {
    @Autowired
    S3Service s3Factory;
    @Autowired
    private QuestionRepo questionRepo;

    public int SaveS3FileToH2(String fileName){
        int lineNumber = 0;
        int numberOfQuestionsAdded = 0;
        InputStream is = null;
        BufferedReader bfReader = null;
        String temp = null;
        QuestionRecord questionRecord = null;
        byte[] data = s3Factory.getFile(fileName);

        try {
            is = new ByteArrayInputStream(data);
            bfReader = new BufferedReader(new InputStreamReader(is));
            while((temp = bfReader.readLine()) != null){
                if(temp.trim().equals("~")){
                    lineNumber = 0;
                    questionRecord = new QuestionRecord();
                }
                else {
                    lineNumber++;
                    switch (lineNumber){
                        case 1:
                            questionRecord.setQuestion(temp);
                            break;
                        case 2:
                            questionRecord.setOption1(temp);
                            break;
                        case 3:
                            questionRecord.setOption2(temp);
                            break;
                        case 4:
                            questionRecord.setOption3(temp);
                            break;
                        case 5:
                            questionRecord.setOption4(temp);
                            break;
                        case 6:
                            questionRecord.setCorrect(Integer.parseInt(temp));
                            questionRecord = questionRepo.save(questionRecord);
                            numberOfQuestionsAdded++;
                            break;
                        default:
                            questionRecord = null;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberOfQuestionsAdded;
    }
}
