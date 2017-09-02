package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;

@SparkController("/with-constructor")
public class ControllerWithConstructorParam {
    private String hello;


    public ControllerWithConstructorParam(String hello) {
        this.hello = hello;
    }

    @SparkGet
    public String hello(){
        return hello;
    }
}
