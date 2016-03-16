package cn.clickvalue.cv2.model;

import java.util.Date;

import cn.clickvalue.cv2.common.util.DateUtil;

public class Report implements java.io.Serializable{
    
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
    
    // count
    private Integer sumCpmCountOld;
    private Integer sumCpmCountNew;
    
    private Integer sumCpcCountOld;
    private Integer sumCpcCountNew;
    
    private Integer sumCplCountOld;
    private Integer sumCplCountNew;
    
    private Integer sumCpsCountOld;
    private Integer sumCpsCountNew;
    
    //网站佣金
    private Float sumCpmSitecommisionOld;
    private Float sumCpmSitecommisionNew;
    
    private Float sumCpcSitecommisionOld;
    private Float sumCpcSitecommisionNew;
    
    private Float sumCplSitecommisionOld;
    private Float sumCplSitecommisionNew;
    
    private Float sumCpsSitecommisionOld;
    private Float sumCpsSitecommisionNew;
    
    //达闻佣金
    private Float sumCpmDarwcommisionOld;
    private Float sumCpmDarwcommisionNew;
    
    private Float sumCpcDarwcommisionOld;
    private Float sumCpcDarwcommisionNew;
    
    private Float sumCplDarwcommisionOld;
    private Float sumCplDarwcommisionNew;
    
    private Float sumCpsDarwcommisionOld;
    private Float sumCpsDarwcommisionNew;
    
    //网站佣金合计
    private Float sumSitecommisionTotalOld;
    private Float sumSitecommisionTotalNew;

    //达闻佣金合计
    private Float sumDarwcommisionTotalOld;
    private Float sumDarwcommisionTotalNew;
    
    // 网站+达闻佣金
    private Float cpcCommisionOld;
    private Float cpcCommisionNew;
    
    private Float cplCommisionOld;
    private Float cplCommisionNew;
    
    private Float cpsCommisionOld;
    private Float cpsCommisionNew;
    
    private Float cpmCommisionOld;
    private Float cpmCommisionNew;
    
    private Float totalCommisionOld;
    private Float totalCommisionNew;
    
    
    public Integer getAdvertiserId() {
        return advertiserId;
    }
    public void setAdvertiserId(Integer advertiserId) {
        this.advertiserId = advertiserId;
    }
    public String getAdvertiserName() {
        return advertiserName;
    }
    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
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
    public Integer getAffiliateId() {
        return affiliateId;
    }
    public void setAffiliateId(Integer affiliateId) {
        this.affiliateId = affiliateId;
    }
    public String getAffiliateName() {
        return affiliateName;
    }
    public void setAffiliateName(String affiliateName) {
        this.affiliateName = affiliateName;
    }
    public Integer getSiteId() {
        return siteId;
    }
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    public String getSiteUrl() {
        return siteUrl;
    }
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }
    public String getSubSiteId() {
        return subSiteId;
    }
    public void setSubSiteId(String subSiteId) {
        this.subSiteId = subSiteId;
    }
    public Integer getAdvertiseAffiliateId() {
        return advertiseAffiliateId;
    }
    public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
        this.advertiseAffiliateId = advertiseAffiliateId;
    }
    public Integer getBannerId() {
        return bannerId;
    }
    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }
    public String getBannerDescription() {
        return bannerDescription;
    }
    public void setBannerDescription(String bannerDescription) {
        this.bannerDescription = bannerDescription;
    }
    public Integer getLandingPageId() {
        return landingPageId;
    }
    public void setLandingPageId(Integer landingPageId) {
        this.landingPageId = landingPageId;
    }
    public String getLandingPageUrl() {
        return landingPageUrl;
    }
    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
    }
    public Date getSummaryDate() {
        return summaryDate;
    }
    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }
    
    public String getSummaryDateDisplay(){
    	if (summaryDate!=null)
			return DateUtil.dateToString(summaryDate);
		else 
			return "";
    }
    
    public Integer getSumCpmCountOld() {
        return sumCpmCountOld;
    }
    public void setSumCpmCountOld(Integer sumCpmCountOld) {
        this.sumCpmCountOld = sumCpmCountOld;
    }
    public Integer getSumCpmCountNew() {
        return sumCpmCountNew;
    }
    public void setSumCpmCountNew(Integer sumCpmCountNew) {
        this.sumCpmCountNew = sumCpmCountNew;
    }
    public Integer getSumCpcCountOld() {
        return sumCpcCountOld;
    }
    public void setSumCpcCountOld(Integer sumCpcCountOld) {
        this.sumCpcCountOld = sumCpcCountOld;
    }
    public Integer getSumCpcCountNew() {
        return sumCpcCountNew;
    }
    public void setSumCpcCountNew(Integer sumCpcCountNew) {
        this.sumCpcCountNew = sumCpcCountNew;
    }
    public Integer getSumCplCountOld() {
        return sumCplCountOld;
    }
    public void setSumCplCountOld(Integer sumCplCountOld) {
        this.sumCplCountOld = sumCplCountOld;
    }
    public Integer getSumCplCountNew() {
        return sumCplCountNew;
    }
    public void setSumCplCountNew(Integer sumCplCountNew) {
        this.sumCplCountNew = sumCplCountNew;
    }
    public Integer getSumCpsCountOld() {
        return sumCpsCountOld;
    }
    public void setSumCpsCountOld(Integer sumCpsCountOld) {
        this.sumCpsCountOld = sumCpsCountOld;
    }
    public Integer getSumCpsCountNew() {
        return sumCpsCountNew;
    }
    public void setSumCpsCountNew(Integer sumCpsCountNew) {
        this.sumCpsCountNew = sumCpsCountNew;
    }
    public Float getSumCpmSitecommisionOld() {
        return sumCpmSitecommisionOld;
    }
    public void setSumCpmSitecommisionOld(Float sumCpmSitecommisionOld) {
        this.sumCpmSitecommisionOld = sumCpmSitecommisionOld;
    }
    public Float getSumCpmSitecommisionNew() {
        return sumCpmSitecommisionNew;
    }
    public void setSumCpmSitecommisionNew(Float sumCpmSitecommisionNew) {
        this.sumCpmSitecommisionNew = sumCpmSitecommisionNew;
    }
    public Float getSumCpcSitecommisionOld() {
        return sumCpcSitecommisionOld;
    }
    public void setSumCpcSitecommisionOld(Float sumCpcSitecommisionOld) {
        this.sumCpcSitecommisionOld = sumCpcSitecommisionOld;
    }
    public Float getSumCpcSitecommisionNew() {
        return sumCpcSitecommisionNew;
    }
    public void setSumCpcSitecommisionNew(Float sumCpcSitecommisionNew) {
        this.sumCpcSitecommisionNew = sumCpcSitecommisionNew;
    }
    public Float getSumCplSitecommisionOld() {
        return sumCplSitecommisionOld;
    }
    public void setSumCplSitecommisionOld(Float sumCplSitecommisionOld) {
        this.sumCplSitecommisionOld = sumCplSitecommisionOld;
    }
    public Float getSumCplSitecommisionNew() {
        return sumCplSitecommisionNew;
    }
    public void setSumCplSitecommisionNew(Float sumCplSitecommisionNew) {
        this.sumCplSitecommisionNew = sumCplSitecommisionNew;
    }
    public Float getSumCpsSitecommisionOld() {
        return sumCpsSitecommisionOld;
    }
    public void setSumCpsSitecommisionOld(Float sumCpsSitecommisionOld) {
        this.sumCpsSitecommisionOld = sumCpsSitecommisionOld;
    }
    public Float getSumCpsSitecommisionNew() {
        return sumCpsSitecommisionNew;
    }
    public void setSumCpsSitecommisionNew(Float sumCpsSitecommisionNew) {
        this.sumCpsSitecommisionNew = sumCpsSitecommisionNew;
    }
    public Float getSumCpmDarwcommisionOld() {
        return sumCpmDarwcommisionOld;
    }
    public void setSumCpmDarwcommisionOld(Float sumCpmDarwcommisionOld) {
        this.sumCpmDarwcommisionOld = sumCpmDarwcommisionOld;
    }
    public Float getSumCpmDarwcommisionNew() {
        return sumCpmDarwcommisionNew;
    }
    public void setSumCpmDarwcommisionNew(Float sumCpmDarwcommisionNew) {
        this.sumCpmDarwcommisionNew = sumCpmDarwcommisionNew;
    }
    public Float getSumCpcDarwcommisionOld() {
        return sumCpcDarwcommisionOld;
    }
    public void setSumCpcDarwcommisionOld(Float sumCpcDarwcommisionOld) {
        this.sumCpcDarwcommisionOld = sumCpcDarwcommisionOld;
    }
    public Float getSumCpcDarwcommisionNew() {
        return sumCpcDarwcommisionNew;
    }
    public void setSumCpcDarwcommisionNew(Float sumCpcDarwcommisionNew) {
        this.sumCpcDarwcommisionNew = sumCpcDarwcommisionNew;
    }
    public Float getSumCplDarwcommisionOld() {
        return sumCplDarwcommisionOld;
    }
    public void setSumCplDarwcommisionOld(Float sumCplDarwcommisionOld) {
        this.sumCplDarwcommisionOld = sumCplDarwcommisionOld;
    }
    public Float getSumCplDarwcommisionNew() {
        return sumCplDarwcommisionNew;
    }
    public void setSumCplDarwcommisionNew(Float sumCplDarwcommisionNew) {
        this.sumCplDarwcommisionNew = sumCplDarwcommisionNew;
    }
    public Float getSumCpsDarwcommisionOld() {
        return sumCpsDarwcommisionOld;
    }
    public void setSumCpsDarwcommisionOld(Float sumCpsDarwcommisionOld) {
        this.sumCpsDarwcommisionOld = sumCpsDarwcommisionOld;
    }
    public Float getSumCpsDarwcommisionNew() {
        return sumCpsDarwcommisionNew;
    }
    public void setSumCpsDarwcommisionNew(Float sumCpsDarwcommisionNew) {
        this.sumCpsDarwcommisionNew = sumCpsDarwcommisionNew;
    }
    public Float getSumSitecommisionTotalOld() {
        return sumSitecommisionTotalOld;
    }
    public void setSumSitecommisionTotalOld(Float sumSitecommisionTotalOld) {
        this.sumSitecommisionTotalOld = sumSitecommisionTotalOld;
    }
    public Float getSumSitecommisionTotalNew() {
        return sumSitecommisionTotalNew;
    }
    public void setSumSitecommisionTotalNew(Float sumSitecommisionTotalNew) {
        this.sumSitecommisionTotalNew = sumSitecommisionTotalNew;
    }
    public Float getSumDarwcommisionTotalOld() {
        return sumDarwcommisionTotalOld;
    }
    public void setSumDarwcommisionTotalOld(Float sumDarwcommisionTotalOld) {
        this.sumDarwcommisionTotalOld = sumDarwcommisionTotalOld;
    }
    public Float getSumDarwcommisionTotalNew() {
        return sumDarwcommisionTotalNew;
    }
    public void setSumDarwcommisionTotalNew(Float sumDarwcommisionTotalNew) {
        this.sumDarwcommisionTotalNew = sumDarwcommisionTotalNew;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public Float getTotalCommisionOld() {
        return totalCommisionOld;
    }
    public void setTotalCommisionOld(Float totalCommisionOld) {
        this.totalCommisionOld = totalCommisionOld;
    }
    public Float getTotalCommisionNew() {
        return totalCommisionNew;
    }
    public void setTotalCommisionNew(Float totalCommisionNew) {
        this.totalCommisionNew = totalCommisionNew;
    }
    public Float getCpcCommisionOld() {
        return cpcCommisionOld;
    }
    public void setCpcCommisionOld(Float cpcCommisionOld) {
        this.cpcCommisionOld = cpcCommisionOld;
    }
    public Float getCpcCommisionNew() {
        return cpcCommisionNew;
    }
    public void setCpcCommisionNew(Float cpcCommisionNew) {
        this.cpcCommisionNew = cpcCommisionNew;
    }
    public Float getCplCommisionOld() {
        return cplCommisionOld;
    }
    public void setCplCommisionOld(Float cplCommisionOld) {
        this.cplCommisionOld = cplCommisionOld;
    }
    public Float getCplCommisionNew() {
        return cplCommisionNew;
    }
    public void setCplCommisionNew(Float cplCommisionNew) {
        this.cplCommisionNew = cplCommisionNew;
    }
    public Float getCpsCommisionOld() {
        return cpsCommisionOld;
    }
    public void setCpsCommisionOld(Float cpsCommisionOld) {
        this.cpsCommisionOld = cpsCommisionOld;
    }
    public Float getCpsCommisionNew() {
        return cpsCommisionNew;
    }
    public void setCpsCommisionNew(Float cpsCommisionNew) {
        this.cpsCommisionNew = cpsCommisionNew;
    }
    public Integer getMatched() {
        return matched;
    }
    public void setMatched(Integer matched) {
        this.matched = matched;
    }
	public Float getCpmCommisionOld() {
		return cpmCommisionOld;
	}
	public void setCpmCommisionOld(Float cpmCommisionOld) {
		this.cpmCommisionOld = cpmCommisionOld;
	}
	public Float getCpmCommisionNew() {
		return cpmCommisionNew;
	}
	public void setCpmCommisionNew(Float cpmCommisionNew) {
		this.cpmCommisionNew = cpmCommisionNew;
	}
}
