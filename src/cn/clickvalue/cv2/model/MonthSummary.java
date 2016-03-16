package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class MonthSummary extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer advertiseAffiliateId;

    private String summaryMonth;

    private Integer confirmedOmmission;

    private Integer paid;

    private Date paidDate;

    public MonthSummary() {
    }

    public Integer getAdvertiseAffiliateId() {
        return this.advertiseAffiliateId;
    }

    public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
        this.advertiseAffiliateId = advertiseAffiliateId;
    }

    public String getSummaryMonth() {
        return this.summaryMonth;
    }

    public void setSummaryMonth(String summaryMonth) {
        this.summaryMonth = summaryMonth;
    }

    public Integer getConfirmedOmmission() {
        return this.confirmedOmmission;
    }

    public void setConfirmedOmmission(Integer confirmedOmmission) {
        this.confirmedOmmission = confirmedOmmission;
    }

    public Integer getPaid() {
        return this.paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Date getPaidDate() {
        return this.paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }
}