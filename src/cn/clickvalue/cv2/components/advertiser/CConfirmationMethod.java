package cn.clickvalue.cv2.components.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;

public class CConfirmationMethod {
    
    @Inject
    private Messages message;
    
    public CConfirmationMethod() {
    this.confirmList = Constants.getConfirmationMethods(message); 
    }
    
    @InjectSelectionModel(labelField = "label", idField = "value")
    private List<LabelValueModel> confirmList = new ArrayList<LabelValueModel>();

    @Persist
    private LabelValueModel labelValueModel;


    public List<LabelValueModel> getConfirmList() {
        return confirmList;
    }

    public void setConfirmList(List<LabelValueModel> confirmList) {
        this.confirmList = confirmList;
    }

    public LabelValueModel getLabelValueModel() {
        return labelValueModel;
    }

    public void setLabelValueModel(LabelValueModel labelValueModel) {
        this.labelValueModel = labelValueModel;
    }
}
