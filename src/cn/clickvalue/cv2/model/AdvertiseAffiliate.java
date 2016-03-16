package cn.clickvalue.cv2.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "advertiseaffiliate")
public class AdvertiseAffiliate extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer actived;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advertiseId")
	private Advertise advertise;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siteId")
	private Site site;

	private Integer status;

	private String trackCode;

	public AdvertiseAffiliate() {
	}

	public Integer getActived() {
		return actived;
	}

	public Advertise getAdvertise() {
		return advertise;
	}

	public Site getSite() {
		return site;
	}

	public Integer getStatus() {
		return status;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public void setAdvertise(Advertise advertise) {
		this.advertise = advertise;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTrackCode() {
		return trackCode;
	}

	public void setTrackCode(String trackCode) {
		this.trackCode = trackCode;
	}
}