package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CampaignSite extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siteId")
	private Site site;

	private Integer verified;

	public CampaignSite() {
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public Site getSite() {
		return site;
	}

	public Integer getVerified() {
		return this.verified;
	}

	public String getVerifiedDisplay(){
		String str = "";
		switch (verified) {
			case 0:
				str = "未提交";
				break;
			case 1 :
				str = "申请下线";
				break;
			case 2 :
				str = "已批准";
				break;
			case 3 :
				str = "已拒绝";
				break;
			case 4 :
				str = "申请下线";
				break;
		}
		return str;
	}
	
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}
	


}