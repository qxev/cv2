package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class PimReportSummary extends PersistentObject {

	private static final long serialVersionUID = -225186545633004358L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User affiliate;

	private Long points;

	public User getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(User affiliate) {
		this.affiliate = affiliate;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
}
