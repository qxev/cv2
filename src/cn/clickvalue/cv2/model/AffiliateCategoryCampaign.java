package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AffiliateCategoryCampaign extends PersistentObject {
    
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliateCategoryId")
    private AffiliateCategory affiliateCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaignId")
    private Campaign campaign;

	private Integer categoryOrder;
	
    public AffiliateCategoryCampaign() {
    }

    public AffiliateCategory getAffiliateCategory() {
        return affiliateCategory;
    }

    public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
        this.affiliateCategory = affiliateCategory;
    }

    public Integer getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }
    
    public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}