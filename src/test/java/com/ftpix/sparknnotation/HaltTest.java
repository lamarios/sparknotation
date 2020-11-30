package com.ftpix.sparknnotation;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.ftpix.sparknnotation.testapp.Setup.LOCALHOST;
import static com.ftpix.sparknnotation.testapp.Setup.startServer;
import static org.junit.Assert.assertEquals;

public class HaltTest {

    @BeforeClass
    public static void setUpServer() {
        startServer();
    }

    @Test
    public void testHalt() throws UnirestException {
        HttpResponse<String> def;

        def = Unirest.get(LOCALHOST + "/halt").asString();
        assertEquals(500, def.getStatus());
        assertEquals("oops", def.getBody());


        def = Unirest.post(LOCALHOST + "/halt").asString();
        assertEquals(401, def.getStatus());
        assertEquals("nope", def.getBody());


        def = Unirest.put(LOCALHOST + "/halt").asString();
        assertEquals(300, def.getStatus());
        assertEquals("not here", def.getBody());

        def = Unirest.delete(LOCALHOST + "/halt").asString();
        assertEquals(200, def.getStatus());
        assertEquals("", def.getBody());

        def = Unirest.options(LOCALHOST + "/halt").asString();
        assertEquals(600, def.getStatus());
        assertEquals("nopenope", def.getBody());

        def = Unirest.get(LOCALHOST + "/halt/before").asString();
        assertEquals(401, def.getStatus());
        assertEquals("unauthorized", def.getBody());

    }
}
