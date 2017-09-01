package com.ftpix.sparknnotation.defaultvalue;

import spark.ModelAndView;
import spark.TemplateEngine;

public class DefaultTemplateEngine extends TemplateEngine {
    @Override
    public String render(ModelAndView modelAndView) {
        return modelAndView.toString();
    }
}
