package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;

@SparkController("/accept-type")
public class AcceptTypeController {


    @SparkGet(accept = "application/json")
    public String acceptJson(){
        return "json";
    }

    @SparkGet(accept = "application/xml")
    public String acceptXML(){
        return "xml";
    }

}
