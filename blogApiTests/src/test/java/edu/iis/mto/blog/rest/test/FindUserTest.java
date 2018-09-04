package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class FindUserTest extends FunctionalTests {
    private static final String USER_LOOKUP_BASE = "/blog/user/";

    @Test
    public void ShouldReturn404WhenTryingToFindNonExistingUser()
    {
        JSONObject object = new JSONObject();
        String userId = "0";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(object.toString()).expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get(USER_LOOKUP_BASE + userId);
    }
    @Test
    public void shouldReturnOKWhenAUserWithValidIDisRequested() {
        JSONObject jsonObj = new JSONObject();
        char userId = '1';
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().get(USER_LOOKUP_BASE + userId);
    }
}
