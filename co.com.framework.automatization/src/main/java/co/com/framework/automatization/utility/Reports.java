package co.com.framework.automatization.utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

import cucumber.api.Result;
import cucumber.api.Scenario;
import cucumber.runtime.ScenarioImpl;

public class Reports {
	private static ExtentTest feature;
	private static ExtentReports extent;
	private static ExtentTest scenary;
	private static ExtentTest test;
	private static boolean execute = true;
	private static String uriScenario = "";
	private static String nameFeature;

	// Contructor
	private Reports() {

	}

	// Inicializa los reportes y solo se ejecuta una vez
	private static void initializeReports() {
		if (execute) {
			ExtentHtmlReporter reporter = new ExtentHtmlReporter(EntityConfiguration.getScreenshotCiclePath() + "\\Reporter.html");
			
			//Configuraci�n del html
			reporter.config().setDocumentTitle("ExtentReportGenerate");
			reporter.config().setReportName(System.getProperty("user.dir"));
			reporter.config().setTheme(Theme.DARK);
			reporter.config().setEncoding("ISO-8859-1");
			reporter.config().enableTimeline(true);
			reporter.config().setProtocol(Protocol.HTTPS);
			reporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

			extent = new ExtentReports();
			extent.setAnalysisStrategy(AnalysisStrategy.TEST);
			extent.attachReporter(reporter);
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
			extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Plataform execute", EntityConfiguration.getNameDriver());
			execute = false;
		}
	}

	/**
	 * Este m�todo se usa crear un contenedor de reportes Feature
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void configFeatureAndScenary(Scenario sce) {
		if(!uriScenario.equals(sce.getUri())) {
			String[] uri = sce.getUri().split("/");
			nameFeature = uri[uri.length - 1];

			nameFeature = nameFeature.replaceAll("(.)([A-Z])", "$1 $2");
			nameFeature = nameFeature.replace(".", "/").split("/")[0];
			nameFeature = nameFeature.toUpperCase().charAt(0) + nameFeature.substring(1, nameFeature.length());
		}
		
		Screenshot.reset(nameFeature);
		
		if (!uriScenario.equals(sce.getUri())) {
			initializeReports();
			uriScenario = sce.getUri();

			feature = extent.createTest(nameFeature + " Feature");
			
			setLogFeature(Status.INFO, System.getProperty("user.dir") + "/" + sce.getUri());
			assignAutorFeature("Feature");
		}
		
		Reports.createScenary(sce);
	}

	/**
	 * Asignar un autor al Feature para filtrar
	 * 
	 * @param name -> Nombre del autor
	 * @return N/A
	 */
	public static void assignAutorFeature(String name) {
		feature.assignAuthor(name);
	}

	/**
	 * Asignar un categoria al Feature para filtrar
	 * 
	 * @param name -> Nombre de la categoria
	 * @return N/A
	 */
	public static void assignCategoryFeature(String name) {
		feature.assignCategory(name);
	}

	/**
	 * Enviar un log al feature en el reporte con una captura automatica he incluida
	 * en el log
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogFeatureScreenShot(Status status, String description) {
		try {
			Screenshot.takeCapture();
			feature.log(status, description,
					MediaEntityBuilder.createScreenCaptureFromPath(EntityConfiguration.getScreenshotTestPath() + "\\" + EntityConfiguration.getScreenshotName()).build());
		} catch (IOException e) {
			feature.fatal(e.getMessage());
		}
	}

	/**
	 * Enviar un log al feature en el reporte
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogFeature(Status status, String description) {
		feature.log(status, description);
	}

	/**
	 * Este m�todo se usa crear un contenedor de reportes Scenario
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void createScenary(Scenario sce) {
		scenary = feature.createNode("Scenary " + EntityConfiguration.getContTest() + ": " + sce.getName());
		scenary.info(sce.getId());

		test = null;
		for (String tag : sce.getSourceTagNames()) {
			assignCategoryScenary(tag);
		}
	}

	/**
	 * Asignar un autor al Scenario para filtrar
	 * 
	 * @param name -> Nombre del autor
	 * @return N/A
	 */
	public static void assignAutorScenary(String name) {
		scenary.assignAuthor(name);
	}

	/**
	 * Asignar un categoria al Scenario para filtrar
	 * 
	 * @param name -> Nombre de la categoria
	 * @return N/A
	 */
	public static void assignCategoryScenary(String name) {
		scenary.assignCategory(name);
	}

	/**
	 * Enviar un log al Scenario en el reporte con una captura automatica he
	 * incluida en el log
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogScenaryScreenShot(Status status, String description) {
		try {
			Screenshot.takeCapture();
			scenary.log(status, description,
					MediaEntityBuilder.createScreenCaptureFromPath(EntityConfiguration.getScreenshotTestPath() + "\\" + EntityConfiguration.getScreenshotName()).build());
		} catch (IOException e) {
			scenary.fatal(e.getMessage());
		}
	}

	/**
	 * Enviar un log al Scenario en el reporte
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogScenary(Status status, String description) {
		scenary.log(status, description);
	}

	/**
	 * Este m�todo se usa crear un test tipo GIVEN en los reportes
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void createTestGiven(String description) {
		createTestGherkin("Given ", description);
		test.assignCategory("Given");
	}

	/**
	 * Este m�todo se usa crear un test tipo WHEN en los reportes
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void createTestWhen(String description) {
		createTestGherkin("When ", description);
		test.assignCategory("When");
	}

	/**
	 * Este m�todo se usa crear un test tipo THEN en los reportes
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void createTestThen(String description) {
		createTestGherkin("Then ", description);
		test.assignCategory("Then");
	}

	/**
	 * Este m�todo se usa para crear Test alternos por fuera de los establecidos un
	 * contenedor de reportes
	 * 
	 * @param nameTest -> Test que se esta ejecutando
	 * @return N/A
	 */
	public static void createTest(String nameTest) {
		test = scenary.createNode(nameTest);
	}

	/**
	 * Asignar un autor al Scenario para filtrar
	 * 
	 * @param name -> Nombre del autor
	 * @return N/A
	 */
	public static void assignAutorTest(String name) {
		test.assignAuthor(name);
	}

	/**
	 * Asignar un categoria al Test para filtrar
	 * 
	 * @param name -> Nombre de la categoria
	 * @return N/A
	 */
	public static void assignCategoryTest(String name) {
		test.assignCategory(name);
	}

	/**
	 * Enviar un log al Test en el reporte con una captura automatica he incluida en
	 * el log
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogTestScreenShot(Status status, String description) {
		try {
			Screenshot.takeCapture();
			test.log(status, description,
					MediaEntityBuilder.createScreenCaptureFromPath(EntityConfiguration.getScreenshotTestPath() + "\\" + EntityConfiguration.getScreenshotName()).build());
		} catch (IOException e) {
			test.fatal(e.getMessage());
		}
	}

	/**
	 * Enviar un log al Test en el reporte
	 * 
	 * @param status      -> status del log ejemplo: Status.PASS
	 * @param description -> Descripci�n a ese log
	 * @return N/A
	 */
	public static void setLogTest(Status status, String description) {
		test.log(status, description);
	}

	/**
	 * Este m�todo se usa para finalizar el scenario y sacar un status de la misma
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void finallyStatusScenario(Scenario sce) {
		String msg = sce.getStatus().toString();
		switch (sce.getStatus()) {
		case PASSED:
			scenary.pass(msg);
			break;
		case FAILED:
			scenary.fail(msg);
			break;

		case SKIPPED:
			scenary.skip(msg);
			break;

		case PENDING:
			scenary.debug(msg);
			break;

		case UNDEFINED:
			scenary.fatal(msg);
			break;
		default:
			scenary.log(Status.INFO, msg);
			break;
		}

		if (sce.isFailed())
			logError(sce);

		try {
			String rout = EntityConfiguration.getScreenshotCiclePath() + "\\" + EntityConfiguration.getScreenshotTestPath();
			String[] list = new File(rout).list();

			for (int i = 0; i < list.length; i++) {
				scenary.addScreenCaptureFromPath(EntityConfiguration.getScreenshotTestPath() + "\\" + list[i]);
			}

		} catch (IOException e) {
			scenary.fatal("No encontro la carpeta de las capturas");
			scenary.fatal(e.getMessage());
		}
	}

	/**
	 * Este m�todo se usa para finalizar lso reportes
	 * 
	 * @param sce -> Scenario que se esta ejecutando
	 * @return N/A
	 */
	public static void finalliceReport() {
		extent.flush();
	}

	@SuppressWarnings("unchecked")
	private static void logError(Scenario scenario) {
		Field field = FieldUtils.getField(((ScenarioImpl) scenario).getClass(), "stepResults", true);
		field.setAccessible(true);
		try {
			ArrayList<Result> results = (ArrayList<Result>) field.get(scenario);
			for (Result result : results) {
				if (result.getError() != null) {
					setLogTest(Status.ERROR, "Error Scenario: " + result.getError());
					setLogTest(Status.ERROR, result.getErrorMessage());
				}
			}
		} catch (Exception e) {
			setLogTest(Status.FATAL, "Error while logging error: " + e);
		}
	}
	
	private static String typeTest = "";
	private static void createTestGherkin(String test, String description) {
		if(typeTest.equals(test)) {
			createTest("And " + description);
		}else{
			createTest(test + description);
		}
		
		typeTest = test;
	}
}
