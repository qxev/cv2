package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "report_summary")
public class ReportSummary extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer advertiserId;

	private String advertiserName;

	private Integer campaignId;

	private String campaignName;

	private Integer affiliateId;

	private String affiliateName;

	private Integer siteId;

	private String siteName;

	private String siteUrl;

	private String subSiteId;

	private Integer advertiseAffiliateId;

	private Integer bannerId;

	private String bannerDescription;

	private Integer landingPageId;

	private String landingPageUrl;

	private Date summaryDate;
	
	private Integer matched;

	@Column(name="cpm_count_old")
	private Integer cpmCountOld;
	
	@Column(name="cpm_count_new")
	private Integer cpmCountNew;

	@Column(name="cpm_sitecommision_old")
	private Float cpmSitecommisionOld;

	@Column(name="cpm_sitecommision_new")
	private Float cpmSitecommisionNew;

	@Column(name="cpm_darwcommision_old")
	private Float cpmDarwcommisionOld;

	@Column(name="cpm_darwcommision_new")
	private Float cpmDarwcommisionNew;

	@Column(name="cpc_count_old")
	private Integer cpcCountOld;

	@Column(name="cpc_count_new")
	private Integer cpcCountNew;

	@Column(name="cpc_sitecommision_old")
	private Float cpcSitecommisionOld;

	@Column(name="cpc_sitecommision_new")
	private Float cpcSitecommisionNew;

	@Column(name="cpc_darwcommision_old")
	private Float cpcDarwcommisionOld;

	@Column(name="cpc_darwcommision_new")
	private Float cpcDarwcommisionNew;

	@Column(name="cpl_count_old")
	private Integer cplCountOld;

	@Column(name="cpl_count_new")
	private Integer cplCountNew;

	@Column(name="cpl_sitecommision_old")
	private Float cplSitecommisionOld;

	@Column(name="cpl_sitecommision_new")
	private Float cplSitecommisionNew;

	@Column(name="cpl_darwcommision_old")
	private Float cplDarwcommisionOld;

	@Column(name="cpl_darwcommision_new")
	private Float cplDarwcommisionNew;

	@Column(name="cps_count_old")
	private Integer cpsCountOld;

	@Column(name="cps_count_new")
	private Integer cpsCountNew;

	@Column(name="cps_sitecommision_old")
	private Float cpsSitecommisionOld;

	@Column(name="cps_sitecommision_new")
	private Float cpsSitecommisionNew;

	@Column(name="cps_darwcommision_old")
	private Float cpsDarwcommisionOld;

	@Column(name="cps_darwcommision_new")
	private Float cpsDarwcommisionNew;

	@Column(name="sitecommision_total_old")
	private Float sitecommisionTotalOld;

	@Column(name="sitecommision_total_new")
	private Float sitecommisionTotalNew;

	@Column(name="darwcommision_total_old")
	private Float darwcommisionTotalOld;

	@Column(name="darwcommision_total_new")
	private Float darwcommisionTotalNew;

	public ReportSummary() {
	}

	public Integer getAdvertiserId() {
		return this.advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserName() {
		return this.advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public Integer getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return this.campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getAffiliateName() {
		return this.affiliateName;
	}

	public void setAffiliateName(String affiliateName) {
		this.affiliateName = affiliateName;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getSubSiteId() {
		return this.subSiteId;
	}

	public void setSubSiteId(String subSiteId) {
		this.subSiteId = subSiteId;
	}

	public Integer getAdvertiseAffiliateId() {
		return this.advertiseAffiliateId;
	}

	public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
		this.advertiseAffiliateId = advertiseAffiliateId;
	}

	public Integer getBannerId() {
		return this.bannerId;
	}

	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerDescription() {
		return this.bannerDescription;
	}

	public void setBannerDescription(String bannerDescription) {
		this.bannerDescription = bannerDescription;
	}

	public Integer getLandingPageId() {
		return this.landingPageId;
	}

	public void setLandingPageId(Integer landingPageId) {
		this.landingPageId = landingPageId;
	}

	public String getLandingPageUrl() {
		return this.landingPageUrl;
	}

	public void setLandingPageUrl(String landingPageUrl) {
		this.landingPageUrl = landingPageUrl;
	}

	public Date getSummaryDate() {
		return this.summaryDate;
	}

	public void setSummaryDate(Date summaryDate) {
		this.summaryDate = summaryDate;
	}

	public Integer getCpmCountOld() {
		return this.cpmCountOld;
	}

	public void setCpmCountOld(Integer cpmCountOld) {
		this.cpmCountOld = cpmCountOld;
	}

	public Integer getCpmCountNew() {
		return this.cpmCountNew;
	}

	public void setCpmCountNew(Integer cpmCountNew) {
		this.cpmCountNew = cpmCountNew;
	}

	public Float getCpmSitecommisionOld() {
		return this.cpmSitecommisionOld;
	}

	public void setCpmSitecommisionOld(Float cpmSitecommisionOld) {
		this.cpmSitecommisionOld = cpmSitecommisionOld;
	}

	public Float getCpmSitecommisionNew() {
		return this.cpmSitecommisionNew;
	}

	public void setCpmSitecommisionNew(Float cpmSitecommisionNew) {
		this.cpmSitecommisionNew = cpmSitecommisionNew;
	}

	public Float getCpmDarwcommisionOld() {
		return this.cpmDarwcommisionOld;
	}

	public void setCpmDarwcommisionOld(Float cpmDarwcommisionOld) {
		this.cpmDarwcommisionOld = cpmDarwcommisionOld;
	}

	public Float getCpmDarwcommisionNew() {
		return this.cpmDarwcommisionNew;
	}

	public void setCpmDarwcommisionNew(Float cpmDarwcommisionNew) {
		this.cpmDarwcommisionNew = cpmDarwcommisionNew;
	}

	public Integer getCpcCountOld() {
		return this.cpcCountOld;
	}

	public void setCpcCountOld(Integer cpcCountOld) {
		this.cpcCountOld = cpcCountOld;
	}

	public Integer getCpcCountNew() {
		return this.cpcCountNew;
	}

	public void setCpcCountNew(Integer cpcCountNew) {
		this.cpcCountNew = cpcCountNew;
	}

	public Float getCpcSitecommisionOld() {
		return this.cpcSitecommisionOld;
	}

	public void setCpcSitecommisionOld(Float cpcSitecommisionOld) {
		this.cpcSitecommisionOld = cpcSitecommisionOld;
	}

	public Float getCpcSitecommisionNew() {
		return this.cpcSitecommisionNew;
	}

	public void setCpcSitecommisionNew(Float cpcSitecommisionNew) {
		this.cpcSitecommisionNew = cpcSitecommisionNew;
	}

	public Float getCpcDarwcommisionOld() {
		return this.cpcDarwcommisionOld;
	}

	public void setCpcDarwcommisionOld(Float cpcDarwcommisionOld) {
		this.cpcDarwcommisionOld = cpcDarwcommisionOld;
	}

	public Float getCpcDarwcommisionNew() {
		return this.cpcDarwcommisionNew;
	}

	public void setCpcDarwcommisionNew(Float cpcDarwcommisionNew) {
		this.cpcDarwcommisionNew = cpcDarwcommisionNew;
	}

	public Integer getCplCountOld() {
		return this.cplCountOld;
	}

	public void setCplCountOld(Integer cplCountOld) {
		this.cplCountOld = cplCountOld;
	}

	public Integer getCplCountNew() {
		return this.cplCountNew;
	}

	public void setCplCountNew(Integer cplCountNew) {
		this.cplCountNew = cplCountNew;
	}

	public Float getCplSitecommisionOld() {
		return this.cplSitecommisionOld;
	}

	public void setCplSitecommisionOld(Float cplSitecommisionOld) {
		this.cplSitecommisionOld = cplSitecommisionOld;
	}

	public Float getCplSitecommisionNew() {
		return this.cplSitecommisionNew;
	}

	public void setCplSitecommisionNew(Float cplSitecommisionNew) {
		this.cplSitecommisionNew = cplSitecommisionNew;
	}

	public Float getCplDarwcommisionOld() {
		return this.cplDarwcommisionOld;
	}

	public void setCplDarwcommisionOld(Float cplDarwcommisionOld) {
		this.cplDarwcommisionOld = cplDarwcommisionOld;
	}

	public Float getCplDarwcommisionNew() {
		return this.cplDarwcommisionNew;
	}

	public void setCplDarwcommisionNew(Float cplDarwcommisionNew) {
		this.cplDarwcommisionNew = cplDarwcommisionNew;
	}

	public Integer getCpsCountOld() {
		return this.cpsCountOld;
	}

	public void setCpsCountOld(Integer cpsCountOld) {
		this.cpsCountOld = cpsCountOld;
	}

	public Integer getCpsCountNew() {
		return this.cpsCountNew;
	}

	public void setCpsCountNew(Integer cpsCountNew) {
		this.cpsCountNew = cpsCountNew;
	}

	public Float getCpsSitecommisionOld() {
		return this.cpsSitecommisionOld;
	}

	public void setCpsSitecommisionOld(Float cpsSitecommisionOld) {
		this.cpsSitecommisionOld = cpsSitecommisionOld;
	}

	public Float getCpsSitecommisionNew() {
		return this.cpsSitecommisionNew;
	}

	public void setCpsSitecommisionNew(Float cpsSitecommisionNew) {
		this.cpsSitecommisionNew = cpsSitecommisionNew;
	}

	public Float getCpsDarwcommisionOld() {
		return this.cpsDarwcommisionOld;
	}

	public void setCpsDarwcommisionOld(Float cpsDarwcommisionOld) {
		this.cpsDarwcommisionOld = cpsDarwcommisionOld;
	}

	public Float getCpsDarwcommisionNew() {
		return this.cpsDarwcommisionNew;
	}

	public void setCpsDarwcommisionNew(Float cpsDarwcommisionNew) {
		this.cpsDarwcommisionNew = cpsDarwcommisionNew;
	}

	public Float getSitecommisionTotalOld() {
		return this.sitecommisionTotalOld;
	}

	public void setSitecommisionTotalOld(Float sitecommisionTotalOld) {
		this.sitecommisionTotalOld = sitecommisionTotalOld;
	}

	public Float getSitecommisionTotalNew() {
		return this.sitecommisionTotalNew;
	}

	public void setSitecommisionTotalNew(Float sitecommisionTotalNew) {
		this.sitecommisionTotalNew = sitecommisionTotalNew;
	}

	public Float getDarwcommisionTotalOld() {
		return this.darwcommisionTotalOld;
	}

	public void setDarwcommisionTotalOld(Float darwcommisionTotalOld) {
		this.darwcommisionTotalOld = darwcommisionTotalOld;
	}

	public Float getDarwcommisionTotalNew() {
		return this.darwcommisionTotalNew;
	}

	public void setDarwcommisionTotalNew(Float darwcommisionTotalNew) {
		this.darwcommisionTotalNew = darwcommisionTotalNew;
	}

    public Integer getMatched() {
        return matched;
    }

    public void setMatched(Integer matched) {
        this.matched = matched;
    }
}