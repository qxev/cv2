package cn.clickvalue.cv2.components.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;

public class CApplyingMode {
    @Persist
    private LabelValueModel labelValueModel;

    @Persist
    @InjectSelectionModel(labelField = "label", idField = "value")
    private List<LabelValueModel> list = new ArrayList<LabelValueModel>();

    @Inject
    private Messages message;

    @SetupRender
    public void setupRender() {
        this.list = Constants.getApplicationStatus(message);
    }

    public LabelValueModel getLabelValueModel() {
        return labelValueModel;
    }

    public List<LabelValueModel> getList() {
        return list;
    }

    public void setLabelValueModel(LabelValueModel labelValueModel) {
        this.labelValueModel = labelValueModel;
    }

    public void setList(List<LabelValueModel> list) {
        this.list = list;
    }
}
