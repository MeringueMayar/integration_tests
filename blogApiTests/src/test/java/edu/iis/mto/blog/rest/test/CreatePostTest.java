package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest {
	private final String confirmedUser = "blog/user/1/post";
	private final String unconfirmedUser = "blog/user/3/post";

	@Test
	public void whenConfirmedUserAddingPostShouldReturnStatusConfirmed() {
		JSONObject jsonObj = new JSONObject().put("entry", "post");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post(confirmedUser);
	}

	@Test
	public void whenUnconfirmedUserAddingPostShouldReturnStatusConflict() {
		JSONObject jsonObj = new JSONObject().put("entry", "post");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post(unconfirmedUser);
	}
}
