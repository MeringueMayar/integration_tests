package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest {
	private final String confirmedUserLikePost = "blog/user/1/like/2";
	private final String unconfirmedUserLikePost = "blog/user/3/like/2";
	@Test
	public void whenConfirmedUserAddLikeToPostShouldReturnStatusOK() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post(confirmedUserLikePost);
	}
	@Test
    public void shouldReturnHttpStatusConflictIfUnonfirmedUserIsLikingOtherUserPost() {
        JSONObject jsonObj = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(unconfirmedUserLikePost);
    }
}
