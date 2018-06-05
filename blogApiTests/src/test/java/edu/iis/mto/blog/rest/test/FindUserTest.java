package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class FindUserTest extends FunctionalTests {
    private static final String USER_FIND_BASE = "/blog/user/find?searchString=";

    @Test
    public void shouldReturnAUserGivenValidQuery() {
        JSONObject jsonObj = new JSONObject();
        String searchString = "Steward";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).and().body("size()", is(1)).when().get(USER_FIND_BASE + searchString);
    }

    @Test
    public void shouldNotReturnAUserGivenInvalidQuery() {
        JSONObject jsonObj = new JSONObject();
        String searchString = "Nobody";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).and().body("size()", is(0)).when().get(USER_FIND_BASE + searchString);
    }

    @Test
    public void shouldNotReturnAUserGivenQueryForRemovedUser() {
        JSONObject jsonObj = new JSONObject();
        String searchString = "Removed";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).and().body("size()", is(0)).when().get(USER_FIND_BASE + searchString);
    }

}
