package edu.iis.mto.blog.rest.test;

import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;

public class HttpRepliesTests extends FunctionalTests {

    private static final String USER_API = "/blog/user";
    private static final String CONFIRMED_USER_POST = "/blog/user/1/post";
    private static final String NEW_USER_POST = "/blog/user/2/post";

    public static final String CONFIRMED_USER_NOT_OWNER_OF_POST = "/blog/user/3/like/1";
    public static final String NEW_USER_NOT_OWNER_OF_POST = "/blog/user/2/like/1";
    public static final String CONFIRMED_USER_OWNER_OF_POST = "/blog/user/1/like/1";

    @Test
    public void postFormWithMalformedRequestDataReturnsConflict() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_CONFLICT)
                .when().post(USER_API);
    }

    @Test
    public void postFormWithCorrectRequestDataReturnsCreated() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_CREATED)
                .when().post(USER_API);
    }

    @Test
    public void getFormWithNonExistingUserRequestDataReturnsNotFound() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_NOT_FOUND)
                .when().get("/blog/user/9");
    }

    @Test
    public void postFormWithDuplicateEmailRequestDataReturnsConflict() {
        JSONObject jsonObj1 = new JSONObject().put("email", "duplicate@domain.com");
        JSONObject jsonObj2 = new JSONObject().put("email", "duplicate@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj1.toString()).expect().log().all().statusCode(SC_CREATED)
                .when().post(USER_API);
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj2.toString()).expect().log().all().statusCode(SC_CONFLICT)
                .when().post(USER_API);
    }

    @Test
    public void postFormByUserWithConfirmedAccountStatusReturnsCreated() {
        JSONObject jsonObj = new JSONObject().put("entry", "entry");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_CREATED)
                .when().post(CONFIRMED_USER_POST);
    }

    @Test
    public void postFormByUserWithNewAccountStatusReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("entry1", "entry1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_BAD_REQUEST)
                .when().post(NEW_USER_POST);
    }

    @Test
    public void likingPostByNewUserReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(SC_BAD_REQUEST).when()
                .post(NEW_USER_NOT_OWNER_OF_POST);
    }

}
