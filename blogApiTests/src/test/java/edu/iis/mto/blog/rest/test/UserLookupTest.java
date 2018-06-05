package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class UserLookupTest extends FunctionalTests {
    private static final String USER_LOOKUP_BASE = "/blog/user/";

    @Test
    public void shouldReturnNotFoundWhenAUserWithNonexisitingIDisRequested() {
        JSONObject jsonObj = new JSONObject();
        String invalidUserId = "0";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get(USER_LOOKUP_BASE + invalidUserId);
    }
}
