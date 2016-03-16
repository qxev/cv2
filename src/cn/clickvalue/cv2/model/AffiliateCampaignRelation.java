package cn.clickvalue.cv2.model;

import java.io.Serializable;

public class AffiliateCampaignRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private User affiliate;

    private Campaign campaign;

    public AffiliateCampaignRelation() {
    }

    public AffiliateCampaignRelation(User affiliate, Campaign campaign) {
        super();
        this.affiliate = affiliate;
        this.campaign = campaign;
    }

    public User getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(User affiliate) {
        this.affiliate = affiliate;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof AffiliateCampaignRelation))
            return false;

        if (obj == null)
            return false;

        AffiliateCampaignRelation affiliateCampaignRelation = (AffiliateCampaignRelation) obj;
        
        if(this.getCampaign() == null || this.getAffiliate() == null)
            return false;
        
        if (this.getCampaign().equals(affiliateCampaignRelation.getCampaign())
                && this.getAffiliate().equals(affiliateCampaignRelation.getAffiliate())) {
            return true;
        }

        return false;
    }

}
