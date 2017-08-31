package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;

@SparkController(name = "prepathcontroller", value ="/test")
public class PrePathController {


    @SparkGet(value = "/ping")
    public String pong(){
        return "pong";
    }
}
