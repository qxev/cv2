package cn.clickvalue.cv2.report;
/**
 * 报表单个维度结构
 * @author jackie
 *
 */
public class Dimension {
    private String idField = null;
    private String nameField = null;
    private String memoField = null;
    private String idValue = null;
    private String nameValue = null;
    private String memoValue = null;
    private String[][] conditions = null;
    
    public String[][] getConditions() {
        return conditions;
    }
    public void setConditions(String[][] conditions) {
        this.conditions = conditions;
    }
    public String getIdField() {
        return idField;
    }
    public void setIdField(String idField) {
        this.idField = idField;
    }
    public String getNameField() {
        return nameField;
    }
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }
    public String getMemoField() {
        return memoField;
    }
    public void setMemoField(String memoField) {
        this.memoField = memoField;
    }
    public String getIdValue() {
        return idValue;
    }
    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }
    public String getNameValue() {
        return nameValue;
    }
    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }
    public String getMemoValue() {
        return memoValue;
    }
    public void setMemoValue(String memoValue) {
        this.memoValue = memoValue;
    }
}
