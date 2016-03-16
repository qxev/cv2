package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.base.PersistentObject;

public class AffiliateSummary implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer pendingApprovalWebSites;
    private Integer approvedWebsites;
    private Integer declinedWebsites;
    private Integer dailyActivedWebsites;
    private Integer activedWebsitesForOneMonth;
    private Integer activedWebsitesForThreeMonth;
    private Integer activedWebsitesForHalfAYear;
    private Integer affiliateClicks;
    private Integer beClickedAds;
    private Integer beClickedCampaigns;
    private Integer beAppliedCampaigns;
    private Integer gotCodes;
    private Integer newCampaigns;
    private Integer newAds;
    private Integer validAds;
    private Integer pageView;
    private Date summaryDate;
    private int week;
    private int year;
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public AffiliateSummary() {
        
    }

    public Integer getPendingApprovalWebSites() {
        return pendingApprovalWebSites;
    }

    public void setPendingApprovalWebSites(Integer pendingApprovalWebSites) {
        this.pendingApprovalWebSites = pendingApprovalWebSites;
    }

    public Integer getApprovedWebsites() {
        return approvedWebsites;
    }

    public void setApprovedWebsites(Integer approvedWebsites) {
        this.approvedWebsites = approvedWebsites;
    }

    public Integer getDeclinedWebsites() {
        return declinedWebsites;
    }

    public void setDeclinedWebsites(Integer declinedWebsites) {
        this.declinedWebsites = declinedWebsites;
    }

    public Integer getDailyActivedWebsites() {
        return dailyActivedWebsites;
    }

    public void setDailyActivedWebsites(Integer dailyActivedWebsites) {
        this.dailyActivedWebsites = dailyActivedWebsites;
    }

    public Integer getActivedWebsitesForOneMonth() {
        return activedWebsitesForOneMonth;
    }

    public void setActivedWebsitesForOneMonth(Integer activedWebsitesForOneMonth) {
        this.activedWebsitesForOneMonth = activedWebsitesForOneMonth;
    }

    public Integer getActivedWebsitesForThreeMonth() {
        return activedWebsitesForThreeMonth;
    }

    public void setActivedWebsitesForThreeMonth(Integer activedWebsitesForThreeMonth) {
        this.activedWebsitesForThreeMonth = activedWebsitesForThreeMonth;
    }

    public Integer getActivedWebsitesForHalfAYear() {
        return activedWebsitesForHalfAYear;
    }

    public void setActivedWebsitesForHalfAYear(Integer activedWebsitesForHalfAYear) {
        this.activedWebsitesForHalfAYear = activedWebsitesForHalfAYear;
    }

    public Integer getAffiliateClicks() {
        return affiliateClicks;
    }

    public void setAffiliateClicks(Integer affiliateClicks) {
        this.affiliateClicks = affiliateClicks;
    }

    public Integer getBeClickedAds() {
        return beClickedAds;
    }

    public void setBeClickedAds(Integer beClickedAds) {
        this.beClickedAds = beClickedAds;
    }

    public Integer getBeClickedCampaigns() {
        return beClickedCampaigns;
    }

    public void setBeClickedCampaigns(Integer beClickedCampaigns) {
        this.beClickedCampaigns = beClickedCampaigns;
    }

    public Integer getBeAppliedCampaigns() {
        return beAppliedCampaigns;
    }

    public void setBeAppliedCampaigns(Integer beAppliedCampaigns) {
        this.beAppliedCampaigns = beAppliedCampaigns;
    }

    public Integer getGotCodes() {
        return gotCodes;
    }

    public void setGotCodes(Integer gotCodes) {
        this.gotCodes = gotCodes;
    }

    public Integer getNewCampaigns() {
        return newCampaigns;
    }

    public void setNewCampaigns(Integer newCampaigns) {
        this.newCampaigns = newCampaigns;
    }

    public Integer getNewAds() {
        return newAds;
    }

    public void setNewAds(Integer newAds) {
        this.newAds = newAds;
    }

    public Integer getValidAds() {
        return validAds;
    }

    public void setValidAds(Integer validAds) {
        this.validAds = validAds;
    }

    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }

    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

	public String getSummaryDateDisplay() {
		if (summaryDate!=null)
			return DateUtil.dateToString(summaryDate);
		else 
			return "";
	}
	
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getWeekDisplay() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(year).append("年  第").append(week).append("周");
    	return sb.toString();
    }
    
}
