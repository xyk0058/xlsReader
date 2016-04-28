import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XlsUtil {
	
  
	public static ArrayList<String> read(String filePath) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		int index = wb.getSheetIndex("现场更新版");
		//System.out.println("Sheet:"+index+":"+wb.getSheetName(index));
		Sheet sheet1 = wb.getSheetAt(index);
		for (Row row : sheet1) {
			String cell3 = getCellValue(row.getCell(3),true);
			String cell6 = getCellValue(row.getCell(6),true);
			String cell7 = getCellValue(row.getCell(7),true);
//			for (Cell cell : row) {
//				//System.out.print(cell.getStringCellValue() + "  ");
//				System.out.print(getCellValue(cell,true) + "  ");
//			}
			
			//if(cell6.contains("1") && cell7.contains("黑色粉末")){
			if(cell6.contains("1") && !cell7.equals("")){
				list.add(cell3);
				//System.out.println(cell3+":"+cell6+":"+cell7);
				//System.out.println(cell3+":"+cell6+":"+cell7);
			}
		}
		wb.close();
		return list;
	}
	
	private static String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

}