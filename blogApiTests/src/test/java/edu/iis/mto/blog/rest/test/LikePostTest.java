package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

    private static final String CONFIRMED_USER_LIKE_API = "/blog/user/1/like/2";
    
    private static final String OWNER_LIKE_API = "/blog/user/1/like/1";
    
    private static final String NEW_USER_LIKE_API = "/blog/user/2/like/1";
    
    @Test
    public void shouldReturnHttpStatusOkIfConfirmedUserIsLikingOtherUserPost() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post(CONFIRMED_USER_LIKE_API);
    }
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfNewUserIsLikingPost() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(NEW_USER_LIKE_API);
    }
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfOwnerIsLikingPost() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(OWNER_LIKE_API);
    }
    
}
