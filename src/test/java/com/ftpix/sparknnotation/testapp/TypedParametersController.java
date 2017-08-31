package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;

@SparkController(name="typed", path = "/typed")
public class TypedParametersController {




    @SparkGet(path = "/int/:id")
    public int test(@SparkParam(name="id") int id){
        return id;
    }

    @SparkGet(path="/float/:id")
    public float testFloat(@SparkParam(name="id") float id){
        return id;
    }

    @SparkGet(path="/long/:id")
    public long testLong(@SparkParam(name="id") long id){
        return id;
    }

    @SparkGet(path="/double/:id")
    public double testDouble(@SparkParam(name="id") double id){
        return id;
    }

    @SparkGet(path="/boolean/:id")
    public boolean testBoolean(@SparkParam(name="id") boolean id){
        return id;
    }



}
