package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
public class TestBeforeAfter {

    @BeforeClass
    public static void setup() {
        Setup.startServer();
    }


    /**
     * Tests before and after annotations {@link com.ftpix.sparknnotation.testapp.BeforeAfterController}
     * @throws UnirestException
     */
    @Test
    public void testBeforeAfter() throws UnirestException {
        HttpResponse<String> stringHttpResponse = Unirest.get(Setup.LOCALHOST + "/before-after/test").asString();

        String before = stringHttpResponse.getHeaders().getFirst("before");
        String after = stringHttpResponse.getHeaders().getFirst("after");
        String afterAfter = stringHttpResponse.getHeaders().getFirst("afterAfter");

        assertEquals("before", before);
        assertEquals("after", after);
        assertEquals("afterAfter", afterAfter);
    }

}
