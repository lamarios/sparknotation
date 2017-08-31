package com.ftpix.sparknnotation.annotations;

import com.ftpix.sparknnotation.defaultvalue.DefaultTransformer;
import spark.ResponseTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SparkGet {
    String value() default "";
    Class<? extends ResponseTransformer> transformer() default DefaultTransformer.class;
}
