package cn.clickvalue.cv2.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jxl.read.biff.BiffException;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.clickvalue.cv2.model.QueryField;

public class ExcelUtils {

	/**
	 * @throws IOException
	 * @throws ParsePropertyException
	 * 
	 */
	public static String mergerXLS(List<?> models, String inputName, String outputName) throws ParsePropertyException, IOException {
		String realPath = RealPath.getRoot();
		String template = realPath.concat("templet/").concat(inputName).concat(".xls");
		String output = realPath.concat("excel/").concat(outputName).concat(".xls");
		XLSTransformer transformer = new XLSTransformer();
		Map<String, List<?>> beans = new HashMap<String, List<?>>();
		beans.put("models", models);
		transformer.transformXLS(template, beans, output);
		return output;
	}

	/**
	 * @throws IOException
	 * @throws ParsePropertyException
	 * 
	 */
	public static void mergerJobXLS(List<?> models, String inputName, String outputName) throws ParsePropertyException, IOException {
		String realPath = RealPath.getRoot();
		XLSTransformer transformer = new XLSTransformer();
		Map<String, List<?>> beans = new HashMap<String, List<?>>();
		beans.put("models", models);
		transformer.transformXLS(realPath + "templet/" + inputName + ".xls", beans, realPath + outputName + ".xls");
	}

	public static void writeExcel(String path, List<QueryField> ourputList, String templateName) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(path + templateName + ".xls");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet s = wb.createSheet();
			wb.setSheetName(0, "sheet1");
			int i = 0;

			for (QueryField queryField : ourputList) {
				HSSFRow row = s.createRow(0);
				HSSFCell cell = row.createCell((short) i);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);

				cell.setCellValue(queryField.getName());
				row = s.createRow(1);
				cell = row.createCell((short) i);
				cell.setCellValue("${models." + queryField.getExportName() + "}");
				i++;
			}
			wb.write(fos);
		} finally {
			fos.close();
		}
	}

	public static void writeExcel(String path, Map<String, String> map, String templateName) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(path + templateName + ".xls");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet s = wb.createSheet();
			wb.setSheetName(0, "first sheet");

			Set<String> keys = map.keySet();
			int i = 0;

			for (String key : keys) {
				HSSFRow row = s.createRow(0);
				HSSFCell cell = row.createCell((short) i);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);

				cell.setCellValue(map.get(key));
				row = s.createRow(1);
				cell = row.createCell((short) i);
				cell.setCellValue("${models." + key + "}");
				i++;
			}
			wb.write(fos);
		} finally {
			fos.close();
		}
	}

	public static <T> List<T> readExl(Class<T> clazz, String resource, String xlsPath) throws BiffException, IOException,
			InstantiationException, IllegalAccessException, NoSuchFieldException {
		jxl.Workbook workBook = null;
		List<T> list = new ArrayList<T>();
		try {
			Properties props = PropertiesUtils.loadProperties(resource);
			Map<Integer, String> map = new HashMap<Integer, String>();
			Enumeration<Object> keys = props.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				map.put(Integer.parseInt(key), props.get(key).toString());
			}
			InputStream input = new FileInputStream(xlsPath);
			workBook = jxl.Workbook.getWorkbook(input);
			jxl.Sheet s = workBook.getSheet(0);
			int rows = s.getRows();
			for (int x = 1; x < rows; x++) {
				boolean available = false;
				T object = (T) clazz.newInstance();
				Set<Integer> ks = map.keySet();
				for (Iterator<Integer> iterator = ks.iterator(); iterator.hasNext();) {
					Integer y = iterator.next();
					String value = map.get(y);
					String content = s.getCell(y, x).getContents();
					BeanUtils.setDeclaredProperty(object, value, StringUtils.trimToNull(content));
					if (!available && StringUtils.isNotEmpty(content)) {
						available = true;
					}
				}
				// 空行不返回
				if (available) {
					list.add(object);
				}
			}
		} finally {
			if (workBook != null) {
				workBook.close();
			}
		}
		return list;
	}

	public static List<Map<String, String>> readExl(String resource, String xlsPath) throws BiffException, IOException,
			InstantiationException, IllegalAccessException, NoSuchFieldException {
		jxl.Workbook workBook = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			Properties props = PropertiesUtils.loadProperties(resource);
			Map<Integer, String> map = new HashMap<Integer, String>();
			Enumeration<Object> keys = props.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				map.put(Integer.parseInt(key), props.get(key).toString());
			}
			InputStream input = new FileInputStream(xlsPath);
			workBook = jxl.Workbook.getWorkbook(input);
			jxl.Sheet s = workBook.getSheet(0);
			int rows = s.getRows();
			for (int x = 1; x < rows; x++) {
				boolean available = false;
				Map<String, String> object = new HashMap<String, String>();
				Set<Integer> ks = map.keySet();
				for (Iterator<Integer> iterator = ks.iterator(); iterator.hasNext();) {
					Integer y = iterator.next();
					String value = map.get(y);
					String content = s.getCell(y, x).getContents();
					object.put(value, StringUtils.trimToNull(content));
					if (!available && StringUtils.isNotEmpty(content)) {
						available = true;
					}
				}
				// 空行不返回
				if (available) {
					list.add(object);
				}
			}
		} finally {
			if (workBook != null) {
				workBook.close();
			}
		}
		return list;
	}

}
