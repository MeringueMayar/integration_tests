package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasItem;

public class LikePostTest extends FunctionalTests {

    private static final String CONFIRMED_USER_LIKE_OWN_POST_API = "/blog/user/1/like/1";
    private static final String CONFIRMED_USER_LIKE_SOMEONES_POST_API = "/blog/user/3/like/1";
    private static final String NEW_USER_LIKE_SOMEONES_POST_API = "/blog/user/2/like/1";

    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";

    @Test
    public void confirmedUserShouldBeAbleToLikeSomeonesPost_shouldReturnOkStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);
    }


    @Test
    public void confirmedUserShouldNotBeAbleToLikeOwnPost_shouldReturnBadRequest() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post(CONFIRMED_USER_LIKE_OWN_POST_API);
    }

    @Test
    public void newUserShouldNotBeAbleToLikeSomeonesPost_shouldReturnBadRequest() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post(NEW_USER_LIKE_SOMEONES_POST_API);
    }

    @Test
    public void likingSamePostAgainShouldNotChangeItsStatus_likesCountShouldStayUnchanged() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().get(CONFIRMED_USER_POST_API).then().body("likesCount", hasItem(1));
    }
}