package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class SearchingUserTest {
	private final String getAllUsers = "/blog/user/find?searchString=@";

	@Test
	public void testShouldReturnAllUsersExceptRemoved() {
		RestAssured.given().get(getAllUsers).then().body("id", not(hasItem(4)));
	}
}
