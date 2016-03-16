package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
@Table(name = "custombannerlog")
public class CustomBannerLog extends PersistentObject {

    private static final long serialVersionUID = 1L;
    
    /**
     * 网站主
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User user;

    /**
     * 广告活动
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
    
    /**
     * 广告类型
     * 1： 图片广告
     * 2： 文字广告
     */
    private String type;

    /**
     * 图片URL/文字信息
     */
    private String content;

    /**
     * 目标页面URL
     */
    private String landpageUrl;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLandpageUrl() {
		return landpageUrl;
	}

	public void setLandpageUrl(String landpageUrl) {
		this.landpageUrl = landpageUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

}