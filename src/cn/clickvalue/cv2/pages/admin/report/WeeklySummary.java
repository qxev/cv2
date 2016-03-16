package cn.clickvalue.cv2.pages.admin.report;

import java.util.Date;

import org.apache.tapestry5.annotations.PageLoaded;

import cn.clickvalue.cv2.common.util.DateUtil;

public class WeeklySummary {

    private Date dateBegin;
    private Date dateEnd;
    
    @PageLoaded
    void pageLoaded() {
        dateBegin = DateUtil.getFirstDayOfThisMonth();
        dateEnd = new Date();
    }
    
    public Date getDateBegin() {
        return dateBegin;
    }
    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }
    public Date getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    
}
