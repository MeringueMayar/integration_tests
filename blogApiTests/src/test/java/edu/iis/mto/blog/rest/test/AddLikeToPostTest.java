package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class AddLikeToPostTest extends FunctionalTests {

    @Test
    public void confirmedUserLikeSecondUserPost() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).body(is("true")).when().post("/blog/user/3/like/1");
    }
}
