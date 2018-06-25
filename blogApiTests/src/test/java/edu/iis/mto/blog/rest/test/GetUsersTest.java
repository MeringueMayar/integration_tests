package edu.iis.mto.blog.rest.test;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class GetUsersTest {

    private static final String REMOVED_USER_API = "/blog/user/4";

    @Test
    public void gettingRemovedUsersDataShouldFail() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get(REMOVED_USER_API);

        ArrayList<String> result = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=Winchester").then().extract()
                .jsonPath().getJsonObject("");
        Assert.assertThat(result.size(), Matchers.equalTo(1));

    }

}
