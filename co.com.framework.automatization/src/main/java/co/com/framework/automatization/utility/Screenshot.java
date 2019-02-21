package co.com.framework.automatization.utility;

import static org.junit.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Screenshot {
	private static String direcFolder; // Direcci�n de carpetas
	private static String moduleName; // Nombre del modulo posicionado
	private static boolean onejec = true; // Variable de un solo uso para verificar una sola ejecuci�n
	private static int test = 0; // Contador de Test
	private static String nameFromExecute; // Nombre de la ejecuci�n posicionadas

	// Constructor
	private Screenshot() {

	}

	/**
	 * Este m�todo se usa para resetear lo que es la ruta de las carpetas de las
	 * capturas
	 * 
	 * @param execute -> El n�mero de test en que esta corriendo la prueba
	 * @param name    -> Nombre del modulo en donde corre la prueba
	 * @return N/A
	 */
	public static void reset(String name) {
		test++;
		EntityConfiguration.setContTest(test);
		moduleName = EntityConfiguration.getNameDriver() + " - " + name;
		generateRout();
	}

	// Generar ruta de carpetas para capturas
	public static void generateRout() {
		String nameExecute = "Ciclo_";
		direcFolder = "src/test/resources/evidencies";
		direcFolder = direcFolder + "/" + getDateToday();
		direcFolder = direcFolder + "/" + moduleName;

		int a = 1;
		while (onejec) {
			if (!verifyFile(direcFolder + "/" + nameExecute + a)) {
				nameFromExecute = nameExecute + a;
				onejec = false;
				break;
			}
			a++;
		}

		direcFolder = direcFolder + "/" + nameFromExecute;
		EntityConfiguration.setScreenshotCiclePath(direcFolder);
		
		direcFolder = direcFolder + "/Test_" + test;
		EntityConfiguration.setScreenshotTestPath("Test_" + test);
		
		File file = new File(direcFolder);
		
		if(!file.exists())
			file.mkdirs();
	}

	/**
	 * Este m�todo toma la captura que hay en el momento
	 * 
	 * @param N/A
	 * @return N/A
	 */
	public static void takeCapture() {
		String imageFileDir = "NO IMGFILE";
		String nombreCapturas = "NO CAPTURE";

		try {
			int a = 1;
			while (true) {
				nombreCapturas = "Capture_" + a + ".jpg";
				imageFileDir = direcFolder + "/" + nombreCapturas;

				if (verifyFile(imageFileDir)) {
					a++;
				} else {
					break;
				}
			}

			File screenshoot = ((TakesScreenshot) EntityConfiguration.getDriver()).getScreenshotAs(OutputType.FILE);
			EntityConfiguration.setScreenshotName(nombreCapturas);
			FileUtils.copyFile(screenshoot, new File(imageFileDir));
		} catch (Exception e) {
			fail(e.getMessage() + ": " + nombreCapturas);
		}
	}

	// Obtiene fecha de hoy en formato yyyy-mm-dd
	private static String getDateToday() {
		Date hoy = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(hoy);
	}

	// Verifica si una ruta de carpeta existe o no
	public static boolean verifyFile(String direcCarpeta) {
		File carpeta = new File(direcCarpeta);
		return carpeta.exists();
	}
}
