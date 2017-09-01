# Sparknotation

Sparknotation is a library to use Sparkjava framework with annotation. It is useful when working on projects with a lot of endpoints where it sometimes becomes messy to deal with all these Spark.something methods. It also saves the hassle of alwyas getting back parameters, query parameters and headers from the *Request* object.

## Download

Add this repository to your pom.xml

```xml
<repositories>
    <repository>
        <id>sparknotation</id>
        <url>https://raw.github.com/lamarios/sparknnotation/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

and the dependency as follow

```xml
<dependency>
	<groupId>com.ftpix</groupId>
    <artifactId>sparknotation</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

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

Once this is done, you just need to add this line of code in your main method or wherever you would usually declare your Spark endpoints.

```Java
public static void main(String[] args) {
	Sparknotation.init();
}
```
And query the server to test
```bash
$ curl http://localhost:4567/hello/world
>>> Hello world
```

An instance of each SparkController will be created and you can get it back
```Java
Sparknotation.getController(TestController.class);
```


Note that is still possible to configure SparkJava normally or to add any other endpoint using SparkJava original way.


### Response Transformer
SparkJava offers a great way to transform the response before sending it back to the client (JSON results or templating engine), Sparknnnotation offers the same options so the method annotations take in a parameter for classes extending Spark's ResponseTransformer


To follow [SparkJava's Response Transformer example](http://sparkjava.com/documentation#response-transformer) using Sparknotation it will be as following using the same JsonTransformer
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
| accept (optional) | the type of request this endpoint should respond to |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|
| templateEngine (optional) | A template engine, works the same way as Spark. **note that templateEngine has the priority against transformer if both are set**|

#### @SparkPost
Used on a method to create a POST endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| accept (optional) | the type of request this endpoint should respond to |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|
| templateEngine (optional) | A template engine, works the same way as Spark. **note that templateEngine has the priority against transformer if both are set**|

#### @SparkPut
Used on a method to create a PUT endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| accept (optional) | the type of request this endpoint should respond to |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|
| templateEngine (optional) | A template engine, works the same way as Spark. **note that templateEngine has the priority against transformer if both are set**|

#### @SparkDelete
Used on a method to create a DELETE endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| accept (optional) | the type of request this endpoint should respond to |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|
| templateEngine (optional) | A template engine, works the same way as Spark. **note that templateEngine has the priority against transformer if both are set**|

#### @SparkOptions
Used on a method to create an OPTIONS endpoint

| Parameter | Usage |
|--------|--------|
| value (optional) | the path of the endpoint |
| accept (optional) | the type of request this endpoint should respond to |
| transformer (optional) | A response transformer to transform the result of the method (example: return a json of an object)|
| templateEngine (optional) | A template engine, works the same way as Spark. **note that templateEngine has the priority against transformer if both are set**|

#### Combining methods
You can combine the different methods on a single method

```Java
@SparkGet("/hello")
@SparkPost("/hello")
public String hello(){
	return hello;
}
```

### Method annotations

#### @SparkParam
Used on a method Parameter. Will retrieve the value of a Request.params(String)

| Parameter | Usage |
|--------|--------|
| value | The name of the parameter to retrieve |


```Java
@SparkGet("/hello/:name")
public String hello(@SparkParam("name") String name){
	return "Hello " + name;
}
```


#### @SparkQueryParam
Used on a method Parameter. Will retrieve the value of a Request.queryParams(String)

Used to get POST method form values or query strings

| Parameter | Usage |
|--------|--------|
| value | The name of the query param parameter to retrieve |

```Java
@SparkPost
public String hello(@SparkQueryParam("name") String name){
	return name;
}
```

```bash
curl -d "name=world" http://localhost:4567/hello
>>> world
```

#### @SparkHeader
Used on a method Parameter. Will retrieve the value of a request header Request.headers(String)

| Parameter | Usage |
|--------|--------|
| value | The name of the header to retrieve |

```Java
@SparkBefore("/*")
public void auth(@SparkHeader("Authorization") String token){
	// do something
}
```

#### @SparkSplat
Used on a method Parameter. Will retrieve the value of a splat form the endpoint

| Parameter | Usage |
|--------|--------|
| value (defaults to 0)| Index of splat to retrieve (starting from 0) |

```Java
@SparkGet("/my-path/*/something/*")
public String hello(@SparkSplat(1) String secondSplat, @SparkSplat String firstSplat){
	return firstSplat+" "+secondSplat;
}
```


#### @SparkBody
Sparknotation can help you convert the body of a request to a java object

For that there are two options
##### Assign a BodyTransformer when using *Sparknotation.init()*
This way will apply the body transformer to all the @SparkBody parameters of all your controllers if not specified othewise with the *transformer* parameter of the annotation.

Example using Gson
```Java
Gson gson = new Gson();
Sparknotation.init(gson::fromJson);
```
This works because BodyTransformer is a functional interface and Gson.fromJson(String json, Class class) fits right into it.

You can create your own transformer by implementing the BodyTransformer interface.

```Java
@SparkPost
public String hello(@SparkBody MyObject myObject){
	//use myObject
}
```

##### Using *@SparkBody* transformer parameter

The *@SparkBody* annotation has can take a *transformer*  parameter which is a class that implements *BodyTransformer*

| Parameter | Usage |
|--------|--------|
| value | Class of a class inplementing *BodyTransformer* |

```Java
@SparkPost
public String hello(@SparkBody(MyBodyTransformer.class) MyObject myObject){
	//use myObject
}
```

### Request and Response objects

You can easily get back the Request and Response objects from Spark if needed, simply add it to your method parameters

```Java
@SparkGet
public String hello(Request req,  Response res){
	//Do something with request and response
	return "hello";
}
```

