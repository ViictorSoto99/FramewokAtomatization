package co.com.framework.automatization.utility;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 *@author Victor Manuel Soto Morales (victor.soto)
 *@version 1.0.0 
 */
public class ConfigurationDriver {
	private static WebDriver driver;
	
	//Contructor
	private ConfigurationDriver() {
		
	}

	/**
	 * Este m�todo para poder obtener un driver web especificado en el archivo de
	 * configuraci�n
	 * @return WebDriver: Driver a usar en toda la ejecuci�n
	 */
	public static WebDriver executeWebDriver() {
		ActionsExcelDinamic.getAutomationConfiguration("WebDriver");
		String nameDriver = "NO HAY DRIVER";

		String plataform = ActionsExcelDinamic.getDataRowColumn(2, "PLATAFORM");
		if (plataform.equals("Web")) {	
			String browser = ActionsExcelDinamic.getDataRowColumn(2, "BROWSER");
			validateBrowser(browser);
			
			nameDriver = plataform + "-" + browser;
			
		} else if (plataform.equals("Mobile")) {
			String udid = ActionsExcelDinamic.getDataRowColumn(2, "UDID");
			String appPackage = ActionsExcelDinamic.getDataRowColumn(2, "APP_PACKAGE");
			String appActivity = ActionsExcelDinamic.getDataRowColumn(2, "APP_ACTIVITY");
			String bundleId = ActionsExcelDinamic.getDataRowColumn(2, "BUNDLE_ID");
			String urlAppium = ActionsExcelDinamic.getDataRowColumn(2, "URL_APPIUM");
			
			setCapabilitieDriver(udid, appPackage, appActivity, bundleId, urlAppium);
			
			String[] name = appPackage.split(Pattern.quote("."));
			nameDriver = plataform + "-" + udid + "_" + name[name.length-1];
		}
		
		EntityConfiguration.setNameDriver(nameDriver);		
		return driver;
	}

	/**
	 * Este m�todo para poder obtener un driver web en especifico
	 * 
	 * @param browser
	 *            -> El navegador que desea ejecutar son los siguientes: [Chrome,
	 *            IExplorer, Firefox, Safari, Edge, Opera]
	 * @return 
	 * @return WebDriver: Driver a usar en toda la ejecuci�n
	 */
	public static WebDriver executeWebDriver(String browser) {
		EntityConfiguration.setNameDriver("Web-" + browser);
		validateBrowser(browser);
		return driver;
	}

	/**
	 * Este m�todo para poder obtener un driver mobil en especifico
	 * 
	 * @param udid -> UDID del dispositivo
	 * @param appPackage -> APP_PACKAGE de la aplicaci�n
	 * @param appActivity -> APP_ACTIVITY de la aplicaci�n
	 * @param bundleId -> BUNDLE_ID de la aplicaci�n
	 * @param urlAppium -> URL_APPIUM que nos proporciona el appium
	 * @return 
	 * @return WebDriver: Driver a usar en toda la ejecuci�n
	 */
	public static WebDriver executeWebDriver(String udid, String appPackage, String appActivity, String bundleId, String urlAppium) {
		EntityConfiguration.setNameDriver(udid + "_" + appPackage.split(".")[appPackage.split(".").length-1]);
		setCapabilitieDriver(udid, appPackage, appActivity, bundleId, urlAppium);
		return driver;
	}

	// Validando que tipo de driver web se usara en toda la ejecuci�n
	private static void validateBrowser(String browser) {
		try {
			switch (browser) {
			case "Chrome":
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "IExplorer":
				WebDriverManager.iedriver().setup();
				InternetExplorerOptions option = new InternetExplorerOptions();
				option.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver = new InternetExplorerDriver(option);
				break;
			case "Firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
			case "Safari":
				driver = new SafariDriver();
				break;
			case "Edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			case "Opera":
				WebDriverManager.operadriver().setup();
				driver = new OperaDriver();
				break;
			default:
				fail("NO ABRIO EL NAVEGADOR: No se encontro ningun navegador especificado");
				break;
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(0, TimeUnit.SECONDS);

		} catch (Exception e) {
			fail("NO ABRIO EL NAVEGADOR: " + e.getMessage());
		}

		validateDriver();
	}

	// Configurando aplicaci�n Mobile al driver
	private static void setCapabilitieDriver(String udid, String appPackage, String appActivity, String bundleId, String urlAppium) {
		try {
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability(MobileCapabilityType.UDID, udid);
			dc.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
			dc.setCapability(MobileCapabilityType.DEVICE_NAME, udid);

			dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
			dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
			dc.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
			dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, bundleId);
			driver = new RemoteWebDriver(new URL(urlAppium), dc);
		} catch (Exception e) {
			fail("NO ABRIO LA APP: " + e.getMessage());
		}
		validateDriver();
	}

	private static void validateDriver() {
		if(EntityConfiguration.getDriver() != null) {
			EntityConfiguration.getDriver().quit();
		}
		
		EntityConfiguration.setDriver(driver);
	}
}
