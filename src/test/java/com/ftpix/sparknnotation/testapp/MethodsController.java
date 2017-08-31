package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkDelete;
import com.ftpix.sparknnotation.annotations.SparkOptions;
import com.ftpix.sparknnotation.annotations.SparkPut;

@SparkController(path = "/methods")
public class MethodsController {

    @SparkPut(path = "/put")
    public String put(){
        return "put";
    }

    @SparkDelete(path = "/delete")
    public String delete(){
        return "delete";
    }

    @SparkOptions(path = "/options")
    public String options(){
        return "options";
    }



}
