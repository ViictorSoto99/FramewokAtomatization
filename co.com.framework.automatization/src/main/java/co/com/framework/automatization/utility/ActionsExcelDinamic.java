package co.com.framework.automatization.utility;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ActionsExcelDinamic {
	private static ArrayList<String[]> data;
	private static String routExcel;
	
	//Constructor
	private ActionsExcelDinamic(){
		
	}

	/**
	 * Este m�todo se usa para obtener la ruta donde esta el archivo de excel
	 * 
	 * @param N/A
	 * @return String: Ruta donde esta el excel
	 */
	public static String getRoutExcel() {
		return routExcel;
	}

	/**
	 * Este m�todo se usa para mandarle la ruta del excel que ejecutara la prueba
	 * 
	 * @param routExcel
	 *            -> Ruta donde esta ubicada el excel
	 * @return N/A
	 */
	public static void setRoutExcel(String routExcel) {
		ActionsExcelDinamic.routExcel = routExcel;
	}

	/**
	 * Este m�todo se usa obtener un dato de una celda del archivo de excel
	 * 
	 * @param row
	 *            -> Fila en donde esta posicionado el campo
	 * @param column
	 *            -> Nombre de la columna en donde esta el campo
	 * @return
	 * @return String: Dato que esta en la celda
	 */
	public static String getDataRowColumn(int row, String column) {
		String dato = "";
		for (int i = 0; i < data.get(1).length; i++) {
			if (data.get(1)[i].equals(column)) {
				dato = data.get(row)[i];
				break;
			} else if (i == data.get(1)[i].length() - 1) {				
				fail("NO EXISTE DATO EN LA FILA " + row + " O LA COLUMNA " + column + " DEL ARCHIVO DE EXCEL");
			}
		}

		return dato;
	}

	/**
	 * Este m�todo se usa para poder obtener toda la data que hay en una hoja de
	 * excel
	 * 
	 * @param sheet
	 *            -> Nombre de la hoja en donde esta posicionada la data
	 * @return N/A
	 */
	public static void getDataExcel(String sheet) {
		getExcel(sheet);
	}

	/**
	 * Este m�todo se usa para poder obtener toda la data que hay en una hoja de
	 * excel * @param nameSheet -> Nombre de la hoja en donde esta posicionada la
	 * data
	 * 
	 * @return N/A
	 */
	public static void getAutomationConfiguration(String action) {
		setRoutExcel("automationConfiguration.xlsx");
		getDataExcel(action);
	}

	// Verificar un tipo de dato que hay en una celda
	private static String verificateCeld(Cell celd) {
		String dato;
		switch (celd.getCellTypeEnum().toString()) {
		case "NUMERIC":
			if (HSSFDateUtil.isCellDateFormatted(celd)) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				dato = "" + sdf.format(celd.getDateCellValue());
			} else {
				dato = "" + (long) celd.getNumericCellValue();
			}
			break;
		case "STRING":
			dato = celd.getStringCellValue();
			break;

		case "FORMULA":
			dato = celd.getStringCellValue();
			break;
		default:
			dato = "";
			break;
		}

		return dato;
	}
	
	//Metodo de conecci�n con el EXCEL
	private static void getExcel(String nameSheet) {
		try {
			FileInputStream file = new FileInputStream(routExcel);
			XSSFWorkbook wb = new XSSFWorkbook(file);
			XSSFSheet sheet = wb.getSheet(nameSheet);
			Iterator<Row> rowIterator = sheet.iterator();

			data = new ArrayList<>();
			data.add(0, null);

			Row rowNameColumn = sheet.getRow(0);
			Iterator<Cell> cellNameColumn = rowNameColumn.cellIterator();
			List<String> nameColumn = new ArrayList<>();

			while (cellNameColumn.hasNext()) {
				nameColumn.add(verificateCeld(cellNameColumn.next()));
			}

			for (int i = 0; rowIterator.hasNext(); i++) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				String[] datasCell = new String[nameColumn.size()];
				while (cellIterator.hasNext()) {
					Cell celd = cellIterator.next();
					datasCell[celd.getColumnIndex()] = verificateCeld(celd);
				}

				for (int j = 0; j < datasCell.length; j++) {
					if (datasCell[j] == null) {
						datasCell[j] = "";
					}
				}

				if (i < row.getRowNum()) {
					for (int j = i; j < row.getRowNum(); j++) {
						data.add(new String[0]);
					}
				}

				data.add(datasCell);
			}
			file.close();
			wb.close();
		} catch (Exception e) {
			fail("NO ENTRO AL EXCEL: " + e.getMessage());
		}
	}

}
