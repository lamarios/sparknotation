package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TypedParamTest {


    @BeforeClass
    public static void setup() {
        Setup.startServer();
    }

    /**
     * Test an int controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#test(int)}
     * @throws UnirestException
     */
    @Test
    public void testInt() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST + "/typed/int/123").asString().getBody();
        assertEquals(123, Integer.parseInt(result));

    }

    /**
     * Tests a float controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testFloat(float)}
     * @throws UnirestException
     */
    @Test
    public void testFloat() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST + "/typed/float/123.32").asString().getBody();
        assertEquals("123.32", result);

    }

    /**
     * Test double controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testDouble(double)}
     * @throws UnirestException
     */
    @Test
    public void testDouble() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST + "/typed/double/123.11").asString().getBody();
        assertEquals("123.11", result);

    }

    /**
     * Test long controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testLong(long)}
     * @throws UnirestException
     */
    @Test
    public void testLong() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST + "/typed/long/321321").asString().getBody();
        assertEquals(321321l, Long.parseLong(result));

    }

    /**
     * Test boolean controller method paramter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testBoolean(boolean)}
     * @throws UnirestException
     */
    @Test
    public void testBoolean() throws UnirestException {
        String result = Unirest.get(Setup.LOCALHOST + "/typed/boolean/true").asString().getBody();
        assertEquals(true, Boolean.parseBoolean(result));

    }

    /**
     * Test an empty int controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#test(int)}
     * @throws UnirestException
     */
    @Test
    public void testIntPost() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST + "/typed/int").asString().getBody();
        assertEquals("0", result );
    }

    /**
     * Tests a float controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testFloat(float)}
     * @throws UnirestException
     */
    @Test
    public void testFloatPost() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST + "/typed/float").asString().getBody();
        assertEquals("0.0", result);

    }

    /**
     * Test double controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testDouble(double)}
     * @throws UnirestException
     */
    @Test
    public void testDoublePost() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST + "/typed/double").asString().getBody();
        assertEquals("0.0", result);

    }

    /**
     * Test long controller method parameter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testLong(long)}
     * @throws UnirestException
     */
    @Test
    public void testLongPost() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST + "/typed/long").asString().getBody();
        assertEquals(0, Long.parseLong(result));

    }

    /**
     * Test boolean controller method paramter {@link com.ftpix.sparknnotation.testapp.TypedParametersController#testBoolean(boolean)}
     * @throws UnirestException
     */
    @Test
    public void testBooleanPost() throws UnirestException {
        String result = Unirest.post(Setup.LOCALHOST + "/typed/boolean").asString().getBody();
        assertEquals(false, Boolean.parseBoolean(result));

    }
}
