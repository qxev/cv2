package cn.clickvalue.cv2.model;

import java.util.Date;

import cn.clickvalue.cv2.model.base.PersistentObject;


public class SummaryEffect extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer advertiseAffiliateId;

    private String subSiteId;

    private Date summaryDate;

    private Integer trackType;

    private Integer trackStep;

    private Integer impressions;

    private Integer clicks;

    private Integer leads;

    public SummaryEffect() {
    }

    public Integer getAdvertiseAffiliateId() {
        return this.advertiseAffiliateId;
    }

    public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
        this.advertiseAffiliateId = advertiseAffiliateId;
    }

    public String getSubSiteId() {
        return this.subSiteId;
    }

    public void setSubSiteId(String subSiteId) {
        this.subSiteId = subSiteId;
    }

    public Date getSummaryDate() {
        return this.summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

    public Integer getTrackType() {
        return this.trackType;
    }

    public void setTrackType(Integer trackType) {
        this.trackType = trackType;
    }

    public Integer getTrackStep() {
        return this.trackStep;
    }

    public void setTrackStep(Integer trackStep) {
        this.trackStep = trackStep;
    }

    public Integer getImpressions() {
        return this.impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return this.clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getLeads() {
        return this.leads;
    }

    public void setLeads(Integer leads) {
        this.leads = leads;
    }
}