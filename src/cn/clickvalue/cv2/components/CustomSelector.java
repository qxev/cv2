package cn.clickvalue.cv2.components;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

import cn.clickvalue.cv2.services.util.selector.GeneralSelectModel;
import cn.clickvalue.cv2.services.util.selector.GeneralValueEncoder;

public class CustomSelector<T> {

    @Parameter(required = true)
    private List<T> list;

    @Parameter(defaultPrefix="literal")
    private String valueField;

    @Parameter(defaultPrefix="literal")
    private String labelField;

    @Persist
    private T selected;

    @Inject
    private PropertyAccess propertyAccess;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public ValueEncoder<T> getMyValueEncode() {
        return new GeneralValueEncoder<T>(getList(), valueField, propertyAccess);
    }

    public SelectModel getMySelectModel() {
        return new GeneralSelectModel<T>(getList(), labelField, propertyAccess);
    }
}
