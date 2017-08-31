package com.ftpix.sparknnotation.testapp.transformer;

import com.ftpix.sparknnotation.interfaces.BodyTransformer;
import com.google.gson.Gson;

public class JsonBodyTransformer implements BodyTransformer {
    @Override
    public <T> T transform(String s, Class<T> clazz) {
        return new Gson().fromJson(s, clazz);
    }
}
