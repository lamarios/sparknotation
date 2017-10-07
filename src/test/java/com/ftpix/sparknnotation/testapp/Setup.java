package com.ftpix.sparknnotation.testapp;

import com.ftpix.sparknnotation.Sparknotation;
import com.google.gson.Gson;
import spark.Spark;

import java.io.IOException;

public class Setup {

    public static final String LOCALHOST = "http://localhost:14567";

    private static boolean started = false;

    public synchronized  static void startServer() {
        if (!started) {
            started = true;
            Spark.port(14567);
            Spark.before("/*", (request, response) -> {
                System.out.println(request.requestMethod() + " " + request.pathInfo());
            });

            Gson gson = new Gson();

            try {
                Sparknotation.init(gson::fromJson, new ControllerWithConstructorParam("hello"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Spark.exception(Exception.class, (e,req, res) ->{
                e.printStackTrace();
            });

            System.out.println("Starting server");
        }
    }
}
