# Sparknnotation

Sparknnotation is a library to use Sparkjava framework with annotation. It is useful when working on projects with a lot of endpoints where it sometimes becomes messy to deal with all these Spark.something methods. It also saves the hassle of alwyas getting back parameters, query parameters and headers from the *Request* object

## How to use

### Simple GET request
Create a class with the annotation *@SparkController* and add a method with the annotation *@SparkGet*

```Java
@SparkController
public class TestController {

    @SparkGet("/hello/:name")
    public String hello(@SparkParam("name") String name){
        return "Hello "+ name;
    }
}
```

Once this is done, you just need to add this line of code in your main method (or anywhere else)

```Java
public static void main(String[] args) {
	Sparknnotation.init();
}
```
And query the server to test
```bash
$ curl http://localhost:4567/hello/world
>>> Hello world
```

An instance of each SparkController will be created and you can get it back
```Java
Sparknnotation.getController(TestController.class);
```


Note that is still possible to configure SparkJava normally or to add any other endpoint using SparkJava original way.


### Response Transformer
SparkJava offers a great way to transform the response before sending it back to the client (JSON results or templating engine), Sparknnnotation offers the same options so the method annotations take in a parameter for classes extending Spark's ResponseTransformer


To follow [SparkJava's Response Transformer example](http://sparkjava.com/documentation#response-transformer) using Sparknnotation it will be as following using the same JsonTransformer
```Java
@SparkGet(value = "/hello", transformer = JsonTransformer.class)
public MyMessage hello(){
	return new MyMessage("Hello World");
}
```


## Detailed usage
### Class annotation
#### @SparkController
Used on a class to declare a SparkController.

| Parameter | Usage |
|--------|--------|
| value (optional) | Used to identify a controller by a name  |
| path (optional) | Prefix path for every endpoints under this controller |

### Method annotations

#### @SparkGet
Used on a method to create a GET endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|

#### @SparkPost
Used on a method to create a POST endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|

#### @SparkPut
Used on a method to create a PUT endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|

#### @SparkDelete
Used on a method to create a DELETE endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|

#### @SparkOptions
Used on a method to create an OPTIONS endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|

### Method annotations

#### @SparkParam
Used on a method Parameter. Will retrieve the value of a Request.params(String)

| Parameter | Usage |
|--------|--------|
| value | The name of the parameter to retrieve |

#### @SparkQueryParam
Used on a method Parameter. Will retrieve the value of a Request.queryParams(String)

Used to get POST method form values or query strings

| Parameter | Usage |
|--------|--------|
| value | The name of the query param parameter to retrieve |

#### @SparkHeader
Used on a method Parameter. Will retrieve the value of a request header Request.headers(String)

| Parameter | Usage |
|--------|--------|
| value | The name of the header to retrieve |

#### @SparkSplat
Used on a method Parameter. Will retrieve the value of a splat form the endpoint

| Parameter | Usage |
|--------|--------|
| value | Index of splat to retrieve |

#### @SparkRequest
Used to get the standard Spark Request object

#### @SparkResponse
Used to get the standard Spark Response object


#### @SparkBody
Sparknnotation can help you convert the body of a request to a java object

For that there are two options
##### Assign a BodyTransformer when using *Sparknnotation.init()*
This way will apply the body transformer to all the @SparkBody parameters of all your controllers if not specified othewise with the *transformer* parameter of the annotation.

Example using Gson
```Java
Gson gson = new Gson();
Sparknnotation.init(gson::fromJson);
```
This works because BodyTransformer is a functional interface and Gson.fromJson(String json, Class class) fits right into it.

You can create your own transformer by implementing the BodyTransformer interface

##### Using *@SparkBody* transformer parameter

The *@SparkBody* annotation has can take a *transformer*  parameter which is a class that implements *BodyTransformer*

| Parameter | Usage |
|--------|--------|
| transformer | Class of a class inplementing *BodyTransformer* |

