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

public class TrelloBoard extends BaseClass {
	private static Logger logger = LogManager.getLogger(TrelloBoard.class);

	private static RestClientWrapper restClient;
	private static String baseUrl;

	String boardName = ConfigFileReader.getProperty("boardName");
	String boardDesc = ConfigFileReader.getProperty("boardDescription");

	@BeforeClass
	public void setupTest() throws Exception {
		baseUrl = ConfigFileReader.getProperty("baseURI");
		System.out.print(baseUrl);
	}

	@BeforeMethod
	public void setupRequest() {

		restClient = new RestClientWrapper(baseUrl, httpRequest);
	}

	@Test(priority = 1)
	public void CreateBoard() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		Response res = httpRequest.log().all().headers(headers).queryParam("name", boardName).when()
				.post(Resources.Board).then().log().all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String name = jp.get("name");
		Board_ID = jp.get("id");
		Assert.assertEquals(name, boardName);
		logger.info("Board Created Scuccesfully");
	}

	@Test(priority = 2)
	public void GetBoard() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		Response res = httpRequest.log().all().headers(headers).when().get(Resources.Board + Board_ID).then().log()
				.all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String name = jp.get("name");
		Assert.assertEquals(name, boardName);
		logger.info("Board Fetched Succesfully");
	}

	@Test(priority = 3)
	public void UpdateBoard() throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		Response res = httpRequest.log().all().headers(headers)
				.queryParam("name", ConfigFileReader.getProperty("UpdateBoardName"))
				.queryParam("desc", ConfigFileReader.getProperty("boardDescription")).when()
				.put(Resources.Board + Board_ID).then().log().all().assertThat().statusCode(200).extract().response();
		res.prettyPeek();

		JsonPath jp = Utils.rawToJson(res);
		String desc = jp.get("desc");
		Assert.assertEquals(desc, boardDesc);
		logger.info("Update Board Succesfully");
	}

	@Test(priority = 4)
	// Negative Scenario
	public void DeleteBoard_NegativeScenario() throws Exception {
		Response res = httpRequest.log().all().when().delete(Resources.Board).then().log().all().assertThat()
				.statusCode(200).extract().response();
		res.prettyPeek();

		int statuscode = res.getStatusCode();

		Assert.assertEquals(statuscode, 200);
		logger.info("Board could not be deleted due to incorrect input data");
	}
}
