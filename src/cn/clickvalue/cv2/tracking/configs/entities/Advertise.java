package cn.clickvalue.cv2.tracking.configs.entities;

import java.io.Serializable;
import java.util.Date;

public class Advertise implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer campaignId;
    
    private Integer landingPageId;

    private LandingPage landingPage;

    private Integer deleted;
    
    private Integer bannerId;

    private Banner banner;

    private Date createdAt;

    private Date updatedAt;

    public Advertise() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LandingPage getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(LandingPage landingPage) {
        this.landingPage = landingPage;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public Integer getLandingPageId() {
        return landingPageId;
    }

    public void setLandingPageId(Integer landingPageId) {
        this.landingPageId = landingPageId;
    }

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }
}