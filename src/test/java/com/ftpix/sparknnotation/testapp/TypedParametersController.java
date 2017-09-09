package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.*;

@SparkController(name = "typed", value = "/typed")
public class TypedParametersController {


    @SparkGet(value = "/int/:id")
    public int test(@SparkParam(value = "id") int id) {
        return id;
    }

    @SparkGet(value = "/float/:id")
    public float testFloat(@SparkParam(value = "id") float id) {
        return id;
    }

    @SparkGet(value = "/long/:id")
    public long testLong(@SparkParam(value = "id") long id) {
        return id;
    }

    @SparkGet(value = "/double/:id")
    public double testDouble(@SparkParam(value = "id") double id) {
        return id;
    }

    @SparkGet(value = "/boolean/:id")
    public boolean testBoolean(@SparkParam(value = "id") boolean id) {
        return id;
    }


    @SparkPost(value = "/int")
    public int testPost(@SparkQueryParam(value = "id") int id) {
        return id;
    }

    @SparkPost(value = "/float")
    public float testFloatPost(@SparkQueryParam(value = "id") float id) {
        return id;
    }

    @SparkPost(value = "/long")
    public long testLongPost(@SparkQueryParam(value = "id") long id) {
        return id;
    }

    @SparkPost(value = "/double")
    public double testDoublePost(@SparkQueryParam(value = "id") double id) {
        return id;
    }

    @SparkPost(value = "/boolean")
    public boolean testBooleanPost(@SparkQueryParam(value = "id") boolean id) {
        return id;
    }

}
