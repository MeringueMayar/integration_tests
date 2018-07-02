package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class PostLikeTest extends  FunctionalTests{
    private static final String NON_OWN_LIKE_POST_REQUEST = "/blog/user/3/like/1";
    private static final String LIKE_OWN_POST_REQUEST = "/blog/user/1/like/1";
    
    @Test
    public void likingNonOwnedPostShouldBeOK() {
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(NON_OWN_LIKE_POST_REQUEST);
    }

    @Test
    public void likingOwnsPostShouldReturnBadRequest() {
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post(LIKE_OWN_POST_REQUEST);
    }
}
