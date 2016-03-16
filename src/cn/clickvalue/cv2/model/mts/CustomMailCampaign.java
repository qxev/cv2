package cn.clickvalue.cv2.model.mts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import cn.clickvalue.cv2.common.util.UUIDUtil;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.LandingPage;

public class CustomMailCampaign {

	private String id;

	private String title;

	private Integer campaignId;

	private Integer advertiseId;

	private Banner banner;

	private LandingPage landingPage;

	private List<Paragraph> paragraphs;

	public CustomMailCampaign() {
		this.id = UUIDUtil.getUUID();
		paragraphs = new ArrayList<Paragraph>();
	}

	public boolean isValid() {
		if (this.advertiseId == null || this.advertiseId == 0) {
			return false;
		}
		if (StringUtils.isBlank(title)) {
			return false;
		}
		return true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getAdvertiseId() {
		return advertiseId;
	}

	public void setAdvertiseId(Integer advertiseId) {
		this.advertiseId = advertiseId;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public LandingPage getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(LandingPage landingPage) {
		this.landingPage = landingPage;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof CustomMailCampaign == false) {
			return false;
		}
		CustomMailCampaign o = (CustomMailCampaign) obj;
		return new EqualsBuilder().append(this.id, o.id).isEquals();
	}

}
