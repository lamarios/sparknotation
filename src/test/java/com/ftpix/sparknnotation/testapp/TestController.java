package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.*;
import spark.Request;
import spark.Response;

@SparkController(name = "test")
public class TestController {


    @SparkGet(value = "/hello/:value")
    public String helloWorld(@SparkParam(value = "value") String name) {
        return "Hello " + name + " !";
    }


    @SparkGet(value = "/hello/:firstName/:lastName")
    public String helloFullName(
            @SparkParam(value = "firstName") String firstName,
            @SparkParam(value = "lastName") String lastName) {
        return "Hello " + firstName + " " + lastName + " !";
    }

    @SparkPost(value = "/hello")
    public String helloPost(
            @SparkQueryParam(value = "value") String name
    ) {
        return "Hello " + name + " !";
    }


    @SparkGet(value = "/testRequest/:value")
    public String testRequest(Request request, Response response) {
        response.header("test-header", "header");
        return request.params("value");
    }


    @SparkGet(value ="/testSplat/*/and/*")
    public String testSplat(@SparkSplat(value = 1) String secondSplat, @SparkSplat String firstSplat){
        return firstSplat+" and "+secondSplat;
    }
}
