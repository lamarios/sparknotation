package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.*;
import com.ftpix.sparknnotation.testapp.transformer.JsonTransformer;

import java.util.HashMap;
import java.util.Map;

@SparkController(path = "/transformer")
public class TranformerController {


    @SparkGet(transformer = JsonTransformer.class)
    @SparkPost(transformer = JsonTransformer.class)
    @SparkOptions(transformer = JsonTransformer.class)
    @SparkDelete(transformer = JsonTransformer.class)
    @SparkPut(transformer = JsonTransformer.class)
    public Map<String, String> test(){
        Map<String, String> result = new HashMap<>();

        result.put("hello","world");
        result.put("salt", "pepper");

        return result;
    }
}
