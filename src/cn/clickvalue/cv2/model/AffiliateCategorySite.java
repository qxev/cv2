package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AffiliateCategorySite extends PersistentObject {
    
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliateCategoryId")
    private AffiliateCategory affiliateCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteId")
    private Site site;
    
	private Integer categoryOrder;

    public AffiliateCategorySite() {
    }

    public AffiliateCategory getAffiliateCategory() {
        return affiliateCategory;
    }

    public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
        this.affiliateCategory = affiliateCategory;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Integer getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }
}