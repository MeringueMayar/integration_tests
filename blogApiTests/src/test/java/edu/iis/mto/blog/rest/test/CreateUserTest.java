package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    @Test
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when().post(USER_API);
    }

    @Test
    public void userHaveToUniqueEmail() {
        final String email = "wspolny@mail.pl";
        JSONObject user1 = new JSONObject().put("accountStatus", "NEW")
                .put("firstName", "Bogdan")
                .put("lastName", "Wegierski");
        JSONObject user2 = new JSONObject().put("accountStatus", "NEW")
                .put("firstName", "Krzysztof")
                .put("lastName", "Awokado");
        user1.put("email", email);
        user2.put("email", email);

        RestAssured.given()
                .accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(user1.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED)
                .when().post("/blog/user");
        RestAssured.given()
                .accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(user2.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT)
                .when().post("/blog/user");
    }
}
