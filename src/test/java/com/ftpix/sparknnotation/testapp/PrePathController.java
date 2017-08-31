package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;

@SparkController(name = "prepathcontroller", path="/test")
public class PrePathController {


    @SparkGet(path = "/ping")
    public String pong(){
        return "pong";
    }
}
