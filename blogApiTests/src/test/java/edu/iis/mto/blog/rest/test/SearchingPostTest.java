package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchingPostTest {
	private final String removedUser = "/blog/user/4/post";
	private final String likePost1 = "/blog/user/1/like/2";
	private final String likePost2 = "/blog/user/3/like/2";
	private final String countOfLikes = "/blog/user/2/post";

	@Test
	public void whenUserStatusRemovedSearchingShouldReturnBadRequest() {
		RestAssured.when().get(removedUser).then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testShouldReturnCorrectCountOfLikes() {
		JSONObject jsonObj = new JSONObject();
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(likePost1);
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when().post(likePost2);
		RestAssured.given().when().get(countOfLikes).then().assertThat().body("likesCount", hasItem(2));
	}
}
