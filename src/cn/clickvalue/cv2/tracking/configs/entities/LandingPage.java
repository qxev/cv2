package cn.clickvalue.cv2.tracking.configs.entities;

import java.io.Serializable;
import java.util.Date;

public class LandingPage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer campaignId;

    private Integer affiliateCategoryId;

    private Integer actived;

    private Integer deleted;

    private Integer verified;

    private String url;

    private String name;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    public LandingPage() {
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

    public Integer getAffiliateCategoryId() {
        return this.affiliateCategoryId;
    }

    public void setAffiliateCategoryId(Integer affiliateCategoryId) {
        this.affiliateCategoryId = affiliateCategoryId;
    }

    public Integer getActived() {
        return this.actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getVerified() {
        return this.verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}