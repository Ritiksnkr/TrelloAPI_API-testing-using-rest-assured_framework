package com.base;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utilities.ConfigFileReader;
import utilities.ExtentManager;

public class BaseClass {
	public static ExtentReports extent;
	public static ExtentTest test;
	protected static RequestSpecification httpRequest;

	protected static String Board_ID;
	protected static String List_ID;
	protected static String Card_ID;

	@BeforeSuite
	public void init() throws Exception {
		extent = ExtentManager.getInstance("reports/ExtentReports.html");
	}

	@BeforeMethod
	public void startTest(Method method) throws Exception {
		httpRequest = RestAssured.given().queryParam("key", ConfigFileReader.getProperty("key")).queryParam("token",
				ConfigFileReader.getProperty("token"));
		test = extent.startTest(method.getName());
	}

	@AfterMethod
	public void testResult(ITestResult result) {
		System.out.println(result.getMethod().getMethodName());
		if (result.getStatus() == ITestResult.FAILURE)
			test.log(LogStatus.FAIL, result.getThrowable());
		else if (result.getStatus() == ITestResult.SKIP)
			test.log(LogStatus.SKIP, result.getThrowable());
		else
			test.log(LogStatus.PASS, "Test Passed");
	}

	@AfterSuite
	public void reportFlush() {
		extent.flush();
	}
}
