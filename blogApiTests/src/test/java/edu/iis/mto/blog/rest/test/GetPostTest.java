package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetPostTest extends FunctionalTests {

    @Test
    public void gettingPostsFromDeletedUsersShouldFail() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get("/blog/user/4/post");
    }

    @Test
    public void gettingPostLikesShouldReturnCorrectCount() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/2");

        int likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
                .getInt("likesCount[1]");

        assertThat(likesCount, Matchers.equalTo(1));
    }
}
