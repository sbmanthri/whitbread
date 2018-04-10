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
    public void addCustomerWith200(){

        given().log().all()
                .contentType("application/json")
                .body(customer)
                .when().post(Configuration.get("URL")).then()
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

    @Test
    public void addCustomerVerifyResponse(){

        given().log().all()
                .contentType("application/json")
                .body(customer)
                .when().post(Configuration.get("URL")).then()
                .statusCode(200)
                .contentType(ContentType.JSON);
 // Failing due to email not having @ symbol in response       
                .spec(assertPutResponse());

    }
  
    //Bug as returning 200 no eamil validation
     @Test
    public void addCustomerWithInvalidEmail(){
        given().log().all()
                .contentType("application/json")
                 .parametres("email","david1234test.com")
                .parametres("firstName","david1")
                .parametres("lastName","Miller1")
                .parameters("title","Mr")
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }
    
       @Test
    public void addCustomerWithoutFName(){
      Random ran = new Random();
        given().log().all()
                .contentType("application/json")
                .parametres("email","dyt"+ran.nextInt()+"@gmail.com")
                .parametres("lastName","Miller1")
                .parameters("title","Mr")
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }

    
        @Test
    public void addCustomerWithoutSName(){
       Random ran = new Random();
        given().log().all()
                .contentType("application/json")
                .parametres("email","dyt"+ran.nextInt()+"@gmail.com")
                .parametres("firstName","david1")
                .parameters("title","Mr")
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }
    
     @Test
    public void addCustomerWithoutEmail(){
        given().log().all()
                .contentType("application/json")
                .parametres("firstName","david1")
                .parametres("lastName","Miller1")
                .parameters("title","Mr")
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }

    //Bug title is returning as mandatory field though it is specified as optional
            @Test
    public void addCustomerWithoutTitle(){
       Random ran = new Random();
        given().log().all()
                .contentType("application/json")
                .parametres("email","dyt"+ran.nextInt()+"@gmail.com")
                .parametres("firstName","david1")
                .parametres("lastName","Miller1")
                .when().post(Configuration.get("URL")).then()
                .statusCode(200);
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
 //Success   
     @Test
    public void DeleteCustomer() throws URISyntaxException {
        given()
                .when()
                .delete(Configuration.get("URL")+ customer.get("email"))
                .then()
                .statusCode(200)
                .body("deleted", equalTo(true));
      
    }

// Bug gets 200 when retrieving deleted user
    @Test
    public void verifyDeletedCustomer() throws URISyntaxException {
        given()
                .when()
                .delete(Configuration.get("URL")+ customer.get("email"))
                .then()
                .statusCode(200)
                .body("deleted", equalTo(true));
        given()
                .when()
                .get(Configuration.get("URL")+ customer.get("email"))
                .then().log().all()
                .statusCode(400)

    }
    

}

