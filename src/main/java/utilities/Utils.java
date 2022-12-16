package utilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Utils {

	public static JsonPath rawToJson(Response res) {
		String response = (res).asString();
		JsonPath x = new JsonPath(response);
		return x;
	}

}
