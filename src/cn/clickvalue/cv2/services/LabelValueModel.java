package cn.clickvalue.cv2.services;

import java.io.Serializable;

/**
 * 用户默认的model select
 */
public class LabelValueModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String label;

    private String value;

    public LabelValueModel() {
    }

    public LabelValueModel(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
