package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class FindUserTest extends FunctionalTests {

    @Test
    public void removedUsersAreNotFound() {
        JsonPath request = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=.eu")
                .then().extract().jsonPath();
        int userCount = request.getInt("$.size()");
        Assert.assertThat(userCount, equalTo(4));
    }
}