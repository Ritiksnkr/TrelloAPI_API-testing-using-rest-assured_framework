package com.Trello;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.base.BaseClass;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utilities.ConfigFileReader;
import utilities.Resources;
import utilities.RestClientWrapper;
import utilities.Utils;

public class TrelloList extends BaseClass {

	private static RestClientWrapper restClient;
	private static String baseUrl;

	private static Logger logger = LogManager.getLogger(TrelloList.class);

	String listName = ConfigFileReader.getProperty("listName");
	String UpdatedListName = ConfigFileReader.getProperty("UpdatedListName");

	@BeforeClass
	public void setupTest() throws Exception {
		baseUrl = ConfigFileReader.getProperty("baseURI");
		System.out.print(baseUrl);
	}

	@BeforeMethod
	public void setupRequest() {

		restClient = new RestClientWrapper(baseUrl, httpRequest);
	}

	@Test(priority = 5)
	// Negative Scenario
	public void createList_NegativeScenario() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", listName).when()
				.post(Resources.List + Board_ID + "/lists").then().log().all().assertThat().statusCode(200).extract()
				.response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		List_ID = jp.get("id");
		String name = jp.get("name");
		System.out.println(List_ID);
		Assert.assertEquals(name, listName);
		logger.info("List could not created due to incorrect invalid input");

	}

	@Test(priority = 6)
	public void createList() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", listName).when()
				.post(Resources.Board + Board_ID + "/lists").then().log().all().assertThat().statusCode(200).extract()
				.response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		List_ID = jp.get("id");
		String name = jp.get("name");
		System.out.println(List_ID);
		Assert.assertEquals(name, listName);
		logger.info("List Created Scuccesfully", "List ID is =", List_ID);

	}

	@Test(priority = 7)
	public void GetList() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).when().get(Resources.List + List_ID).then().log().all()
				.assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		List_ID = jp.get("id");
		String name = jp.get("name");
		System.out.println(List_ID);
		Assert.assertEquals(name, listName);
		logger.info("List Fetched Scuccesfully", "List ID is =", List_ID, "List Name is =", name);

	}

	@Test(priority = 12)
	public void updateList() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", UpdatedListName).when()
				.put(Resources.List + List_ID).then().log().all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String desc = jp.get("name");
		Assert.assertEquals(desc, UpdatedListName);
		logger.info("List Updated Scuccesfully", "Updated List Name is =", UpdatedListName);

	}

}
