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

public class TrelloCard extends BaseClass {
	private static RestClientWrapper restClient;
	private static String baseUrl;

	private static Logger logger = LogManager.getLogger(TrelloCard.class);

	String cardName = ConfigFileReader.getProperty("boardName");
	String cardDesc = ConfigFileReader.getProperty("boardDescription");

	@BeforeClass
	public void setupTest() throws Exception {
		baseUrl = ConfigFileReader.getProperty("baseURI");
		System.out.print(baseUrl);
	}

	@BeforeMethod
	public void setupRequest() {

		restClient = new RestClientWrapper(baseUrl, httpRequest);
	}

	@Test(priority = 8)
	public void CreateCard() throws Exception {

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", cardName)
				.queryParam("idList", List_ID).when().post(Resources.Card).then().log().all().assertThat()
				.statusCode(200).extract().response();
		res.prettyPeek();
		JsonPath jp = Utils.rawToJson(res);
		Card_ID = jp.get("id");
		String name = jp.get("name");
		Assert.assertEquals(name, cardName);
		logger.info("Card Created Scuccesfully");
	}

	@Test(priority = 9)
	public void GetCard() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", cardName)
				.queryParam("idList", List_ID).when().get(Resources.Card + Card_ID).then().log().all().assertThat()
				.statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String name = jp.get("name");
		Assert.assertEquals(name, cardName);
		logger.info("Card Fetched Scuccesfully");
	}

	@Test(priority = 10)
	// Negative Scenario
	public void UpdateCard_NegativeScenario() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("idList", List_ID).when()
				.put(Resources.Card + Card_ID).then().log().all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String desc = jp.get("desc");
		Assert.assertEquals(desc, cardDesc);
		logger.info("Card could not updated! No parameter passed");
	}

	@Test(priority = 11)
	public void UpdateCard() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("desc", cardDesc)
				.queryParam("color", "yellow").queryParam("idList", List_ID).when().put(Resources.Card + Card_ID).then()
				.log().all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String desc = jp.get("desc");
		Assert.assertEquals(desc, cardDesc);
		logger.info("Card Updated Scuccesfully");
	}

	@Test(priority = 12)
	public void DeleteCard() throws Exception {
		Response res = httpRequest.log().all().when().delete(Resources.Card + Card_ID).then().log().all().assertThat()
				.statusCode(200).extract().response();
		res.prettyPeek();

		int statuscode = res.getStatusCode();
		Assert.assertEquals(statuscode, 200);
		logger.info("Card Deleted Scuccesfully");
	}

	@Test(priority = 13)
	public void DeleteBoard() throws Exception {
		Response res = httpRequest.log().all().when().delete(Resources.Board + Board_ID).then().log().all().assertThat()
				.statusCode(200).extract().response();
		res.prettyPeek();

		int statuscode = res.getStatusCode();

		Assert.assertEquals(statuscode, 200);
		logger.info("Board Deleted Scuccesfully");
	}
}
