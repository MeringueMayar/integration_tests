package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class GetUserTest extends FunctionalTests {

    private static final String FIND_USER_API = "/blog/user/find";
    private static final String REMOVED_USER_MAIL = "mark@domain.com";

    @Test
    public void shouldNotReturnUsersWithRemovedStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "@").expect().log().all().statusCode(HttpStatus.SC_OK).when().get(FIND_USER_API).then().body("email", not(hasItem(REMOVED_USER_MAIL)));
    }
}