package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreatePostTest extends FunctionalTests {
    @Test
    public void confirmedUserCanAddPost() {
        JSONObject jsonObject1 = new JSONObject().put("entry", "bla bla bla");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject1.toString()).expect().log().body().statusCode(HttpStatus.SC_CREATED)
                .when().post("/blog/user/1/post");
    }

    @Test
    public void notConfirmedUserCanNotAddPost() {
        JSONObject jsonObject2 = new JSONObject().put("entry", "bla bla ble");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject2.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("/blog/user/2/post");
    }

}
