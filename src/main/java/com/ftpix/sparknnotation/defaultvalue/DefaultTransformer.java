package com.ftpix.sparknnotation.defaultvalue;

import spark.ResponseTransformer;

public class DefaultTransformer implements ResponseTransformer {

    @Override
    public String render(Object o) throws Exception {
        return o.toString();
    }
}
