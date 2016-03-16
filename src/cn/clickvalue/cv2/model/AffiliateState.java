package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AffiliateState extends PersistentObject {

	private static final long serialVersionUID = 3902406644034853040L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User affiliate;

	private Integer activeMonth;

	private Integer unActiveMonth;

	private Integer maxActiveMonth;

	private Integer maxUnActiveMonth;

	private Date lastActiveScanDate;

	public User getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(User affiliate) {
		this.affiliate = affiliate;
	}

	public Integer getActiveMonth() {
		return activeMonth;
	}

	public void setActiveMonth(Integer activeMonth) {
		this.activeMonth = activeMonth;
	}

	public Integer getUnActiveMonth() {
		return unActiveMonth;
	}

	public void setUnActiveMonth(Integer unActiveMonth) {
		this.unActiveMonth = unActiveMonth;
	}

	public Integer getMaxActiveMonth() {
		return maxActiveMonth;
	}

	public void setMaxActiveMonth(Integer maxActiveMonth) {
		this.maxActiveMonth = maxActiveMonth;
	}

	public Integer getMaxUnActiveMonth() {
		return maxUnActiveMonth;
	}

	public void setMaxUnActiveMonth(Integer maxUnActiveMonth) {
		this.maxUnActiveMonth = maxUnActiveMonth;
	}

	public Date getLastActiveScanDate() {
		return lastActiveScanDate;
	}

	public void setLastActiveScanDate(Date lastActiveScanDate) {
		this.lastActiveScanDate = lastActiveScanDate;
	}
}
