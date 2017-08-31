import com.ftpix.sparknnotation.Sparknnotation;
import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;
import spark.Spark;

@SparkController
public class TestController {


    @SparkGet(path = "/hello/:name")
    public String hello(@SparkParam(name = "name") String name){
        return "Hello "+ name;
    }

    public static void main(String[] args) {
        Spark.port(14567);
        Sparknnotation.init();
    }
}
