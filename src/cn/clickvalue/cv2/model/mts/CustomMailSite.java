package cn.clickvalue.cv2.model.mts;

import org.apache.commons.lang.builder.EqualsBuilder;

import cn.clickvalue.cv2.common.util.UUIDUtil;

public class CustomMailSite {

	private String id;

	private String siteName;

	private String siteUrl;

	private String logo;

	private String description;

	public CustomMailSite() {
		this.id = UUIDUtil.getUUID();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName == null ? "" : siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl == null ? "" : siteUrl;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? "" : description;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof CustomMailSite == false) {
			return false;
		}
		CustomMailSite o = (CustomMailSite) obj;
		return new EqualsBuilder().append(this.id, o.id).isEquals();
	}

}
