package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HeaderTest {

   @BeforeClass
   public static void init(){
       Setup.startServer();
   }


    /**
     * Test getting a header from the controller method parameter {@link com.ftpix.sparknnotation.testapp.HeaderController#test(String)}
     *
     * @throws UnirestException
     */
   @Test
    public void testHeader() throws UnirestException {
       String result = Unirest.get(Setup.LOCALHOST+"/header").header("myHeader", "yo!").asString().getBody();

       Assert.assertEquals("yo!", result);
   }

}
