package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;

public class LikePostTest extends FunctionalTests {

    private static final String CONFIRMED_USER_LIKE_API = "/blog/user/1/like/2";
    
    private static final String OWNER_LIKE_API = "/blog/user/1/like/1";
    
    private static final String NEW_USER_LIKE_API = "/blog/user/2/like/1";
    
    private static final String LIKE_API = "/blog/user/1/like/3";
    
    private static final String POST_API = "/blog/user/3/post";
    
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
    
    @Test
    public void shouldNotChangePostStatusAfterSecondLike() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post(LIKE_API);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post(LIKE_API);
        RestAssured.when().get(POST_API).then().contentType(ContentType.JSON).and().body("likesCount", hasItem(1));
    }
    
}
