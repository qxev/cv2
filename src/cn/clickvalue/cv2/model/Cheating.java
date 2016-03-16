package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class Cheating extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User affiliate;

	private Integer siteId;

	private String siteUrl;

	private Integer campaignId;

	private String campaignName;

	private String description;

	private Date cheatBeginAt;

	private Date cheatEndAt;
	
	private Integer deleted;

	public User getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(User affiliate) {
		this.affiliate = affiliate;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCheatBeginAt() {
		return cheatBeginAt;
	}

	public void setCheatBeginAt(Date cheatBeginAt) {
		this.cheatBeginAt = cheatBeginAt;
	}

	public Date getCheatEndAt() {
		return cheatEndAt;
	}

	public void setCheatEndAt(Date cheatEndAt) {
		this.cheatEndAt = cheatEndAt;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
