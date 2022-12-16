package utilities;

import io.restassured.specification.RequestSpecification;

public class RestClientWrapper {
	public String resource;
	public String baseUrl;
	private RequestSpecification request;
	String boardName = ConfigFileReader.getProperty("boardName");
	String boardDesc = ConfigFileReader.getProperty("boardDescription");

	public RestClientWrapper(String baseUrl, RequestSpecification request) {
		this.request = request;
		this.request.baseUri(baseUrl);
	}
}
