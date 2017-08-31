package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;

@SparkController(name="typed", value = "/typed")
public class TypedParametersController {




    @SparkGet(value = "/int/:id")
    public int test(@SparkParam(value ="id") int id){
        return id;
    }

    @SparkGet(value ="/float/:id")
    public float testFloat(@SparkParam(value ="id") float id){
        return id;
    }

    @SparkGet(value ="/long/:id")
    public long testLong(@SparkParam(value ="id") long id){
        return id;
    }

    @SparkGet(value ="/double/:id")
    public double testDouble(@SparkParam(value ="id") double id){
        return id;
    }

    @SparkGet(value ="/boolean/:id")
    public boolean testBoolean(@SparkParam(value ="id") boolean id){
        return id;
    }



}
