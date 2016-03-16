package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class SummaryDimension extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer advertiseAffiliateId;

    private String trackCode;

    private Integer campaignId;

    private String campaignName;

    private Integer ruleGroupId;

    private String ruleGroupName;

    private Integer bannerId;

    private String bannerDescription;

    private Integer landingPageId;

    private String landingPageUrl;

    private Integer affiliateId;

    private String affiliateName;

    private Integer advertiserId;

    private String advertiserName;

    private Integer siteId;

    private String siteName;

    public SummaryDimension() {
    }

    public Integer getAdvertiseAffiliateId() {
        return this.advertiseAffiliateId;
    }

    public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
        this.advertiseAffiliateId = advertiseAffiliateId;
    }

    public String getTrackCode() {
        return this.trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
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

    public Integer getRuleGroupId() {
        return this.ruleGroupId;
    }

    public void setRuleGroupId(Integer ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }

    public String getRuleGroupName() {
        return this.ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
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
}