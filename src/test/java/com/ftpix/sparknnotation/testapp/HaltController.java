package com.ftpix.sparknnotation.testapp;


import com.ftpix.sparknnotation.annotations.*;
import spark.Spark;

@SparkController("/halt")
public class HaltController {

    @SparkGet
    public int haltGet(){
        Spark.halt(500, "oops");
        return 0;
    }

    @SparkPost
    public int haltPost(){
        Spark.halt(401, "nope");
        return  0;
    }


    @SparkPut
    public int haltPut(){
        Spark.halt(300, "not here");
        return  0;
    }

    @SparkDelete
    public int haltDelete(){
        Spark.halt();
        return  0;
    }

    @SparkOptions
    public int haltOption(){
        Spark.halt(600, "nopenope");
        return  0;
    }
}
