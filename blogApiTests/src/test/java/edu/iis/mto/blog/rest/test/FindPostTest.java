package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class FindPostTest extends FunctionalTests {

    @Test
    public void findRemovedUserPostsReturnNotFound() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get("/blog/user/4/post");
    }

    @Test
    public void findUserPosts() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/2");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/5/like/2");

        JsonPath request = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post")
                .then().extract().jsonPath();

        String entry = request.getString("entry[1]");
        Assert.assertThat(entry, equalTo("Post nr 2"));
        int likesCount = request.getInt("likesCount[1]");
        Assert.assertThat(likesCount, equalTo(2));
        int postCount = request.getInt("$.size()");
        Assert.assertThat(postCount, equalTo(3));
    }
}
