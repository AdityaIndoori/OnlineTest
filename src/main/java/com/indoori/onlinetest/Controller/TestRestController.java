package com.indoori.onlinetest.Controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TestRestController {
    @PostMapping("test")
    public JSONObject test(Model model, String myData, String myData2) throws JSONException {
        log.info("Test called " + myData + " - "+ myData2);
        JSONObject entity = new JSONObject();
        entity.put("Key1","Value1");
        entity.put("Key2","Value2");

        return entity;
    }
}
