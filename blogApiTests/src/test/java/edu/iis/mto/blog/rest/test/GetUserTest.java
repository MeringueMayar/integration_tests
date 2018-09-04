package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetUserTest extends FunctionalTests{

    @Test(expected = AssertionError.class)
    public void shouldNotReceiveResponse_whenFetchedForNonExistingUser(){
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get("blog/user/4");

        ArrayList<String> result = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=Winchester").then().extract()
                .jsonPath().getJsonObject("");

        assertThat(result.size(), Matchers.equalTo(1));
    }
}
