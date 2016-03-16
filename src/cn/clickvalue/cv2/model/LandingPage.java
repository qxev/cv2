package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "landingpage")
@BatchSize(size = 15)
public class LandingPage extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer actived;

	@OneToMany(mappedBy = "landingPage", fetch = FetchType.LAZY)
	private List<Advertise> advertises;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateCategoryId")
	private AffiliateCategory affiliateCategory;

	private Integer deleted;

	private String description;

	private String name;

	private String url;

	private Integer verified;

	public LandingPage() {
	}

	public Integer getActived() {
		return this.actived;
	}

	public List<Advertise> getAdvertises() {
		return advertises;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public Integer getDeleted() {
		return this.deleted;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public String getUrl() {
		return this.url;
	}

	public Integer getVerified() {
		return this.verified;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public void setAdvertises(List<Advertise> advertises) {
		this.advertises = advertises;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
		this.affiliateCategory = affiliateCategory;
	}

	public AffiliateCategory getAffiliateCategory() {
		return affiliateCategory;
	}
}