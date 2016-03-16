package cn.clickvalue.cv2.tracking.configs.entities;

import java.io.Serializable;

public class CampaignSite implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer siteId;
	
	private Integer campaignId;
	
	private Integer verified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}
	
	

}
