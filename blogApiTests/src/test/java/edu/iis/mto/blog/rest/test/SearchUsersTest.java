package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class SearchUsersTest {
    private static final String BASE_URL = "/blog/user/";

    @Test
    public void shouldntReturnNonExistedUser() {
        JSONObject jsonObj = new JSONObject();
        String userId = "0";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get(BASE_URL + userId);
    }

    @Test
    public void shouldReturnOKWhenAUserWithValidIDisRequested() {
        JSONObject jsonObj = new JSONObject();
        String userId = "1";
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().get(BASE_URL + userId);
    }
}
