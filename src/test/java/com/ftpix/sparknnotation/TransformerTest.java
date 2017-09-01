package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.Setup;
import com.ftpix.sparknnotation.testapp.TranformerController;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;



public class TransformerTest {


    @BeforeClass
    public static void init(){
        Setup.startServer();
    }


    /**
     * Test json response value {@link TranformerController#test()}
     * @throws UnirestException
     */
    @Test
    public void testJsonTransformer() throws UnirestException {
        String json = Unirest.get(Setup.LOCALHOST+"/value").asString().getBody();

        Map<String, String> result = new GsonBuilder().create().fromJson(json, HashMap.class);

        assertEquals("world",result.get("hello") );
        assertEquals("pepper", result.get("salt"));
    }

    /**
     * Test json response value {@link TranformerController#test()}
     * @throws UnirestException
     */
    @Test
    public void testJsonTransformerPut() throws UnirestException {
        String json = Unirest.put(Setup.LOCALHOST+"/value").asString().getBody();

        Map<String, String> result = new GsonBuilder().create().fromJson(json, HashMap.class);

        assertEquals("world",result.get("hello") );
        assertEquals("pepper", result.get("salt"));
    }


    /**
     * Test json response value {@link TranformerController#test()}
     * @throws UnirestException
     */
    @Test
    public void testJsonTransformerPost() throws UnirestException {
        String json = Unirest.post(Setup.LOCALHOST+"/value").asString().getBody();

        Map<String, String> result = new GsonBuilder().create().fromJson(json, HashMap.class);

        assertEquals("world",result.get("hello") );
        assertEquals("pepper", result.get("salt"));
    }


    /**
     * Test json response value {@link TranformerController#test()}
     * @throws UnirestException
     */
    @Test
    public void testJsonTransformerDelete() throws UnirestException {
        String json = Unirest.delete(Setup.LOCALHOST+"/value").asString().getBody();

        Map<String, String> result = new GsonBuilder().create().fromJson(json, HashMap.class);

        assertEquals("world",result.get("hello") );
        assertEquals("pepper", result.get("salt"));
    }


    /**
     * Test json response value {@link TranformerController#test()}
     * @throws UnirestException
     */
    @Test
    public void testJsonTransformerOptions() throws UnirestException {
        String json = Unirest.options(Setup.LOCALHOST+"/value").asString().getBody();

        Map<String, String> result = new GsonBuilder().create().fromJson(json, HashMap.class);

        assertEquals("world",result.get("hello") );
        assertEquals("pepper", result.get("salt"));
    }
}
