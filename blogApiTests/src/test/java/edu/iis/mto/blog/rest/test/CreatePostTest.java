package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {

    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";
    
    private static final String NEW_USER_POST_API = "/blog/user/2/post";
    
    @Test
    public void shouldReturnHttpStatusCreatedIfConfirmedUserIsCreatingPost() {
        JSONObject jsonObj = new JSONObject().put("entry", "test");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED)
                .when().post(CONFIRMED_USER_POST_API);
    }
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfNewUserIsCreatingPost() {
        JSONObject jsonObj = new JSONObject().put("entry", "test");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(NEW_USER_POST_API);
    }
    
}
