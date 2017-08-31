package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkDelete;
import com.ftpix.sparknnotation.annotations.SparkOptions;
import com.ftpix.sparknnotation.annotations.SparkPut;

@SparkController(value = "/methods")
public class MethodsController {

    @SparkPut(value = "/put")
    public String put(){
        return "put";
    }

    @SparkDelete(value = "/delete")
    public String delete(){
        return "delete";
    }

    @SparkOptions(value = "/options")
    public String options(){
        return "options";
    }



}
