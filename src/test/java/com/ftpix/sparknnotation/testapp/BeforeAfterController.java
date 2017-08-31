package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.annotations.*;
import spark.Response;

@SparkController(path = "/before-after")
public class BeforeAfterController {

    @SparkBefore(path = "/*")
    public void before(Response response){
        response.header("before", "before");
    }

    @SparkAfter(path = "/*")
    public void after(Response response){
        response.header("after", "after");
    }

    @SparkAfterAfter(path = "/*")
    public void afterAfter(Response response){
        response.header("afterAfter", "afterAfter");
    }


    @SparkGet(path = "/test")
    public String test(){
        return "yo";
    }

}
