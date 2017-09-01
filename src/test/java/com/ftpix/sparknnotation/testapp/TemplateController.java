package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;
import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@SparkController("/template")
public class TemplateController {


    @SparkGet(value = "/hello/:name", templateEngine = JadeTemplateEngine.class)
    public ModelAndView hello(@SparkParam("name") String name){
        Map<String, String> map = new HashMap<>();
        map.put("name", name);

        return new ModelAndView(map, "hello");
    }
}
