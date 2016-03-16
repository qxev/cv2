package cn.clickvalue.cv2.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AffiliateCampaign extends PersistentObject {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User user;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	private Integer showed;

	private Integer concerned;

	public AffiliateCampaign() {
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

	public Integer getShowed() {
		return showed;
	}

	public void setShowed(Integer showed) {
		this.showed = showed;
	}

	public Integer getConcerned() {
		return concerned;
	}

	public void setConcerned(Integer concerned) {
		this.concerned = concerned;
	}
}