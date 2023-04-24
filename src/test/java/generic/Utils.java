package generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Utils {
	
	public static String getProperty(String path, String key) {
		String value = "";
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(path));
			value = p.getProperty(key);		
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		return value;
	}
	
	public static String readXL(String path, String sheet, int row, int cell) {
		String value = "";
		
		 try {
			Workbook wb = WorkbookFactory.create(new File(path));
			value = wb.getSheet(sheet).getRow(row).getCell(cell).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
