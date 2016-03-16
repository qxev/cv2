package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "banner")
@BatchSize(size = 10)
public class Banner extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer actived;

	private String bannerType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	private Integer deleted;

	private String content;

	private Integer height;

	@OneToMany(mappedBy = "banner")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Advertise> advertises;

	private Integer verified;

	private Integer width;

	public Banner() {
	}

	public List<Advertise> getAdvertises() {
		return advertises;
	}

	public void setAdvertises(List<Advertise> advertises) {
		this.advertises = advertises;
	}

	public Integer getActived() {
		return this.actived;
	}

	public String getBannerType() {
		return this.bannerType;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public Integer getDeleted() {
		return this.deleted;
	}

	public Integer getHeight() {
		return this.height;
	}

	public Integer getVerified() {
		return this.verified;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}