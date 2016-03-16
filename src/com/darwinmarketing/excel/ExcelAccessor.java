/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.darwinmarketing.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jxl.Workbook;
import jxl.Sheet;
import jxl.Cell;
import java.io.File;

import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.WriteException;

/**
 * 
 * @author jackie
 */
public class ExcelAccessor {
	public boolean isSuccess = false;
	public String error = "";
	public EASheet[] sheets = null;
	public String[] sheetNames = null;
	public String[][][] sheetDatas = null;

	public ExcelAccessor() {
	}

	public void print(Object o) {
		System.out.println(String.valueOf(o));
	}

	public void loadExcel(String filepath) {
		isSuccess = false;
		try {
			File f = new File(filepath);
			if (f.exists()) {
				InputStream is = new FileInputStream(filepath);
				Workbook wb = Workbook.getWorkbook(is);
				int stn = wb.getNumberOfSheets();
				sheets = new EASheet[stn];
				sheetNames = new String[stn];
				sheetDatas = new String[stn][][];
				for (int st = 0; st < stn; st++) {
					Sheet sheet = wb.getSheet(st);
					String sname = sheet.getName();
					sheetNames[st] = sname;
					sheets[st] = new EASheet(sname);
					int rows = sheet.getRows();
					int cols = sheet.getColumns();
					sheets[st].cells = new EACell[rows][cols];
					sheetDatas[st] = new String[rows][cols];
					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < cols; j++) {
							Cell cell = sheet.getCell(j, i);
							String v = cell.getContents();
							sheets[st].cells[i][j] = new EACell(i, j, v);
							sheetDatas[st][i][j] = v;
						}
					}
				}
			} else {
				sheets = new EASheet[] {};
				sheetNames = new String[] {};
				sheetDatas = new String[][][] {};
			}
			isSuccess = true;
		} catch (Exception e) {
			error = wrap(e, "\n", "loadExcel Error Happen");
			isSuccess = false;
		}
	}

	public void writeExcel(String filepath, Object[] names, Object[][][] sds) throws WriteException, IOException {
		isSuccess = false;
		WritableWorkbook ww = null;
		try {
			// SheetData sda = (SheetData)sds[0];
			File f = new File(filepath);
			if (f.exists()) {
				f.delete();
			}
			int p = f.getPath().lastIndexOf(File.separator);
			String dr = f.getPath().substring(0, p);
			File d = new File(dr);
			d.mkdirs();
			ww = Workbook.createWorkbook(f);
			for (int st = 0; st < names.length; st++) {
				String cname = names[st].toString();
				WritableSheet ws = ww.createSheet(cname, st);
				// Object[][] sdarr = sda.getData(cname);
				for (int i = 0; i < sds[st].length; i++) {
					for (int j = 0; j < sds[st][i].length; j++) {
						if (sds[st][i][j] == null) {
						} else {
							Label lb = new Label(j, i, sds[st][i][j].toString());
							ws.addCell(lb);
						}
					}
				}
			}
			ww.write();
		} catch (Exception e) {
			error = wrap(e, "\n", "writeExcel Error Happen");
			isSuccess = false;
		} finally {
			ww.close();
		}
	}

	public String wrap(Exception ex, String linechar, String title) {
		StringBuffer logMsg = new StringBuffer("");
		logMsg.append(" ").append(title);
		logMsg.append(" ---- ").append(ex.toString()).append(linechar);
		StackTraceElement[] stackTrace = ex.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			logMsg.append(linechar);
			logMsg.append("          at ");
			logMsg.append(stackTrace[i].getClassName());
			logMsg.append(".");
			logMsg.append(stackTrace[i].getMethodName());
			logMsg.append(" <");
			logMsg.append(stackTrace[i].getFileName());
			logMsg.append(":");
			logMsg.append(String.valueOf(stackTrace[i].getLineNumber()));
			logMsg.append(">");
		}
		return logMsg.toString();
	}
}
