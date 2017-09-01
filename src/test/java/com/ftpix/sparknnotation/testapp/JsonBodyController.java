package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.annotations.SparkBody;
import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkPost;
import com.ftpix.sparknnotation.testapp.model.SuperModel;
import com.ftpix.sparknnotation.testapp.transformer.ModelBodyTransformer;

@SparkController(value ="/json-body")
public class JsonBodyController {


    @SparkPost
    public String test(@SparkBody SuperModel model){
        return model.getName()+"-"+model.getCount();
    }



    @SparkPost(value = "/2")
    public String test2(@SparkBody(value = ModelBodyTransformer.class) SuperModel model){
        return model.getName()+"-"+model.getCount();
    }
}
