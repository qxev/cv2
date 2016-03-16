package cn.clickvalue.cv2.excel.operation;

import java.util.List;

import jxl.write.WritableSheet;

public interface ExcelWriter extends ExcelOperation {

    public WritableSheet  createExcel(WritableSheet ws, Class clazz, List modelList);
    
}
