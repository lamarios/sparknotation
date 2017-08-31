package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.TestController;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Request;
import spark.Response;

import static com.ftpix.sparknnotation.testapp.Setup.LOCALHOST;
import static com.ftpix.sparknnotation.testapp.Setup.startServer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleTest {



    @BeforeClass
    public static void setUpServer() {
        startServer();
    }


    /**
     * Check if controllers can be found again after init
     */
    @Test
    public void testControllerExists() {
        assertTrue(Sparknnotation.getController("test", TestController.class).getClass().equals(TestController.class));
        assertTrue(Sparknnotation.getController(TestController.class).getClass().equals(TestController.class));
    }


    /**
     * Tests a simple get method with a parameter {@link TestController#helloWorld(String)}
     * @throws UnirestException
     */
    @Test
    public void testGet() throws UnirestException {
        String result = Unirest.get(LOCALHOST + "/hello/moto").asString().getBody();
        assertEquals("Hello moto !", result);

    }

    /**
     * Tests a get request with 2 parameters {@link TestController#helloFullName(String, String)}
     * @throws UnirestException
     */
    @Test
    public void testGetFullName() throws UnirestException {
        String result = Unirest.get(LOCALHOST + "/hello/moto/rolla").asString().getBody();
        assertEquals("Hello moto rolla !", result);
    }


    /**
     * Test a post request with a single parameter {@link TestController#helloPost(String)}
     * @throws UnirestException
     */
    @Test
    public void testPostName() throws UnirestException {
        String result = Unirest.post(LOCALHOST + "/hello").field("value", "moto").asString().getBody();


        assertEquals("Hello moto !", result);
    }


    /**
     * Test when controller method is using Request and Response Spark objects {@link TestController#testRequest(Request, Response)}
     * @throws UnirestException
     */
    @Test
    public void testResponseAndRequest() throws UnirestException {
        HttpResponse<String> result = Unirest.get(LOCALHOST + "/testRequest/Yo").asString();

        String header = result.getHeaders().getFirst("test-header");

        assertEquals("Yo", result.getBody());
        assertEquals("header", header);

    }


    /**
     * Test Spark splats {@link TestController#testSplat(String, String)}
     * @throws UnirestException
     */
    @Test
    public void testSplat() throws UnirestException {

        String result = Unirest.get(LOCALHOST + "/testSplat/salt/and/pepper").asString().getBody();

        assertEquals("salt and pepper", result);
    }
}
