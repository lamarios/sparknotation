package com.ftpix.sparknnotation.testapp.transformer;

import com.ftpix.sparknnotation.interfaces.BodyTransformer;
import com.ftpix.sparknnotation.testapp.model.SuperModel;

public class ModelBodyTransformer implements BodyTransformer {
    @Override
    public <T> T transform(String s, Class<T> clazz) {
        SuperModel model = new SuperModel();

        String[] split = s.split(";");

        model.setName(split[0].split("=")[1]);
        model.setCount(Integer.parseInt(split[1].split("=")[1]));

        return (T) model;
    }
}
