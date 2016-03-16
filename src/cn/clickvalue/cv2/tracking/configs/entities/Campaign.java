package cn.clickvalue.cv2.tracking.configs.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Campaign implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer userId;

	private String name;

	private String parameters;

	private Integer siteId;

	private Integer actived;

	private Integer deleted;

	private Integer verified;

	private Float confirmationRate;

	private Integer cookieMaxage;

	private Integer affiliateCategoryId;

	private Integer affiliateVerified;

	private Integer enforced;

	private String region;

	private String cpa;

	private String description;

	private Date startDate;

	private Date endDate;

	private Date createdAt;

	private Date updatedAt;

	private Integer htmlCode;

	private Integer rank;

	private Integer bbsId;

	public List<Advertise> advertises = new ArrayList<Advertise>();

	public List<Integer> refusedSiteIds = new ArrayList<Integer>();

	private String categories;

	private Integer intrust;

	private Integer semId;

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getBbsId() {
		return bbsId;
	}

	public void setBbsId(Integer bbsId) {
		this.bbsId = bbsId;
	}

	public Campaign() {}

	public Integer getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(Integer htmlCode) {
		this.htmlCode = htmlCode;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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

	public Float getConfirmationRate() {
		return this.confirmationRate;
	}

	public void setConfirmationRate(Float confirmationRate) {
		this.confirmationRate = confirmationRate;
	}

	public Integer getCookieMaxage() {
		return this.cookieMaxage;
	}

	public void setCookieMaxage(Integer cookieMaxage) {
		this.cookieMaxage = cookieMaxage;
	}

	public Integer getAffiliateCategoryId() {
		return this.affiliateCategoryId;
	}

	public void setAffiliateCategoryId(Integer affiliateCategoryId) {
		this.affiliateCategoryId = affiliateCategoryId;
	}

	public Integer getAffiliateVerified() {
		return this.affiliateVerified;
	}

	public void setAffiliateVerified(Integer affiliateVerified) {
		this.affiliateVerified = affiliateVerified;
	}

	public Integer getEnforced() {
		return this.enforced;
	}

	public void setEnforced(Integer enforced) {
		this.enforced = enforced;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCpa() {
		return this.cpa;
	}

	public void setCpa(String cpa) {
		this.cpa = cpa;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public List<Advertise> getAdvertises() {
		return advertises;
	}

	public void setAdvertises(List<Advertise> advertises) {
		this.advertises = advertises;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public Integer getIntrust() {
		return intrust;
	}

	public void setIntrust(Integer intrust) {
		this.intrust = intrust;
	}

	public Integer getSemId() {
		return semId;
	}

	public void setSemId(Integer semId) {
		this.semId = semId;
	}

	public List<Integer> getRefusedSiteIds() {
		return refusedSiteIds;
	}

	public void setRefusedSiteIds(List<Integer> refusedSiteIds) {
		this.refusedSiteIds = refusedSiteIds;
	}
}