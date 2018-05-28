package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchPostTest extends FunctionalTests {

    private static final String REMOVED_USER_POST_API = "/blog/user/4/post";
    
    private static final String POST_API = "/blog/user/2/post";
    
    private static final String USER1_LIKE_API = "/blog/user/1/like/2";
    
    private static final String USER2_LIKE_API = "/blog/user/3/like/2";
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfUserIsRemoved() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.when().get(REMOVED_USER_POST_API).then().contentType(ContentType.JSON).statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    public void shouldReturnCorrectNumberOfLikes() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post(USER1_LIKE_API);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK)
                .when().post(USER2_LIKE_API);
        RestAssured.when().get(POST_API).then().contentType(ContentType.JSON).and().body("likesCount", hasItem(2));
    }
    
}
