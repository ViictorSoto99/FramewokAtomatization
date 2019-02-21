package co.com.framework.automatization.utility;

import org.openqa.selenium.WebDriver;

public class EntityConfiguration {	
	private static WebDriver driver;
	private static String nameDriver;
	private static String screenshotName;
	private static String screenshotTestPath;
	private static String screenshotCiclePath; 
	private static int contTest;
	
	//Contructor
	private EntityConfiguration() {
		
	}
	
	public static int getContTest() {
		return contTest;
	}

	public static void setContTest(int contTest) {
		EntityConfiguration.contTest = contTest;
	}

	public static String getScreenshotCiclePath() {
		return screenshotCiclePath;
	}

	public static void setScreenshotCiclePath(String screenshotCiclePath) {
		EntityConfiguration.screenshotCiclePath = screenshotCiclePath;
	}

	public static String getScreenshotTestPath() {
		return screenshotTestPath;
	}

	public static void setScreenshotTestPath(String screenshotTestPath) {
		EntityConfiguration.screenshotTestPath = screenshotTestPath;
	}

	public static String getScreenshotName() {
		return screenshotName;
	}

	public static void setScreenshotName(String screenshotName) {
		EntityConfiguration.screenshotName = screenshotName;
	}

	public static String getNameDriver() {
		return nameDriver;
	}

	public static void setNameDriver(String nameDriver) {
		EntityConfiguration.nameDriver = nameDriver;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		EntityConfiguration.driver = driver;
	}
}
