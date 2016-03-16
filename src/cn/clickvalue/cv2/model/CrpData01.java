package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "crp_data01")
public class CrpData01 extends PersistentObject {
    
    private static final long serialVersionUID = 1L;

    private String advertiserId;

    private String advertiserName;

    private String campaignId;

    private String campaignName;

    private String publisherId;

    private String publisherName;

    private String siteId;

    private String siteName;

    private String siteUrl;

    private String bannerId;

    private String bannerName;

    private String landingId;

    private String landingName;

    private String landingUrl;

    private String subSiteId;

    @Column(name="report_date")
    private Date reportDate;

    @Column(name="report_start")
    private Date reportStart;

    @Column(name="report_end")
    private Date reportEnd;

    private String dataName;

    private String stringValue;

    private Integer longValue;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="updated_date")
    private Date updatedDate;

    @Column(name="report_type")
    private String reportType;

    @Column(name="row_mark")
    private Integer rowMark;

    public CrpData01() {}

    public String getAdvertiserId() {
        return this.advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getAdvertiserName() {
        return this.advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return this.campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getPublisherId() {
        return this.publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return this.publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public void setSiteId(String siteId) {
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

    public String getBannerId() {
        return this.bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return this.bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getLandingId() {
        return this.landingId;
    }

    public void setLandingId(String landingId) {
        this.landingId = landingId;
    }

    public String getLandingName() {
        return this.landingName;
    }

    public void setLandingName(String landingName) {
        this.landingName = landingName;
    }

    public String getLandingUrl() {
        return this.landingUrl;
    }

    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }

    public String getSubSiteId() {
        return this.subSiteId;
    }

    public void setSubSiteId(String subSiteId) {
        this.subSiteId = subSiteId;
    }

    public Date getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getReportStart() {
        return this.reportStart;
    }

    public void setReportStart(Date reportStart) {
        this.reportStart = reportStart;
    }

    public Date getReportEnd() {
        return this.reportEnd;
    }

    public void setReportEnd(Date reportEnd) {
        this.reportEnd = reportEnd;
    }

    public String getDataName() {
        return this.dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getLongValue() {
        return this.longValue;
    }

    public void setLongValue(Integer longValue) {
        this.longValue = longValue;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getReportType() {
        return this.reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getRowMark() {
        return this.rowMark;
    }

    public void setRowMark(Integer rowMark) {
        this.rowMark = rowMark;
    }

}