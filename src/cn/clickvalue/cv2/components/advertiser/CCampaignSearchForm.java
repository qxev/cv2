package cn.clickvalue.cv2.components.advertiser;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;

public class CCampaignSearchForm {
    @Persist
    private String campaignName;
    
    @Persist
    private int  actived;
    
    @InjectComponent(value="CApplyingMode")
    private CApplyingMode cApplyingMode;
    
    public String getCampaignName() {
        return this.campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public int getActived() {
        return this.actived;
    }

    public void setActived(int actived) {
        this.actived = actived;
    }

    public CApplyingMode getCApplyingMode() {
        return cApplyingMode;
    }

    public void setCApplyingMode(CApplyingMode applyingMode) {
        cApplyingMode = applyingMode;
    }
}
