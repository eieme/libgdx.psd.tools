package com.keyroy.gdx.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.HTMLEditorKit.Parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.m.JSONArray;
import org.json.m.JSONObject;

public class I18nXlsxParser {
	private static boolean debug = false;
	
	public static final List<JsonPack> parser(File file) throws Exception {
		List<JsonPack> arrays = new ArrayList<JsonPack>();
		if (file.getName().endsWith(".xlsx")
				&& file.getName().startsWith("~$") == false) {
			log("parser file ", file.getName());
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
			log( "the sheet num :"+workbook.getNumberOfSheets());
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				log("parser sheet:"+sheet.getSheetName());
				//
				int definingRow = 2;
				XSSFRow row = sheet.getRow(definingRow);
				
				if (row == null) {
					continue;
				} else {
					HashMap<Integer, Language> languages = parserData(sheet,row,definingRow);
					for (Language language : languages.values()) {
						JSONObject json = new JSONObject();
						System.out.println(language.language);
						for (Pair pair : language.pairs) {
							System.out.println(pair.keyString+":"+pair.valueString);
							json.put(pair.keyString, pair.valueString);
						}
						if(json.length() != 0){
							arrays.add(new JsonPack(language.language, json));	
						}
					}
				}
			}
			workbook.close();
		}
		return arrays;
	}
	public static HashMap<Integer, Language> parserData(XSSFSheet sheet,XSSFRow row,int definingRow) {
		HashMap<Integer, Language> languages = new HashMap<Integer, Language>();
		
		for (int col = 0; col < row.getLastCellNum(); col++) {
			XSSFCell column = row.getCell(col);
			if (column != null
					&& column.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				String cellValue = column.getStringCellValue();
				if(!cellValue.equals("key")){
					Language language = new Language(cellValue);
					languages.put(col,language);
					System.out.println("row:"+definingRow+" col:"+col+" val:"+column.getStringCellValue());
				}
			}
		}
		definingRow++;
		for (int r = definingRow; r < sheet
				.getPhysicalNumberOfRows(); r++) {
			row = sheet.getRow(r);
			
			if (row != null) {
				for (int col = 0; col < row.getLastCellNum(); col++) {
					XSSFCell column = row.getCell(col);
					if (column != null) {
						try {
							String cellValue = getCellData(sheet,column);
							if(cellValue != null && !cellValue.equals("key")){
								XSSFCell keyCell = row.getCell(0);
								System.out.println("row:"+r+" col:"+col+" val:"+cellValue + " keyCell: "+keyCell);
								if(keyCell == null){
									System.out.println("the key cell is null so we'll continue");
									continue;
								}
								String keyString = keyCell.getStringCellValue();
								Pair pair = new Pair(keyString,cellValue);
								Language language = languages.get(col);
								if(language != null){
									language.put(pair);	
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
//		for (Language language : languages.values()) {
//			System.out.println(language.language);
//			for (Pair pair : language.pairs) {
//				System.out.println(pair.keyString+":"+pair.valueString);
//			}
//		}
		return languages;
	}
	private static final String getCellData(XSSFSheet sheet, XSSFCell cell) {
		String source = null;
		if (cell == null) {

		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			source = "" + cell.getNumericCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			source = cell.getStringCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			source = "" + cell.getBooleanCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_ERROR) {
			log("Error Cell", cell.getErrorCellString());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			log("Error Blank", cell.getReference());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {

			FormulaEvaluator evaluator = sheet.getWorkbook()
					.getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);
			if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				source = "" + cellValue.getNumberValue();
			} else if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				source = "" + cellValue.getBooleanValue();
			} else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
				source = "" + cellValue.getStringValue();
			} else {
				source = cellValue.getStringValue();
			}
		} else {
			log("Error CellType", "" + cell.getCellType());
		}

		return source;
	}

	private static class Language{
		String language;
		ArrayList<Pair> pairs = new ArrayList<I18nXlsxParser.Pair>();
		public Language(String language) {
			this.language = language;
		}
		public void put(Pair pair) {
			pairs.add(pair);
		}
	}
	private static class Pair{
		String keyString;
		String valueString;
		public Pair(String keyString,String valueString) {
			valueString.trim();
			valueString = valueString.replace("\\n", "\n");
			this.keyString = keyString;
			this.valueString = valueString;
		}
	}

	private static final void log(String msg) {
		if (debug) {
			System.out.println(msg);
		}
	}

	private static final void log(String key, String msg) {
		log(key + " : " + msg);
	}

}
