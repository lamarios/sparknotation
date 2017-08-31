package com.ftpix.sparknnotation;

import com.ftpix.sparknnotation.annotations.*;
import com.ftpix.sparknnotation.defaultvalue.DefaultBodyTransformer;
import com.ftpix.sparknnotation.defaultvalue.DefaultTransformer;
import com.ftpix.sparknnotation.interfaces.BodyTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Sparknnotation {

    private static Map<String, Object> controllers = new HashMap<>();
    private static Logger logger = LogManager.getLogger(Sparknnotation.class);
private static BodyTransformer bodyTransformer;




    private static Predicate<Annotation> isSparkAnnotation = a -> a.annotationType().equals(SparkGet.class)
            || a.annotationType().equals(SparkPut.class)
            || a.annotationType().equals(SparkDelete.class)
            || a.annotationType().equals(SparkOptions.class)
            || a.annotationType().equals(SparkPost.class)
            || a.annotationType().equals(SparkBefore.class)
            || a.annotationType().equals(SparkAfter.class)
            || a.annotationType().equals(SparkAfterAfter.class);



    public static void init(){
        init(null);
    }

    /**
     * Will find all the @SparkController annotations and will process them.
     * @param transformer a json body transformer
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

                        String path = Optional.ofNullable(annotation.path()).orElse("");

                        //find method with desired annotations
                        Stream.of(clazz.getDeclaredMethods())
                                .filter(m -> Stream.of(m.getDeclaredAnnotations()).anyMatch(isSparkAnnotation))
                                .forEach(m -> Sparknnotation.processMethod(path, o, m));
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
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     */
    private static void processMethod(String controllerPath, Object controller, Method method) {
        Optional.of(method).map(m -> m.getAnnotation(SparkGet.class)).ifPresent(a -> Sparknnotation.createGet(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkPost.class)).ifPresent(a -> Sparknnotation.createPost(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkPut.class)).ifPresent(a -> Sparknnotation.createPut(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkDelete.class)).ifPresent(a -> Sparknnotation.createDelete(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkOptions.class)).ifPresent(a -> Sparknnotation.createOptions(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkBefore.class)).ifPresent(a -> Sparknnotation.createBefore(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkAfter.class)).ifPresent(a -> Sparknnotation.createAfter(controllerPath, controller, method, a));
        Optional.of(method).map(m -> m.getAnnotation(SparkAfterAfter.class)).ifPresent(a -> Sparknnotation.createAfterAfter(controllerPath, controller, method, a));
    }

    /**
     * Creates a Spark before endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param before         the annotation
     */
    private static void createBefore(String controllerPath, Object controller, Method method, SparkBefore before) {
        String path = (controllerPath + before.path()).trim();

        if (path.length() > 0) {
            logger.info("Creating before [{}] on controller: {}", path, controller.getClass());
            Spark.before(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating before [no-path] on controller: {}", controller.getClass());
            Spark.before((request, response) -> beforeAfterContent(controller, method, request, response));
        }
    }

    /**
     * Creates a Spark After endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param after          the annotation
     */
    private static void createAfter(String controllerPath, Object controller, Method method, SparkAfter after) {
        String path = (controllerPath + after.path()).trim();

        if (path.length() > 0) {
            logger.info("Creating After [{}] on controller: {}", path, controller.getClass());
            Spark.after(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating After [no-path] on controller: {}", controller.getClass());
            Spark.after((request, response) -> beforeAfterContent(controller, method, request, response));
        }
    }


    /**
     * Creates a Spark AfterAfter endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param afterAfter     the annotation
     */
    private static void createAfterAfter(String controllerPath, Object controller, Method method, SparkAfterAfter afterAfter) {
        String path = (controllerPath + afterAfter.path()).trim();

        if (path.length() > 0) {
            logger.info("Creating afterAfter [{}] on controller: {}", path, controller.getClass());
            Spark.afterAfter(path, (request, response) -> beforeAfterContent(controller, method, request, response));
        } else {
            logger.info("Creating afterAfter [no-path] on controller: {}", controller.getClass());
            Spark.afterAfter((request, response) -> beforeAfterContent(controller, method, request, response));

        }
    }


    /**
     * Creates a Spark option endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param options        the annotation
     */
    private static void createOptions(String controllerPath, Object controller, Method method, SparkOptions options) {
        String path = (controllerPath + options.path()).trim();


        ResponseTransformer t;
        try {
            t = options.transformer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create transformer, using DefaultTransformer", e);
            t = new DefaultTransformer();
        }

        logger.info("Creating OPTIONS [{}] on controller: {}", path, controller.getClass());
        Spark.options(path, (request, response) -> methodContent(controller, method, request, response), t);
    }

    /**
     * Creates a Spark Delete endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param delete         the annotation
     */
    private static void createDelete(String controllerPath, Object controller, Method method, SparkDelete delete) {
        String path = (controllerPath + delete.path()).trim();


        ResponseTransformer t;
        try {
            t = delete.transformer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create transformer, using DefaultTransformer", e);
            t = new DefaultTransformer();
        }


        logger.info("Creating Delete [{}] on controller: {}", path, controller.getClass());
        Spark.delete(path, (request, response) -> methodContent(controller, method, request, response), t);
    }

    /**
     * Creates a Spark Put endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param put            the annotation
     */
    private static void createPut(String controllerPath, Object controller, Method method, SparkPut put) {
        String path = (controllerPath +put.path()).trim();


        ResponseTransformer t;
        try {
            t = put.transformer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create transformer, using DefaultTransformer", e);
            t = new DefaultTransformer();
        }


        logger.info("Creating PUT [{}] on controller: {}", path, controller.getClass());
        Spark.put(path, (request, response) -> methodContent(controller, method, request, response), t);
    }

    /**
     * Creates a Spark Post endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param post           the annotation
     */
    private static void createPost(String controllerPath, Object controller, Method method, SparkPost post) {
        String path = (controllerPath +post.path()).trim();


        ResponseTransformer t;
        try {
            t = post.transformer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create transformer, using DefaultTransformer", e);
            t = new DefaultTransformer();
        }

        logger.info("Creating POST [{}] on controller: {}", path, controller.getClass());
        Spark.post(path, (request, response) -> methodContent(controller, method, request, response), t);
    }

    /**
     * Creates a Spark Get endpoint
     *
     * @param controllerPath the path prefix from the controller
     * @param controller     the controller itself
     * @param method         the method we're processing
     * @param get            the annotation
     */
    private static void createGet(String controllerPath, Object controller, Method method, SparkGet get) {
        String path = (controllerPath +get.path()).trim();
        logger.info("Creating GET [{}] on controller: {}", path, controller.getClass());

        ResponseTransformer t;
        try {
            t = get.transformer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create transformer, using DefaultTransformer", e);
            t = new DefaultTransformer();
        }

        Spark.get(path, (request, response) -> methodContent(controller, method, request, response),t);
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
                                    Object param = getParamValue(request.params(a.name()), p.getType());
                                    logger.info("Param of type {} has SparkParam annotation with name [{}]", p.getType(), a.name());
                                    params.add(param);
                                });


                        Optional.ofNullable(p.getDeclaredAnnotation(SparkQueryParam.class))
                                .ifPresent(a -> {
                                    Object param = getParamValue(request.queryParams(a.name()), p.getType());
                                    logger.info("Param of type {} has SparkQueryParam annotation with name [{}]", p.getType(), a.name());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkHeader.class))
                                .ifPresent(a -> {
                                    Object param = getParamValue(request.headers(a.name()), p.getType());
                                    logger.info("Param of type {} has SparkHeader annotation with name [{}]", p.getType(), a.name());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkSplat.class))
                                .filter(a -> p.getType().equals(String.class))
                                .ifPresent(a -> {
                                    Object param = request.splat()[a.index()];
                                    logger.info("Param of type {} has SparkSplat annotation with index [{}]", p.getType(), a.index());
                                    params.add(param);
                                });

                        Optional.ofNullable(p.getDeclaredAnnotation(SparkBody.class))
                                .ifPresent(a -> {
                                    Object param;
                                    BodyTransformer transformer;

                                    if(a.transformer().equals(DefaultBodyTransformer.class)){
                                        transformer = bodyTransformer;
                                    }else{
                                        try {
                                            transformer = a.transformer().newInstance();
                                        } catch (InstantiationException | IllegalAccessException e) {
                                            transformer = new DefaultBodyTransformer();
                                            logger.error("Couldn't create the body transformer");
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
     * Gets back a controller by its name
     *
     * @param name  the name of the controller
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
