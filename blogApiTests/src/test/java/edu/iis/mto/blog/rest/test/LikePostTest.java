package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;

public class LikePostTest extends FunctionalTests {
    private static final String LIKE_OWN_POST_URL = "/blog/user/1/like/1";
    private static final String OWN_POST_URL = "/blog/user/1/post";

    private static final String LIKE_OTHERS_POST_URL = "/blog/user/1/like/2";
    private static final String ANOTHER_LIKE_OTHERS_POST_URL = "/blog/user/4/like/1";

    @Test
    public void likingAPostByAnotherUserShouldReturnOK() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(LIKE_OTHERS_POST_URL);

    }

    @Test
    public void likeOwnsPostShouldReturnBadRequest() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post(LIKE_OWN_POST_URL);
    }

    @Test
    public void likingSamePostTwiceShouldNotChangePostStatus() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(ANOTHER_LIKE_OTHERS_POST_URL);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(ANOTHER_LIKE_OTHERS_POST_URL);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").when().get(OWN_POST_URL).then().body("likesCount", hasItem(1));

    }
}
