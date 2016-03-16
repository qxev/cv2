package cn.clickvalue.cv2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Table(name = "campaign")
public class Campaign extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer actived;

	private Integer cookieMaxage;

	private String region;

	private Integer deleted;

	private String description;

	private Date endDate;

	private String name;

	private String parameters;

	private Date startDate;

	private Integer verified;

	private String cpa;

	private Integer enforced;

	private Integer affiliateVerified;

	private Integer customVerified;

	// partnerType=1时，传给广告主的subId为partnerId表中type＝campaign.id and
	// ourId=site.id时对应的partnerId
	@Column(name = "partnerId")
	private Integer partnerType;

	private float confirmationRate;

	private Integer htmlCode;

	private Integer rank;

	private Integer bbsId;

	private String industry;

	private String industrySubseries;
	
	private Integer intrust;

	public Campaign() {
	}

	// hibernate使用策略 勿删
	public Campaign(Integer id, String name) {
		this.setId(id);
		this.setName(name);
	}

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AffiliateCategoryCampaign> affiliateCategoryCampaigns;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siteId")
	private Site site;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Banner> banners;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CampaignSite> campaignSites;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CommissionRule> commissionRules;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CampaignHistory> campaignHistorys;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AffiliateCampaign> affiliateCampaigns;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<LandingPage> landingPages;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Bonus> Bonuses;

	@OneToMany(mappedBy = "campaign")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CommissionIncome> commissionIncomes;

	public Integer getActived() {
		return this.actived;
	}

	public List<Banner> getBanners() {
		return banners;
	}

	public List<CampaignSite> getCampaignSites() {
		return campaignSites;
	}

	public List<CommissionRule> getCommissionRules() {
		return commissionRules;
	}

	public Integer getCookieMaxage() {
		return this.cookieMaxage;
	}

	public Integer getDeleted() {
		return this.deleted;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public List<LandingPage> getLandingPages() {
		return landingPages;
	}

	public String getName() {
		return this.name;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public User getUser() {
		return user;
	}

	public Integer getVerified() {
		return this.verified;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public void setBanners(List<Banner> banners) {
		this.banners = banners;
	}

	public void setCampaignSites(List<CampaignSite> campaignSites) {
		this.campaignSites = campaignSites;
	}

	public void setCommissionRules(List<CommissionRule> commissionRules) {
		this.commissionRules = commissionRules;
	}

	public void setCookieMaxage(Integer cookieMaxage) {
		this.cookieMaxage = cookieMaxage;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLandingPages(List<LandingPage> landingPages) {
		this.landingPages = landingPages;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public String getCpa() {
		return cpa;
	}

	public void setCpa(String cpa) {
		this.cpa = cpa;
	}

	public String getTest() {
		return "test";
	}

	public List<AffiliateCampaign> getAffiliateCampaigns() {
		return affiliateCampaigns;
	}

	public void setAffiliateCampaigns(List<AffiliateCampaign> affiliateCampaigns) {
		this.affiliateCampaigns = affiliateCampaigns;
	}

	public Integer getEnforced() {
		return enforced;
	}

	public void setEnforced(Integer enforce) {
		this.enforced = enforce;
	}

	public Integer getAffiliateVerified() {
		return affiliateVerified;
	}

	public void setAffiliateVerified(Integer affiliateVerified) {
		this.affiliateVerified = affiliateVerified;
	}

	public float getConfirmationRate() {
		return confirmationRate;
	}

	public void setConfirmationRate(float confirmationRate) {
		this.confirmationRate = confirmationRate;
	}

	public List<CampaignHistory> getCampaignHistorys() {
		return campaignHistorys;
	}

	public void setCampaignHistorys(List<CampaignHistory> campaignHistorys) {
		this.campaignHistorys = campaignHistorys;
	}

	public void setBonuses(List<Bonus> bonuses) {
		Bonuses = bonuses;
	}

	public List<Bonus> getBonuses() {
		return Bonuses;
	}

	public void setCommissionIncomes(List<CommissionIncome> commissionIncomes) {
		this.commissionIncomes = commissionIncomes;
	}

	public List<CommissionIncome> getCommissionIncomes() {
		return commissionIncomes;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Integer getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(Integer htmlCode) {
		this.htmlCode = htmlCode;
	}

	public Integer getRank() {
		return (rank == 1000) ? 0 : rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getBbsId() {
		return bbsId;
	}

	public void setBbsId(Integer bbsId) {
		this.bbsId = bbsId;
	}

	public List<AffiliateCategoryCampaign> getAffiliateCategoryCampaigns() {
		return affiliateCategoryCampaigns;
	}

	public void setAffiliateCategoryCampaigns(List<AffiliateCategoryCampaign> affiliateCategoryCampaigns) {
		this.affiliateCategoryCampaigns = affiliateCategoryCampaigns;
	}

	public Integer getCustomVerified() {
		return customVerified;
	}

	public void setCustomVerified(Integer customVerified) {
		this.customVerified = customVerified;
	}

	public Integer getPartnerType() {
		return partnerType == null ? 0 : partnerType;
	}

	public void setPartnerType(Integer partnerType) {
		this.partnerType = partnerType;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustrySubseries() {
		return industrySubseries;
	}

	public void setIndustrySubseries(String industrySubseries) {
		this.industrySubseries = industrySubseries;
	}

	public Integer getIntrust() {
		return intrust;
	}

	public void setIntrust(Integer intrust) {
		this.intrust = intrust;
	}
}