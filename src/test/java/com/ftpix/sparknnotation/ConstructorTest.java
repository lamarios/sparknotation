package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.ControllerWithConstructorParam;
import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstructorTest {


    @BeforeClass
    public static void init(){
        Setup.startServer();
    }


    /**
     * Tests if a constructor added with a constructor works
     * {@link Setup#startServer()} -> where the controller is created
     * {@link ControllerWithConstructorParam}  -> the controller itself
     * @return
     */
    @Test
    public void testController() throws UnirestException {

        String result = Unirest.get(Setup.LOCALHOST + "/with-constructor").asString().getBody();

        assertEquals("hello", result);

        assertEquals("hello", Sparknotation.getController(ControllerWithConstructorParam.class).hello());

    }
}
