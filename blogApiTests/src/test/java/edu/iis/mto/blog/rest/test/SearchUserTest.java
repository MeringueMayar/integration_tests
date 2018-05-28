package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUserTest extends FunctionalTests {

    private static final String REMOVED_USER_API = "/blog/user/4";
    
    private static final String USER_API = "/blog/user/find?searchString=domain";
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfUserIsRemoved() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.when().get(REMOVED_USER_API).then().contentType(ContentType.JSON).statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    public void shouldReturnAllNewAndConfirmedUsers() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.when().get(USER_API).then().contentType(ContentType.JSON).and().body("id", not(hasItem(4)));
    }
    
}
