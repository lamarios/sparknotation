package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TemplateTest {


    @BeforeClass
    public static  void init(){
        Setup.startServer();
    }


    /**
     * Tests template compilation {@link com.ftpix.sparknnotation.testapp.TemplateController#hello(String)}
     * @throws UnirestException
     */
    @Test
    public void testTemplate() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST+"/template/hello/world").asString().getBody();

        Assert.assertEquals("<h1>hello world</h1>", result);
    }
}
