package cn.clickvalue.cv2.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "semclient")
@BatchSize(size = 10)
public class SemClient extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advertiserId")
	private User advertiser;

	private Integer clientId;

	private String description;

	public SemClient() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(User advertiser) {
		this.advertiser = advertiser;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

}