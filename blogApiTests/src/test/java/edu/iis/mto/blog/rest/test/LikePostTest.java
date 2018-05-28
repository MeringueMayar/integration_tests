package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest {
	private final String confirmedUserLikePost = "blog/user/1/like/3";
	private final String unconfirmedUserLikePost = "blog/user/2/like/1";
	private final String likeOwnPost = "blog/user/1/like/1";
	private final String multipleLikedPost = "blog/user/3/post";

	@Test
	public void whenConfirmedUserAddLikeToPostShouldReturnStatusOK() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post(confirmedUserLikePost);
	}

	@Test
	public void whenUnconfirmedUserAddLikeToPostShouldReturnStatusBadRequest() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
				.post(unconfirmedUserLikePost);
	}

	@Test
	public void whenUserTryToLikeOwnPostShouldReturnStatusConflict() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
				.post(likeOwnPost);
	}

	@Test
	public void whenUserMultipleLikePostStatusShouldStayTheSame() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post(confirmedUserLikePost);
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post(confirmedUserLikePost);
		RestAssured.given().when().get(multipleLikedPost).then().assertThat().body("likesCount", hasItem(1));
	}
}
