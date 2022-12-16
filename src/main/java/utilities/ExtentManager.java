package utilities;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {

	private static ExtentReports extent;

	public static ExtentReports getInstance(String fileName) {
		if (extent == null) {
			extent = new ExtentReports(System.getProperty("user.dir") + "//ExtentReports//ExtentReportResults.html",
					true);
			extent.loadConfig(new File(System.getProperty("user.dir") + "//reportConfig.xml"));
		}
		return extent;
	}
}
