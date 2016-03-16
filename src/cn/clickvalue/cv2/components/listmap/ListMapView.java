package cn.clickvalue.cv2.components.listmap;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;

import cn.clickvalue.cv2.report.api.Report;
import cn.clickvalue.cv2.report.api.Reader;
import cn.clickvalue.cv2.report.api.Field;
//import org.apache.tapestry5.annotations.Parameter;
public class ListMapView {
    @Persist
    private HashMap<String,Object> cmap;
    @Persist
    private ArrayList<HashMap<String,Object>> clist;
    @Persist
    private Report report;
    @Persist
    private ArrayList<Integer> pageIds;
    @Persist
    private int pageId = 1;

    @Persist
    private int currentPageId = 1;
    
    @Persist
    private ArrayList<Field> fds;
    @Persist
    private Field fd;
    @Persist
    private String orderField;
    @Persist
    private int orderType = Report.ORDER_ASC;
    @OnEvent(component = "pagedGuide")
    void clickPagedGuide(int idx) {
        currentPageId = idx;
        showPagedist(idx);
    }
    
    @OnEvent(component = "orderGuide")
    void clickPagedGuide(String cfield) {
        pageId = 1;
        currentPageId = 1;
        if(pageIds ==null) {}else pageIds.clear();
        showOrderField(cfield);
    }
    
    public void showOrderField(String ofd) {
        orderField = ofd;
        orderType = (orderType == Report.ORDER_DESC)?Report.ORDER_ASC:Report.ORDER_DESC;
        readData();
    }

    public void showPagedist(int id) {
        pageId = id;
        readData();
    }
    /**
     * 读取报表数据
     */
    public void readData() {
        if(pageIds==null) pageIds = new ArrayList<Integer>();
        int plen = pageIds.size();
        if(plen < pageId) {
            pageIds.add(pageId);
        }
        report.setPageId(currentPageId);
        if(orderField==null){}else {
            report.setOrderField(orderField,orderType);
            orderField = report.getOrderField();
            orderType = report.getOrderType();
        }
        clist = Reader.execute(report);
        int rlen = clist.size();
        if(rlen == report.getPageRecords() && plen < pageId) {
            int nextId = pageId+1;
            pageIds.add(nextId);
        }
        fds = report.getHeaderFields();
    }

    public int getCurrentPageId() {
        return currentPageId;
    }

    public void setCurrentPageId(int currentPageId) {
        this.currentPageId = currentPageId;
    }

    public boolean isCurrent(int index) {
        boolean re = (currentPageId == index);
        return re;
    }

    public ArrayList<Field> getFds() {
        return fds;
    }

    public void setFds(ArrayList<Field> fds) {
        this.fds = fds;
    }

    public Field getFd() {
        return fd;
    }

    public void setFd(Field fd) {
        this.fd = fd;
    }

    public HashMap<String, Object> getCmap() {
        return cmap;
    }

    public void setCmap(HashMap<String, Object> cmap) {
        this.cmap = cmap;
    }

    public ArrayList<HashMap<String, Object>> getClist() {
        return clist;
    }

    public void setClist(ArrayList<HashMap<String, Object>> clist) {
        this.clist = clist;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ArrayList<Integer> getPageIds() {
        return pageIds;
    }

    public void setPageIds(ArrayList<Integer> pageIds) {
        this.pageIds = pageIds;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }
}
