package cn.clickvalue.cv2.excel.config.model;

public class PropertyModel {

    private String name;
    private String column;
    private String excelTitleName;
    private String dataType;
    private String maxLength;
    private String fixity;
    private String codeTableName;
    private String defaultValue;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getExcelTitleName() {
        return excelTitleName;
    }
    public void setExcelTitleName(String excelTitleName) {
        this.excelTitleName = excelTitleName;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }
    public String getFixity() {
        return fixity;
    }
    public void setFixity(String fixity) {
        this.fixity = fixity;
    }
    public String getCodeTableName() {
        return codeTableName;
    }
    public void setCodeTableName(String codeTableName) {
        this.codeTableName = codeTableName;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
}
