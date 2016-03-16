package cn.clickvalue.cv2.excel.parseXml;

import cn.clickvalue.cv2.excel.config.model.ExcelConfigModel;

public interface ParseXmlService {

    /**
     * 传入ExcelConfigModel
     * @param clazz
     * @return
     */
    public ExcelConfigModel loadExcelConfigModel(Class clazz);
    
}
