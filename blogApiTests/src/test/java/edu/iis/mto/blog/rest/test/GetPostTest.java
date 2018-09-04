package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetPostTest extends FunctionalTests {

    @Test(expected = AssertionError.class)
    public void shouldNotReceivePost_ofDeletedUser() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get("/blog/user/4/post");
    }

    @Test(expected = AssertionError.class)
    public void shouldReceiveCorrectLikes_fromExistingPost() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/1/like/2");

        int likesNumber = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
                .getInt("likesCount[1]");

        assertThat(likesNumber, Matchers.equalTo(1));
    }
}
