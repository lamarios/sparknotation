package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.*;
import spark.Request;
import spark.Response;

@SparkController(name = "test")
public class TestController {


    @SparkGet(path = "/hello/:name")
    public String helloWorld(@SparkParam(name = "name") String name) {
        return "Hello " + name + " !";
    }


    @SparkGet(path = "/hello/:firstName/:lastName")
    public String helloFullName(
            @SparkParam(name = "firstName") String firstName,
            @SparkParam(name = "lastName") String lastName) {
        return "Hello " + firstName + " " + lastName + " !";
    }

    @SparkPost(path = "/hello")
    public String helloPost(
            @SparkQueryParam(name = "name") String name
    ) {
        return "Hello " + name + " !";
    }


    @SparkGet(path = "/testRequest/:name")
    public String testRequest(Request request, Response response) {
        response.header("test-header", "header");
        return request.params("name");
    }


    @SparkGet(path="/testSplat/*/and/*")
    public String testSplat(@SparkSplat(index = 1) String secondSplat, @SparkSplat String firstSplat){
        return firstSplat+" and "+secondSplat;
    }
}
