package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AdvertiserSumInfo extends PersistentObject {

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private Integer userId;

	/** 广告主id */
	private Integer campaignId;

	/** 广告主名称 */
	private String campaignName;

	/** cpc */
	private Long cpc;

	/** cpm */
	private Long cpm;

	/** cps */
	private Long cps;

	/** cpl */
	private Long cpl;

	/** 站点size */
	private Long sites;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Long getCpc() {
		return cpc;
	}

	public void setCpc(Long cpc) {
		this.cpc = cpc;
	}

	public Long getCpm() {
		return cpm;
	}

	public void setCpm(Long cpm) {
		this.cpm = cpm;
	}

	public Long getCps() {
		return cps;
	}

	public void setCps(Long cps) {
		this.cps = cps;
	}

	public Long getCpl() {
		return cpl;
	}

	public void setCpl(Long cpl) {
		this.cpl = cpl;
	}

	public Long getSites() {
		return sites;
	}

	public void setSites(Long sites) {
		this.sites = sites;
	}
}
