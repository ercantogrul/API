package contactlist_user;

import base_urls.ContactListBaseUrl;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojos.contactList_UsersPojo.User;
import pojos.contactList_UsersPojo.UserPojo;
import pojos.contactList_UsersPojo.UserResponsePojo;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class R01_CreateUser extends ContactListBaseUrl {
    /*
    Given
        1) https://thinking-tester-contact-list.herokuapp.com/users
        2) {
                "firstName": "John",
                "lastName": "Doe",
                "email": "john@doe.com",
                "password": "John.123"
            }
    When
        User sends post request
    Then
        Status code should be 201
    And
        Response body should be like:
        {
          "user": {
            "_id": "608b2db1add2691791c04c89",
            "firstName": "John",
            "lastName": "Doe",
            "email": "test@fake.com",
            "__v": 1
          },
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDhiMmRiMWFkZDI2OTE3OTFjMDRjODgiLCJpYXQiOjE2MTk3MzM5Mzd9.06wN8dRBLkFiS_m2XdY6h4oLx3nMeupHvv-3C2AEKlY"
        }
     */

    public static User createdUser;  //Get user yaparken bunu expected data olarak kullanabiliriz.
    public static UserPojo expectedData;// Authentication icin burada olusturulan email ve password 'e ulasmak icin
    public static UserResponsePojo actualData;

    @Test
    void createUserTest(){

        //Set the url
        spec.pathParams("first","users");
//______________________________________________________________________________________
        //Set the expected data
        String json = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@doe.com",
                    "password": "John.123"
                }""";

        expectedData = ObjectMapperUtils.jsonToJava(json, UserPojo.class);
        expectedData.setEmail(Faker.instance().internet().emailAddress());
        System.out.println("expectedData = " + expectedData);
//______________________________________________________________________________________
        //Send the request and get the response
        Response response = given(spec).body(expectedData).post("{first}");
        response.prettyPrint();
//______________________________________________________________________________________
        //Do assertion
         actualData = response.as(UserResponsePojo.class);
        assertEquals(response.statusCode(), 201);
        assertEquals(actualData.getUser().getFirstName(), expectedData.getFirstName());
        assertEquals(actualData.getUser().getLastName(), expectedData.getLastName());
        assertEquals(actualData.getUser().getEmail(), expectedData.getEmail());

        createdUser=actualData.getUser();  //Diğer classlarda kullanabilmek için class variable'a assign ediyoruz.


    }
}