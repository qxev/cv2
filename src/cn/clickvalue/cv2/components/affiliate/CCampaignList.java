package cn.clickvalue.cv2.components.affiliate;

import java.util.List;

import cn.clickvalue.cv2.model.Campaign;

public class CCampaignList {
    private Campaign campaign;
    private List<Campaign> cpsdata;

    public void setCampaigns(List<Campaign> cps) {
        this.cpsdata = cps;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public List<Campaign> getCpsdata() {
        return cpsdata;
    }

    public void setCpsdata(List<Campaign> cpsdata) {
        this.cpsdata = cpsdata;
    }
    
}
