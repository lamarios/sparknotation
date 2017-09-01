package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.annotations.*;
import com.ftpix.sparknnotation.defaultvalue.DefaultBodyTransformer;
import com.ftpix.sparknnotation.defaultvalue.DefaultTemplateEngine;
import com.ftpix.sparknnotation.defaultvalue.DefaultTransformer;
import com.ftpix.sparknnotation.interfaces.BodyTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import spark.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Sparknotation {

    private static Map<String, Object> controllers = new HashMap<>();
    private static Logger logger = LogManager.getLogger(Sparknotation.class);
    private static BodyTransformer bodyTransformer;


    private static Predicate<Annotation> isSparkAnnotation = a -> a.annotationType().equals(SparkGet.class)
            || a.annotationType().equals(SparkPut.class)
            || a.annotationType().equals(SparkDelete.class)
            || a.annotationType().equals(SparkOptions.class)
            || a.annotationType().equals(SparkPost.class)
            || a.annotationType().equals(SparkBefore.class)
            || a.annotationType().equals(SparkAfter.class)
            || a.annotationType().equals(SparkAfterAfter.class);


    public static void init() {
        init(null);
    }

    /**
     * Will find all the @SparkController annotations and will process them.
     *
     * @param transformer a json body value
     */
    public static void init(BodyTransformer transformer) {

        bodyTransformer = transformer;

        logger.info("Json Body Transformer");

        Set<Class<?>> typesAnnotatedWith = new Reflections("").getTypesAnnotatedWith(SparkController.class);

        logger.debug("found {} classes with @SparkController annotation", typesAnnotatedWith.size());

        typesAnnotatedWith.stream()
                .forEach(clazz -> {

                    try {
                        SparkController annotation = clazz.getAnnotation(SparkController.class);

                        Object o = clazz.newInstance();

                        String controllerName = Optional.of(annotation.name())
                                .filter(n -> n.trim().length() > 0)
                                .orElse(o.getClass().getCanonicalName());

                        controllers.put(controllerName, o);

                        String path = Optional.ofNullable(annotation.value()).orElse("");

                        //find method with desired annotations
                        Stream.of(clazz.getDeclaredMethods())
                                .filter(m -> Stream.of(m.getDeclaredAnnotations()).anyMatch(isSparkAnnotation))
                                .forEach(m -> Sparknotation.processMethod(path, o, m));
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

    }


    /**
     * Process methods with any of the Spark annotation
     *
     * @param controllerPath the value prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     */
    private static void processMethod(String controllerPath, Object controller, Method method) {
        Optional.of(method).map(m -> m.getAnnotation(SparkGet.class)).ifPresent(a -> Sparknotation.createEndPoint(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkPost.class)).ifPresent(a -> Sparknotation.createEndPoint(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkPut.class)).ifPresent(a -> Sparknotation.createEndPoint(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkDelete.class)).ifPresent(a -> Sparknotation.createEndPoint(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkOptions.class)).ifPresent(a -> Sparknotation.createEndPoint(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkBefore.class)).ifPresent(a -> Sparknotation.createBefore(controllerPath, controller, method, a));

        Optional.of(method).map(m -> m.getAnnotation(SparkAfter.class)).ifPresent(a -> Sparknotation.createAfter(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkAfterAfter.class)).ifPresent(a -> Sparknotation.createAfterAfter(controllerPath, controller, method, a));

    }


    private static void createEndPoint(String controllerPath, Object controller, Method method, Annotation annotation) {

        String value = null;
        ResponseTransformer transformer = null;
        TemplateEngine templateEngine = null;
        String acceptType = "*/*";
        String sparkMethod = null;
        try {
            switch (annotation.annotationType().getSimpleName()) {
                case "SparkGet":
                    SparkGet get = (SparkGet) annotation;
                    value = get.value();
                    transformer = get.transformer().newInstance();
                    templateEngine = get.templateEngine().newInstance();
                    acceptType = get.accept();
                    sparkMethod = "get";
                    break;
                case "SparkPost":
                    SparkPost post = (SparkPost) annotation;
                    value = post.value();
                    transformer = post.transformer().newInstance();
                    templateEngine = post.templateEngine().newInstance();
                    acceptType = post.accept();
                    sparkMethod = "post";
                    break;
                case "SparkPut":
                    SparkPut put = (SparkPut) annotation;
                    value = put.value();
                    transformer = put.transformer().newInstance();
                    templateEngine = put.templateEngine().newInstance();
                    acceptType = put.accept();
                    sparkMethod = "put";
                    break;
                case "SparkDelete":
                    SparkDelete delete = (SparkDelete) annotation;
                    value = delete.value();
                    transformer = delete.transformer().newInstance();
                    templateEngine = delete.templateEngine().newInstance();
                    acceptType = delete.accept();
                    sparkMethod = "delete";
                    break;
                case "SparkOptions":
                    SparkOptions opts = (SparkOptions) annotation;
                    value = opts.value();
                    transformer = opts.transformer().newInstance();
                    templateEngine = opts.templateEngine().newInstance();
                    acceptType = opts.accept();
                    sparkMethod = "options";
                    break;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create value, using DefaultTransformer", e);
            transformer = new DefaultTransformer();
            templateEngine = new DefaultTemplateEngine();
            sparkMethod = null;
        }

        final String path = (controllerPath + value).trim();

        Class spark = Spark.class;
        List<Class> sparkMethodParams = new ArrayList<>();
        sparkMethodParams.add(String.class);
        sparkMethodParams.add(String.class);


        try {
            //method that use a template
            if (method.getReturnType().equals(ModelAndView.class)) {
                sparkMethodParams.add(TemplateViewRoute.class);
                sparkMethodParams.add(TemplateEngine.class);
                Optional<Method> optionalMethod = Optional.ofNullable(spark.getMethod(sparkMethod, sparkMethodParams.toArray(new Class[sparkMethodParams.size()])));

                TemplateViewRoute route = (req, res) -> (ModelAndView) methodContent(controller, method, req, res);
                if(optionalMethod.isPresent()){
                    Method m = optionalMethod.get();
                    logger.info("Creating {} [{}] on controller: {} with TemplateEngine {}", sparkMethod, path, controller.getClass(), templateEngine.getClass());
                    m.invoke(null, path, acceptType, route, templateEngine);
                }
            } else { //normal methods
                sparkMethodParams.add(Route.class);
                sparkMethodParams.add(ResponseTransformer.class);

                Optional<Method> optionalMethod = Optional.ofNullable(spark.getMethod(sparkMethod, sparkMethodParams.toArray(new Class[sparkMethodParams.size()])));

                Route route = (req, res) -> methodContent(controller, method, req, res);
                if(optionalMethod.isPresent()){
                    Method m = optionalMethod.get();
                    logger.info("Creating {} [{}] on controller: {} with ResponseTransformer {}", sparkMethod, path, controller.getClass(), transformer.getClass());
                    m.invoke(null, path, acceptType, route, transformer);
                }
            }
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.error("Couldn't create Spark endpoint");
            throw new RuntimeException(e);
        }





//        Spark.options(path, acceptType,(request, response) -> methodContent(controller,method, request, response), value);
    }

    /**
     * Creates a Spark before endpoint
     *
     * @param controllerPath the value prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param before         the annotation
     */
    private static void createBefore(String controllerPath, Object controller, Method method, SparkBefore before) {
        String path = (controllerPath + before.value()).trim();

        if (path.length() > 0) {
            logger.info("Creating before [{}] on controller: {}", path, controller.getClass());
            Spark.before(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating before [no-value] on controller: {}", controller.getClass());
            Spark.before((request, response) -> beforeAfterContent(controller, method, request, response));
        }
    }

    /**
     * Creates a Spark After endpoint
     *
     * @param controllerPath the value prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param after          the annotation
     */
    private static void createAfter(String controllerPath, Object controller, Method method, SparkAfter after) {
        String path = (controllerPath + after.value()).trim();

        if (path.length() > 0) {
            logger.info("Creating After [{}] on controller: {}", path, controller.getClass());
            Spark.after(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating After [no-value] on controller: {}", controller.getClass());
            Spark.after((request, response) -> beforeAfterContent(controller, method, request, response));
        }
    }


    /**
     * Creates a Spark AfterAfter endpoint
     *
     * @param controllerPath the value prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param afterAfter     the annotation
     */
    private static void createAfterAfter(String controllerPath, Object controller, Method method, SparkAfterAfter afterAfter) {
        String path = (controllerPath + afterAfter.value()).trim();

        if (path.length() > 0) {
            logger.info("Creating afterAfter [{}] on controller: {}", path, controller.getClass());
            Spark.afterAfter(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating afterAfter [no-value] on controller: {}", controller.getClass());
            Spark.afterAfter((request, response) -> beforeAfterContent(controller, method, request, response));

        }
    }



    /**
     * Calling the method from the controller for before and after
     *
     * @param controller
     * @param method
     * @param request    the Spark request object
     * @param response   the Spark response object
     * @return the result of the  controller's method
     * @throws InvocationTargetException if the invocation of the method fails
     * @throws IllegalAccessException    if the invocation of the method fails
     */
    private static void beforeAfterContent(Object controller, Method method, Request request, Response response) throws InvocationTargetException, IllegalAccessException {

        List<Object> params = buildParamList(method, request, response);

        logger.info("Params {}", params);

        method.invoke(controller, params.toArray());
    }

    /**
     * Calling the method from the controller
     *
     * @param controller
     * @param method
     * @param request    the Spark request object
     * @param response   the Spark response object
     * @return the result of the  controller's method
     * @throws InvocationTargetException if the invocation of the method fails
     * @throws IllegalAccessException    if the invocation of the method fails
     */
    private static Object methodContent(Object controller, Method method, Request request, Response response) throws InvocationTargetException, IllegalAccessException {

        List<Object> params = buildParamList(method, request, response);

        logger.info("Params {}", params);

        return method.invoke(controller, params.toArray());
    }


    /**
     * Builds the parameter list to be used with java reflection invoke method
     *
     * @param method   the controller method
     * @param request  the Spark request object
     * @param response the Spark response object
     * @return
     */
    private static List<Object> buildParamList(Method method, Request request, Response response) {

        List<Object> params = new ArrayList<>();

        Stream.of(method.getParameters())
                .forEach(p -> {
                    if (p.getType().equals(Request.class)) {
                        params.add(request);
                    } else if (p.getType().equals(Response.class)) {
                        params.add(response);
                    } else if (p.getDeclaredAnnotations().length == 1) {

                        logger.info(p.getType());

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkParam.class))
                                .ifPresent(a -> {
                                    Object param = getParamValue(request.params(a.value()), p.getType());
                                    logger.info("Param of type {} has SparkParam annotation with value [{}]", p.getType(), a.value());
                                    params.add(param);
                                });


                        Optional.ofNullable(p.getDeclaredAnnotation(SparkQueryParam.class))
                                .ifPresent(a -> {
                                    Object param = getParamValue(request.queryParams(a.value()), p.getType());
                                    logger.info("Param of type {} has SparkQueryParam annotation with value [{}]", p.getType(), a.value());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkHeader.class))
                                .ifPresent(a -> {
                                    Object param = getParamValue(request.headers(a.value()), p.getType());
                                    logger.info("Param of type {} has SparkHeader annotation with value [{}]", p.getType(), a.value());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkSplat.class))
                                .filter(a -> p.getType().equals(String.class))
                                .ifPresent(a -> {
                                    Object param = request.splat()[a.value()];
                                    logger.info("Param of type {} has SparkSplat annotation with value [{}]", p.getType(), a.value());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkBody.class))
                                .ifPresent(a -> {
                                    Object param;
                                    BodyTransformer transformer;

                                    if (a.value().equals(DefaultBodyTransformer.class)) {
                                        transformer = bodyTransformer;
                                    } else {
                                        try {
                                            transformer = a.value().newInstance();
                                        } catch (InstantiationException | IllegalAccessException e) {
                                            transformer = new DefaultBodyTransformer();
                                            logger.error("Couldn't create the body value");
                                        }
                                    }
                                    param = transformer.transform(request.body(), p.getType());
                                    logger.info("Param of type {} has SparkJsonBody annotation", p.getType());
                                    params.add(param);
                                });

                    }
                });

        return params;

    }


    /**
     * Gets the parameter value and convert it to the same type as the controller method's parameter
     *
     * @param requestParam the value of the parameter
     * @param clazz        the class we're expecting
     * @return
     */
    private static Object getParamValue(String requestParam, Class clazz) {
        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return Integer.parseInt(requestParam);
        } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return Float.parseFloat(requestParam);
        } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return Double.parseDouble(requestParam);
        } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return Boolean.parseBoolean(requestParam);
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return Long.parseLong(requestParam);
        } else {
            return requestParam;
        }


    }


    /**
     * Gets back a controller by its value
     *
     * @param name  the value of the controller
     * @param clazz the class of it
     * @param <T>
     * @return
     */
    public static <T> T getController(String name, Class<T> clazz) {
        return (T) controllers.get(name);
    }


    /**
     * Gets a controller by its class as we won't have more than a single controller by class
     *
     * @param clazz the class of the controller
     * @param <T>
     * @return
     */
    public static <T> T getController(Class<T> clazz) {
        return (T) controllers.values().stream().filter(c -> c.getClass().equals(clazz)).findFirst().orElse(null);
    }
}
