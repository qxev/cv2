package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class Summary extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer advertiseAffiliateId;

    private Date summaryDate;

    private Integer clicks;

    private Integer leads;

    private Integer validLeads;

    private Integer commission;

    private Integer confirmedCommission;

    private Integer checked;

    private Date checkedDate;

    private Integer paid;

    private Date paidDate;

    /** default constructor */
    public Summary() {
    }

    /** full constructor */
    // Property accessors
    public Integer getAdvertiseAffiliateId() {
        return this.advertiseAffiliateId;
    }

    public void setAdvertiseAffiliateId(Integer advertiseAffiliateId) {
        this.advertiseAffiliateId = advertiseAffiliateId;
    }

    public Date getSummaryDate() {
        return this.summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

    public Integer getClicks() {
        return this.clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getLeads() {
        return this.leads;
    }

    public void setLeads(Integer leads) {
        this.leads = leads;
    }

    public Integer getValidLeads() {
        return this.validLeads;
    }

    public void setValidLeads(Integer validLeads) {
        this.validLeads = validLeads;
    }

    public Integer getCommission() {
        return this.commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public Integer getConfirmedCommission() {
        return this.confirmedCommission;
    }

    public void setConfirmedCommission(Integer confirmedCommission) {
        this.confirmedCommission = confirmedCommission;
    }

    public Integer getChecked() {
        return this.checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Date getCheckedDate() {
        return this.checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
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