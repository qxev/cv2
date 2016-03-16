package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AffiliateTrackData extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer advertiseAffiliateId;

    private String trackCode;

    private Integer campaignId;

    private Integer affiliateId;

    private Integer siteId;

    private String subSiteId;

    private Integer dispalyType;

    private Integer ruleType;

    private Integer trackStatus;

    private Integer trackStep;

    private String orderinfo;

    private String trackIp;

    private String trackUser;

    private String referrerUrl;

    private Date trackTime;
    
    private Long dataTotal;

    public AffiliateTrackData() {
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

    public Integer getAffiliateId() {
        return this.affiliateId;
    }

    public void setAffiliateId(Integer affiliateId) {
        this.affiliateId = affiliateId;
    }

    public Integer getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSubSiteId() {
        return this.subSiteId;
    }

    public void setSubSiteId(String subSiteId) {
        this.subSiteId = subSiteId;
    }

    public Integer getDispalyType() {
        return this.dispalyType;
    }

    public void setDispalyType(Integer dispalyType) {
        this.dispalyType = dispalyType;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getTrackStatus() {
        return this.trackStatus;
    }

    public void setTrackStatus(Integer trackStatus) {
        this.trackStatus = trackStatus;
    }

    public Integer getTrackStep() {
        return this.trackStep;
    }

    public void setTrackStep(Integer trackStep) {
        this.trackStep = trackStep;
    }

    public String getOrderinfo() {
        return this.orderinfo;
    }

    public void setOrderinfo(String orderinfo) {
        this.orderinfo = orderinfo;
    }

    public String getTrackIp() {
        return this.trackIp;
    }

    public void setTrackIp(String trackIp) {
        this.trackIp = trackIp;
    }

    public String getTrackUser() {
        return this.trackUser;
    }

    public void setTrackUser(String trackUser) {
        this.trackUser = trackUser;
    }

    public String getReferrerUrl() {
        return this.referrerUrl;
    }

    public void setReferrerUrl(String referrerUrl) {
        this.referrerUrl = referrerUrl;
    }

    public Date getTrackTime() {
        return this.trackTime;
    }

    public void setTrackTime(Date trackTime) {
        this.trackTime = trackTime;
    }

    public Long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(Long dataTotal) {
        this.dataTotal = dataTotal;
    }
}