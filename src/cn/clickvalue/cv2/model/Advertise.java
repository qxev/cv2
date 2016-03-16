package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class Advertise extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "advertise")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AdvertiseAffiliate> advertiseAffiliates;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bannerId")
	private Banner banner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "landingPageId")
	private LandingPage landingPage;

	private Integer campaignId;

	private Integer deleted;

	public Advertise() {
	}

	public List<AdvertiseAffiliate> getAdvertiseAffiliates() {
		return advertiseAffiliates;
	}

	public Banner getBanner() {
		return banner;
	}

	public LandingPage getLandingPage() {
		return landingPage;
	}

	public void setAdvertiseAffiliates(
			List<AdvertiseAffiliate> advertiseAffiliates) {
		this.advertiseAffiliates = advertiseAffiliates;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public void setLandingPage(LandingPage landingPage) {
		this.landingPage = landingPage;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}