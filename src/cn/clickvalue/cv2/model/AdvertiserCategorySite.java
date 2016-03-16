package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AdvertiserCategorySite extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advertiserCategoryId")
	private AdvertiserCategory advertiserCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siteId")
	private Site site;

	private Integer categoryOrder;

	/** default constructor */
	public AdvertiserCategorySite() {
	}

	// Property accessors
	public AdvertiserCategory getAdvertiserCategory() {
		return advertiserCategory;
	}

	public void setAdvertiserCategory(AdvertiserCategory advertiserCategory) {
		this.advertiserCategory = advertiserCategory;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Integer getCategoryOrder() {
		return this.categoryOrder;
	}

	public void setCategoryOrder(Integer categoryOrder) {
		this.categoryOrder = categoryOrder;
	}
}