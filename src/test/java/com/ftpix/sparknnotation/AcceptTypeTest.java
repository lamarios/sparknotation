package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.testapp.AcceptTypeController;
import com.ftpix.sparknnotation.testapp.Setup;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AcceptTypeTest {

    @BeforeClass
    public static  void init(){
        Setup.startServer();
    }


    /**
     * Testing same endpoint with different accept type to make sure it's differentiated properly
     * {@link AcceptTypeController#acceptXML()}
     * {@link AcceptTypeController#acceptJson()}
     *
     * @throws UnirestException
     */
    @Test
    public void testAcceptType() throws UnirestException {

        String json = Unirest.get(Setup.LOCALHOST+"/accept-type").header("Accept", "application/json").asString().getBody();
        String xml = Unirest.get(Setup.LOCALHOST+"/accept-type").header("Accept", "application/xml").asString().getBody();

        Assert.assertEquals("json", json);
        Assert.assertEquals("xml", xml);
    }
}
