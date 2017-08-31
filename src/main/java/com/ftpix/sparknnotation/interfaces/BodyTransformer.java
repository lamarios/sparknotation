package com.ftpix.sparknnotation.interfaces;

@FunctionalInterface
public interface BodyTransformer {

    <T> T transform(String s, Class<T> clazz);
}
