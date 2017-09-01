package com.ftpix.sparknnotation.annotations;

import com.ftpix.sparknnotation.defaultvalue.DefaultBodyTransformer;
import com.ftpix.sparknnotation.interfaces.BodyTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SparkBody {
    Class<? extends BodyTransformer> value() default DefaultBodyTransformer.class;
}
