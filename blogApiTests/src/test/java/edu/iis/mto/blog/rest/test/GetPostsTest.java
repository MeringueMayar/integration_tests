package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class GetPostsTest extends FunctionalTests {

    private static final String REMOVED_USER_POST_API = "/blog/user/4/post";
    private static final String CONFIRMED_USER_OTHER_POST_2_LIKE_API = "/blog/user/3/like/2";
    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";

    @Test
    public void gettingPostsFromDeletedUsersShouldFail() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get(REMOVED_USER_POST_API);
    }

    @Test
    public void gettingPostLikesShouldReturnCorrectCount() {

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post(CONFIRMED_USER_OTHER_POST_2_LIKE_API);
        int likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get(CONFIRMED_USER_POST_API).then().extract().jsonPath()
                .getInt("likesCount[1]");
        Assert.assertThat(likesCount, Matchers.equalTo(1));

    }
}
