package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;


public class SearchingPostTest {
	private final String removedUser="/blog/user/4/post";
	@Test
	public void whenUserStatusRemovedSearchingShouldReturnBadRequest() {
		RestAssured.when().get(removedUser).then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}
	
}
