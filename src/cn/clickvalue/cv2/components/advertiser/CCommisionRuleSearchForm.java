package cn.clickvalue.cv2.components.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CCommisionRuleSearchForm {

    private int campaignId;
    private Form search;
    
    public int getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
   
    public Form getSearch() {
        return search;
    }
    public void setSearch(Form search) {
        this.search = search;
    }
}
