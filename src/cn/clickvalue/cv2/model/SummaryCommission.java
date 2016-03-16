package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "summarycommission")
public class SummaryCommission extends PersistentObject {

	private static final long serialVersionUID = 1L;
	
	private String campaignName;

	private Integer advertiseAffiliateId;

	private Integer affiliateId;
	
	private Integer advertiserId;

	private Integer balanced;

	private Date balanceDate;

	private Integer balanceId;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "campaignId")
//	@Cascade({CascadeType.SAVE_UPDATE})
//	private Campaign campaign;
	
	private Integer campaignId;

	private BigDecimal darwCommisionNew;

	private BigDecimal darwCommisionOld;

	private BigDecimal dataTotalNew;

	private BigDecimal dataTotalOld;

	private BigDecimal orderamountNew;

	private BigDecimal orderamountOld;
	
	private BigDecimal siteCommisionNew;
	
	private Integer ruleType;

	private BigDecimal siteCommisionOld;

	private Integer siteId;

	private String subSiteId;

	private Date summaryDate;
	
	private String siteName;
	
	private String siteUrl;

	public SummaryCommission() {
	}

	public Integer getAdvertiseAffiliateId() {
		return advertiseAffiliateId;
	}

	public Integer getAffiliateId() {
		return affiliateId;
	}
	
	public Integer getBalanced() {
		return balanced;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public Integer getBalanceId() {
		return balanceId;
	}
	
//	public Campaign getCampaign() {
//		return campaign;
//	}
	
	public BigDecimal getDarwCommisionNew() {
		return darwCommisionNew;
	}

	public BigDecimal getDarwCommisionOld() {
		return darwCommisionOld;
	}

	public BigDecimal getDataTotalNew() {
		return dataTotalNew;
	}

	public BigDecimal getDataTotalOld() {
		return dataTotalOld;
	}

	public BigDecimal getOrderamountNew() {
		return orderamountNew;
	}

	public BigDecimal getOrderamountOld() {
		return orderamountOld;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public BigDecimal getSiteCommisionNew() {
		return siteCommisionNew;
	}

	public BigDecimal getSiteCommisionOld() {
		return siteCommisionOld;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public String getSubSiteId() {
		return subSiteId;
	}

	public Date getSummaryDate() {
		return summaryDate;
	}

	public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
		this.advertiseAffiliateId = advertiseAffiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public void setBalanced(Integer balanced) {
		this.balanced = balanced;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public void setBalanceId(Integer balanceId) {
		this.balanceId = balanceId;
	}

//	public void setCampaign(Campaign campaign) {
//		this.campaign = campaign;
//	}

	public void setDarwCommisionNew(BigDecimal darwCommisionNew) {
		this.darwCommisionNew = darwCommisionNew;
	}

	public void setDarwCommisionOld(BigDecimal darwCommisionOld) {
		this.darwCommisionOld = darwCommisionOld;
	}

	public void setDataTotalNew(BigDecimal dataTotalNew) {
		this.dataTotalNew = dataTotalNew;
	}

	public void setDataTotalOld(BigDecimal dataTotalOld) {
		this.dataTotalOld = dataTotalOld;
	}

	public void setOrderamountNew(BigDecimal orderamountNew) {
		this.orderamountNew = orderamountNew;
	}

	public void setOrderamountOld(BigDecimal orderamountOld) {
		this.orderamountOld = orderamountOld;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public void setSiteCommisionNew(BigDecimal siteCommisionNew) {
		this.siteCommisionNew = siteCommisionNew;
	}

	public void setSiteCommisionOld(BigDecimal siteCommisionOld) {
		this.siteCommisionOld = siteCommisionOld;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public void setSubSiteId(String subSiteId) {
		this.subSiteId = subSiteId;
	}

	public void setSummaryDate(Date summaryDate) {
		this.summaryDate = summaryDate;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}
}