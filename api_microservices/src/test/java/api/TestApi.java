package api;

import com.test.Configuration;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestApi {

    private static Map<String, String> customer = new HashMap<String, String>();

    @Before
    public void createData(){
        Random ran = new Random();
        customer.put("email", "dyt"+ran.nextInt()+"@gmail.com");
        customer.put("firstName", "David"+ran.nextInt());
        customer.put("lastName", "Allen"+ran.nextInt());
        customer.put("password", "a2q4y4Qu");
        customer.put("title", "Mr");
    }

    private ResponseSpecification assertPutResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectBody("statusCode",equalTo(200));
        builder.expectBody("body.customerId", equalTo(customer.get("email")));
        builder.expectBody("body.success", equalTo(true));
        builder.expectBody("body.sessionId", notNullValue());

        return builder.build();
    }

    private ResponseSpecification assertGetResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectBody("customerId", equalTo(customer.get("email")));
        return builder.build();
    }


    @Test
    public void addCustomer(){

        given().log().all()
                .contentType("application/json")
                .body(customer)
                .when().post(Configuration.get("URL")).then()
                .statusCode(200)
                .contentType(ContentType.JSON);
//                .spec(assertPutResponse());


    }

    @Test
    public void GetCustomer() {

        given()
                .when()
                .contentType("application/json")
                .body(customer)
                .post(Configuration.get("URL"))
                .then().log().all()
                .statusCode(200);

        given()
                .when()
                .get(Configuration.get("URL")+ customer.get("email"))
                .then().log().all()
                .statusCode(200)
                .spec(assertGetResponse());

    }


    @Test
    public void DeleteCustomer() throws URISyntaxException {
        given()
                .when()
                .delete(Configuration.get("URL")+ customer.get("email"))
                .then()
                .statusCode(200)
                .body("deleted", equalTo(true));

    }

}

