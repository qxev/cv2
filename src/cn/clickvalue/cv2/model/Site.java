package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Table(name = "site")
public class Site extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "site")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AdvertiseAffiliate> advertiseAffiliates;

	@OneToMany(mappedBy = "site")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AdvertiserCategorySite> advertiserCategorySites;

	@OneToMany(mappedBy = "site")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AffiliateCategorySite> affiliateCategorySites;

	@OneToMany(mappedBy = "site")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Campaign> campaigns;

	@OneToMany(mappedBy = "site")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CampaignSite> campaignSites;

	@Column(name="is_active")
	private Integer isActived;
	
	private Integer deleted;

	private String urlName;

	private String description;

	private String logo;

	private String name;

	private Integer pvPerDay;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	private Integer verified;

	public Site() {
	}
	
	public Site(Integer id, String name) {
	    this.setId(id);
	    this.name = name;
	}
	
	public Site(Integer id, String name, User user) {
	    this.setId(id);
	    this.name = name;
	    this.user = user;
	}
	
	public Site(String name, String urlName, String description, Integer pvPerDay, String url) {
	    this.url = url;
	    this.urlName = urlName;
	    this.name = name;
	    this.description = description;
	    this.pvPerDay = pvPerDay;
	}

	public List<AdvertiseAffiliate> getAdvertiseAffiliates() {
		return advertiseAffiliates;
	}

	public List<AdvertiserCategorySite> getAdvertiserCategorySites() {
		return advertiserCategorySites;
	}

	public List<AffiliateCategorySite> getAffiliateCategorySites() {
		return affiliateCategorySites;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public List<CampaignSite> getCampaignSites() {
		return campaignSites;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public String getDescription() {
		return description;
	}

	public String getLogo() {
		return logo;
	}

	public String getName() {
		return name;
	}

	public Integer getPvPerDay() {
		return pvPerDay;
	}

	public String getUrl() {
		return url;
	}

	public User getUser() {
		return user;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setAdvertiseAffiliates(
			List<AdvertiseAffiliate> advertiseAffiliates) {
		this.advertiseAffiliates = advertiseAffiliates;
	}

	public void setAdvertiserCategorySites(
			List<AdvertiserCategorySite> advertiserCategorySites) {
		this.advertiserCategorySites = advertiserCategorySites;
	}

	public void setAffiliateCategorySites(
			List<AffiliateCategorySite> affiliateCategorySites) {
		this.affiliateCategorySites = affiliateCategorySites;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public void setCampaignSites(List<CampaignSite> campaignSites) {
		this.campaignSites = campaignSites;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPvPerDay(Integer pvPerDay) {
		this.pvPerDay = pvPerDay;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

    public Integer getIsActived() {
        return isActived;
    }

    public void setIsActived(Integer isActived) {
        this.isActived = isActived;
    }
}