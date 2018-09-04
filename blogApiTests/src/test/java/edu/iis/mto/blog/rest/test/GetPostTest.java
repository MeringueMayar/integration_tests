package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasItem;

public class GetPostTest extends FunctionalTests {

    private static final String REMOVED_USER_POST_API = "/blog/user/5/post";
    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";
    private static final String CONFIRMED_USER_LIKE_SOMEONES_POST_API = "/blog/user/3/like/1";
    private static final String CONFIRMED_USER2_LIKE_SOMEONES_POST_API = "/blog/user/4/like/1";


    @Test
    public void shouldReturnOkStatusWhenTryingToGetPostsOfConfirmedUser() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().get(CONFIRMED_USER_POST_API);
    }


    @Test
    public void shouldReturnBadRequestWhenTryingToGetPostsOfRemovedUser() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get(REMOVED_USER_POST_API);
    }


    @Test
    public void shouldReturnCorrectLikesCountOfPostLikedTwice() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER2_LIKE_SOMEONES_POST_API);

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().get(CONFIRMED_USER_POST_API).then().body("likesCount", hasItem(2));
    }
}