package com.ftpix.sparknnotation;


import com.ftpix.sparknnotation.testapp.Setup;
import com.ftpix.sparknnotation.testapp.model.SuperModel;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonBodyTest {

    @BeforeClass
    public static void init(){
        Setup.startServer();
    }

    /**
     * Tests the globally set BodyTransformer {@link com.ftpix.sparknnotation.testapp.JsonBodyController#test(SuperModel)}
      * @throws UnirestException
     */
    @Test
    public void testJsonBody() throws UnirestException {
        SuperModel model = new SuperModel();
        model.setName("hello");
        model.setCount(2);

        Gson gson = new Gson();

        String result = Unirest.post(Setup.LOCALHOST+"/json-body").body(gson.toJson(model)).asString().getBody();

        Assert.assertEquals("hello-2", result);

    }


    /**
     * Tests when the BodyTransformer class is set from the annotation itself {@link com.ftpix.sparknnotation.testapp.JsonBodyController#test2(SuperModel)}
     * @throws UnirestException
     */
    @Test
    public void testJsonBodyFromBodyAnnotation() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST+"/json-body/2").body("value=salt;count=3").asString().getBody();
        Assert.assertEquals("salt-3", result);
    }

}
