package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchPostTest extends FunctionalTests {

    private static final String POST_API = "/blog/user/4/post";
    
    @Test
    public void shouldReturnHttpStatusBadRequestIfUserIsRemoved() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.when().get(POST_API).then().contentType(ContentType.JSON).statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
}
