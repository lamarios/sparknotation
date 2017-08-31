package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.PrePathController;
import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.ftpix.sparknnotation.testapp.Setup.LOCALHOST;
import static org.junit.Assert.assertEquals;
public class PathControllerTest {


    @BeforeClass
    public static void setUpServer() {
        Setup.startServer();
    }


    /**
     * Tests when a Path prefix is set on the controller {@link PrePathController#pong()}
     *
     * @throws UnirestException
     */
    @Test
    public void testPathGet() throws UnirestException {

        String response = Unirest.get(LOCALHOST + "/test/ping").asString().getBody();

        assertEquals("pong", response);

    }

}
