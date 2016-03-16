package cn.clickvalue.cv2.tracking.configs.entities;

import java.io.Serializable;
import java.util.Date;

public class Banner implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer campaignId;

    private String name;

    private String bannerType;

    private Integer actived;

    private Integer deleted;

    private Integer verified;

    private String content;

    private Integer width;

    private Integer height;

    private Date createdAt;

    private Date updatedAt;

    public Banner() {
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerType() {
        return this.bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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