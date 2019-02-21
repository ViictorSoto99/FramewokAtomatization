package co.com.framework.automatization.utility;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConnectionGoogleDrive {
	private static JSONArray data;
	private static JSONObject dataRow;
	private static String urlExcel;
	private static String state;
	private static String params;

	// Contructor
	private ConnectionGoogleDrive() {

	}

	/**
	 * Este m�todo se usa para poder obtener toda la data que hay en una hoja de
	 * excel en drive
	 * 
	 * @param sheet -> Nombre de la hoja en donde esta posicionada la data
	 * @param row   -> N�mero de la fila que quiero capturar(Opcional)
	 * @return N/A
	 */
	public static void getDataExcel(String sheet, int row) {
		getService(sheet);
		setDataRow(row);
		setUrlExcel(urlExcel);
	}

	/**
	 * Este m�todo se usa para poder obtener toda la data que hay en una hoja de
	 * excel en drive
	 * 
	 * @param sheet -> Nombre de la hoja en donde esta posicionada la data
	 * @return N/A
	 */
	public static void getDataExcel(String sheet) {
		getService(sheet);
		setUrlExcel(urlExcel);
	}

	/**
	 * Este m�todo nos indica cuanto es el tama�o de la data en general que hay en
	 * el excel de Google Drive
	 * 
	 * @param N/A
	 * @return int -> Cantidad de datos
	 */
	public static int getLengthData() {
		return data.length() - 1;
	}

	/**
	 * Este m�todo obtiene un dato del archivo de excel de Google Drive con solo el
	 * nombre de la columna [Nota]: Solo se usa cuando le hayamos mandado al metodo
	 * getDataService(sheet, row) la fila
	 * 
	 * @param column -> Columna en donde esta posicionada el dato
	 * @return String -> El dato que esta en la celda
	 */
	public static String getDataColumn(String column) {
		String dato = "";
		for (int j = 0; j < dataRow.length(); j++) {
			String strColumn = dataRow.names().get(j).toString();
			if (column.equals(strColumn)) {
				dato = dataRow.get(column).toString();
				break;
			}
		}

		return dato;
	}

	/**
	 * Este m�todo manda a toda esta clase el link del excel de Google Drive que
	 * queremos leer
	 * 
	 * @param url -> Link de donde se encuentra el excel
	 * @return N/A
	 */
	public static void setUrlExcel(String url) {
		params = "?callback=ctrlq";
		urlExcel = url;
	}

	/**
	 * Este m�todo obtiene el dato que tiene una celda del excel de Google Drive
	 * 
	 * @param row    -> N�mero de la fila en donde se encuentra el dato
	 * @param column -> Nombre de la columna en donde se encuentra el dato
	 * @return String -> El dato que esta en la celda
	 */
	public static String getDataRowColumn(int row, String column) {
		if (data.length() > row) {
			JSONObject resJson = (JSONObject) data.get(row);
			return resJson.get(column).toString();
		} else {
			fail("NO EXISTE LA FILA " + row + " O LA COLUMNA " + column + " DEL ARCHIVO DE DRIVE");
			return "";
		}
	}

	/**
	 * Este m�todo obtiene todo un arreglo en formato JSON con los datos que esta en
	 * el excel de Google Drive
	 * 
	 * @param N/A
	 * @return JSONArray -> Todos los datos que estan en la hoja especificada
	 */
	public static JSONArray getData() {
		return data;
	}

	// Metodo de conecci�n con el servicio de Google Drive
	private static void getService(String sheet) {
		try {
			setParams("sheet", sheet);
			setParams("state", state);
			setParams("url", urlExcel);
			String url = "https://script.google.com/macros/s/AKfycbxlx47hrFk0y3rhXgzKxORu-24FiHCcBXQXMsWvNnhohMuZgAg/exec"
					+ params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String response = in.readLine();
			data = new JSONObject(response).getJSONArray("data");
		} catch (Exception e) {
			fail("NO ENCONTRO EL SERVICIO: " + e.getMessage());
		}
	}

	// Metodo para crear los parametros para mandar al servicio
	private static void setParams(String name, String value) {
		params = params + "&" + name + "=" + value;
	}

	// Metodo para definir la fila de donde quiero los datos para ejecutar dentro
	// del metodo getDataExcel(sheet, row)
	private static void setDataRow(int row) {
		for (int i = 0; i < data.length() - 1; i++) {
			JSONObject resJson = (JSONObject) data.get(i + 1);
			if (i == row) {
				dataRow = resJson;
				break;
			}
		}
	}

	/**
	 * Este m�todo manda una respuesta a la fila que especifique y queda como nueva
	 * columna en el excel de Google Drive
	 * 
	 * @param sheet  -> Hoja en la que quiero mandar la respuesta
	 * @param row    -> Fila en la que quiero mandar la respuesta
	 * @param result -> El texto que le mandare para la columna resultado
	 * @return N/A
	 */
	public static void setResultService(String sheet, int row, String result) {
		params = "?callback=ctrlq";
		setParams("result", result);
		setParams("row", "" + row);
		state = "setResult";
		getService(sheet);
	}
}
