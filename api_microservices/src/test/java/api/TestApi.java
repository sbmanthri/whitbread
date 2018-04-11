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
      private static Map<String, String> missingTitle = new HashMap<String, String>();
          private static Map<String, String> invalidEmail = new HashMap<String, String>();
     private static Map<String, String> noFname = new HashMap<String, String>();
     private static Map<String, String> noLname = new HashMap<String, String>();
      private static Map<String, String> missingEmail = new HashMap<String, String>();
    

    @Before
    public void createData(){
        Random ran = new Random();
        customer.put("email", "dyt"+ran.nextInt()+"@gmail.com");
        customer.put("firstName", "David"+ran.nextInt());
        customer.put("lastName", "Allen"+ran.nextInt());
        customer.put("password", "a2q4y4Qu");
        customer.put("title", "Mr");
    }
      @Before
    public void dataMissingFname(){
        Random ran = new Random();
        cnoFnamer.put("email", "dyt"+ran.nextInt()+"@gmail.com");
         noFname.put("lastName", "Allen"+ran.nextInt());
        noFname.put("password", "a2q4y4Qu");
        noFname.put("title", "Mr");
    }
    ublic void dataMissingLname(){
        Random ran = new Random();
         noLname.put("email", "dyt"+ran.nextInt()+"@gmail.com");
         noLname.put("lastName", "Allen"+ran.nextInt());
         noLname.put("password", "a2q4y4Qu");
         noLname.put("title", "Mr");
    }
    
     @Before
    public void invalidEmailData()
       invalidEmail.put("email", "dyt123gmail.com");
        invalidEmail.put("firstName", "David"+ran.nextInt());
        invalidEmail.put("lastName", "Allen"+ran.nextInt());
        invalidEmail.put("password", "a2q4y4Qu");
       invalidEmail.put("title", "Mr");
    }

@Before
    public void missingEmailData()
       missingEmail.put("firstName", "David"+ran.nextInt());
        missingEmail.put("lastName", "Allen"+ran.nextInt());
        missingEmail.put("password", "a2q4y4Qu");
      missingEmail.put("title", "Mr");
    }
    @Before
    public void missingTitle(){
         missingTitle.put("email":"david123@sfsf.fr");
         missingTitle.put("password":"Qwr24234");
         missingTitle.put("firstName":"David");
         missingTitle.put("lastName":"Allan");
    }

    private ResponseSpecification assertPutResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectBody("statusCode",equalTo(200));
        builder.expectBody("body.customerId", equalTo("^(.+)(.+)$"));
        builder.expectBody("body.success", equalTo(true));
        builder.expectBody("body.sessionId", notNullValue());

        return builder.build();
    }

    private ResponseSpecification assertGetResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectBody("customerId", equalTo(customer.get("email")));
        return builder.build();
    }
//Bug status code is 200 OK but but response body getting Error some times not always
// "errorMessage": "Error! Database insertion crashed, looks like we have a bug here",
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
                .spec(assertPutResponse());

    }
  
    //Bug as returningStatus code 200, no eamil validation can accept without @ and .com
     @Test
    public void addCustomerWithInvalidEmail(){
        given().log().all()
                .contentType("application/json")
                 .body(invalidEmailData)
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }
   //Bug first name, last name being mandatory filed still gives 200 code when not passed 
       @Test
    public void addCustomerWithoutFName(){
      Random ran = new Random();
        given().log().all()
                .contentType("application/json")
                .body(noFname)
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }

    //Bug first name, last name being mandatory filed still gives 200 code when not passed  
        @Test
    public void addCustomerWithoutSName(){
       Random ran = new Random();
        given().log().all()
                .contentType("application/json")
                .body( noLname)
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }
    //Bug email being mandatory filed still gives 200 code when not passed  
     @Test
    public void addCustomerWithoutEmail(){
        given().log().all()
                .contentType("application/json")
                .body(missingEmail)
                .when().post(Configuration.get("URL")).then()
                .statusCode(400);
           }

    //Bug title is returning as mandatory field though it is specified as optional
   // "errorMessage": "Error! Database insertion crashed, looks like we have a bug here", 
            @Test
    public void addCustomerWithoutTitle(){

        given().log().all()
                .contentType("application/json")
                .body(missingTitle),
                .when().post(Configuration.get("URL")).then()
                .statusCode(200);
           }
//Bug All get calls with any garbage endpoint gives 200 
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

