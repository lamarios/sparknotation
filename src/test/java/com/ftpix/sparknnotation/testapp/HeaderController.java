package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkHeader;

@SparkController(value = "/header")
public class HeaderController {

    @SparkGet
    public String test(@SparkHeader(value ="myHeader") String header){
       return header;
    }

}
