package cn.clickvalue.cv2.services.util.selector;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.util.AbstractSelectModel;

public class GeneralSelectModel<T> extends AbstractSelectModel {

    private String labelField;

    private List<T> list;

    private final PropertyAccess adapter;

    private GeneralGetOptionCallBack generalGetOptionCallBack;

    public GeneralSelectModel(List<T> list, String labelField,
            PropertyAccess adapter) {
        this.labelField = labelField;
        this.list = list;
        this.adapter = adapter;
    }
    

    public GeneralSelectModel(GeneralGetOptionCallBack generalGetOptionCallBack) {
        this.generalGetOptionCallBack = generalGetOptionCallBack;
        this.adapter = null;
    }


    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {
        if (generalGetOptionCallBack != null) {
            getOptionsCallBack();
        }
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();

        for (T obj : list) {
            if (labelField == null || labelField.equals("")) {
                optionModelList.add(new OptionModelImpl(obj + "", obj));
            } else {
                optionModelList.add(new OptionModelImpl(adapter.get(obj,labelField) + "", obj));
            }
        }
        
        return optionModelList;
    }

    private List<OptionModel> getOptionsCallBack() {
        return generalGetOptionCallBack.getOptions();
    }
}
