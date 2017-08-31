package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.annotations.*;
import spark.Response;

@SparkController(value = "/before-after")
public class BeforeAfterController {

    @SparkBefore(value = "/*")
    public void before(Response response){
        response.header("before", "before");
    }

    @SparkAfter(value = "/*")
    public void after(Response response){
        response.header("after", "after");
    }

    @SparkAfterAfter(value = "/*")
    public void afterAfter(Response response){
        response.header("afterAfter", "afterAfter");
    }


    @SparkGet(value = "/test")
    public String test(){
        return "yo";
    }

}
