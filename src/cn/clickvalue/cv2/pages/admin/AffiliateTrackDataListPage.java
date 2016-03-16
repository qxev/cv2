package cn.clickvalue.cv2.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateTrackData;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AffiliateTrackDataService;

public class AffiliateTrackDataListPage extends BasePage {
    
    @Persist
    private String trackCode;
    
    @Persist
    private String trackIP;
    
    @Persist
    private LabelValueModel ruleType;
    
    @Inject
    private PropertyAccess adapter;
    
    private String col;
    
    private String attr;
    
    private AffiliateTrackData affiliateTrackData;
    
    private List<AffiliateTrackData> affiliateTrackDatas;
    
    @InjectSelectionModel(labelField="label",idField="value")
    private List<LabelValueModel> ruleTypes = new ArrayList<LabelValueModel>();
    
    @Inject
    private AffiliateTrackDataService affiliateTrackDataService;
    
    public void pageLoaded() {
        ruleTypes = Constants.getCommissions(getMessages());
    }
    
    void onActivate() {
        CritQueryObject c = new CritQueryObject();
        
        Map<String, Object> conditions = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(trackCode)) {
            conditions.put("trackCode", trackCode);
        }
        if (StringUtils.isNotBlank(trackIP)) {
            conditions.put("trackIp", trackIP);
        }
        if(ruleType!=null && StringUtils.isNotBlank(ruleType.getValue())) {
            conditions.put("ruleType", Integer.parseInt(ruleType.getValue()));
        }
        
        c.addOrder(Order.desc("trackTime"));
        c.setMaxResults(50);
        c.setCondition(conditions);
        affiliateTrackDatas = affiliateTrackDataService.find(c);
    }
    
    public String[] getCols(){
        String[] cols = new String[] {"trackCode","orderinfo","trackIp","ruleType","dataTotal","trackStatus","trackStep","trackUser","trackTime"};
        return cols;
    }
    
    public String[] getAttrs() {
        String[] cols = new String[] {"trackCode","orderinfo","trackIp","ruleType","dataTotal","trackStatus","trackStep","trackUser","trackTime"};
        return cols;
    }
    
    public Object getValue() {
        Object obj = adapter.get(affiliateTrackData, attr);
        if("trackStatus".equals(attr)) {
            return "0".equals(obj.toString()) ? "无效" : "有效";
        }
        if("ruleType".equals(attr)) {
            CommissionRule c = new CommissionRule();
            c.setRuleType((Integer)obj);
            return Constants.formatCommissionType(c);
        }
        if("createdAt".equals(attr) || "trackTime".equals(attr)) {
            DateUtil.dateToStringWithTime((Date)obj);
        }
        return obj;
    }

    public List<AffiliateTrackData> getAffiliateTrackDatas() {
        return affiliateTrackDatas;
    }

    public void setAffiliateTrackDatas(List<AffiliateTrackData> affiliateTrackDatas) {
        this.affiliateTrackDatas = affiliateTrackDatas;
    }

    public AffiliateTrackDataService getAffiliateTrackDataService() {
        return affiliateTrackDataService;
    }

    public void setAffiliateTrackDataService(
            AffiliateTrackDataService affiliateTrackDataService) {
        this.affiliateTrackDataService = affiliateTrackDataService;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getTrackIP() {
        return trackIP;
    }

    public void setTrackIP(String trackIP) {
        this.trackIP = trackIP;
    }

    public AffiliateTrackData getAffiliateTrackData() {
        return affiliateTrackData;
    }

    public void setAffiliateTrackData(AffiliateTrackData affiliateTrackData) {
        this.affiliateTrackData = affiliateTrackData;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public List<LabelValueModel> getRuleTypes() {
        return ruleTypes;
    }

    public void setRuleTypes(List<LabelValueModel> ruleTypes) {
        this.ruleTypes = ruleTypes;
    }

    public LabelValueModel getRuleType() {
        return ruleType;
    }

    public void setRuleType(LabelValueModel ruleType) {
        this.ruleType = ruleType;
    }

}