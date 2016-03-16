package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.StringUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.EffectReport;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.EffectReportService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class EffectReportPage extends BasePage {
    @Property
    private int noOfRowsPerPage = 15;

    @ApplicationState
    private User user;

    @Inject
    private Messages messages;

    private String cplSum;
    private String cpcSum;
    private String cpsSum;
    private String cpmSum;

    private String confirmedCpcSum;
    private String confirmedCplSum;
    private String confirmedCpsSum;
    private String confirmedCpmSum;

    private String cplMoney;
    private String cpcMoney;
    private String cpsMoney;
    private String cpmMoney;

    private String confirmedCpcMoney;
    private String confirmedCplMoney;
    private String confirmedCpsMoney;
    private String confirmedCpmMoney;
    
    @Inject
    @Service("effectReportService")
    private EffectReportService effectReportService;
    
    @Inject
    @Service("campaignService")
    private CampaignService campaignService;

    @Property
    @Persist("flash")
    private Date date;

    @Property
    @Persist("flash")
    private Date date1;

    @Property
    @Persist
    private EffectReport effectReport;

    @Inject
    private BeanModelSource beanModelSource;

    @Persist
    @Property
    private GridDataSource dataSource;

    @Inject
    @Service("siteService")
    private SiteService siteService;

    private BeanModel<EffectReport> beanModel;

    @Inject
    private ComponentResources componentResources;

    @InjectSelectionModel(labelField = "name", idField = "id")
    @Persist
    private List<Site> sites = new ArrayList<Site>();
    
    @InjectSelectionModel(labelField = "name", idField = "id")
    @Persist
    private List<Campaign> campaigns = new ArrayList<Campaign>();

    @Persist("flash")
    private Site site;
    
    @Persist("flash")
    private Campaign campaign;

    /**
     * 
     */
    void onPrepare() {
        Calendar instance = Calendar.getInstance();
        if (this.date == null) {
            instance.add(Calendar.MONTH, -1);
            this.date = instance.getTime();
        }

        if (this.date1 == null) {
            instance.add(Calendar.MONTH, 1);
            instance.add(Calendar.DATE, 1);
            this.date1 = instance.getTime();
        }
    }

    /**
     * Add a custom column to hold the row no to the table.
     */
    public BeanModel<EffectReport> getBeanModel() {
        this.beanModel = beanModelSource.create(EffectReport.class, true,
                componentResources);
        beanModel.get("createTime").label("时间");
        beanModel.get("cpc").label("点击(CPC)数").sortable(false);
        beanModel.get("cpcMoney").label("点击(CPC)佣金").sortable(false);
        beanModel.get("cpl").label("引导(CPL)数").sortable(false);
        beanModel.get("cplMoney").label("引导(CPL)佣金").sortable(false);
        beanModel.get("cps").label("销售(CPS)数").sortable(false);
        beanModel.get("cpsMoney").label("销售(CPS)佣金").sortable(false);
        beanModel.get("cpm").label("销售(CPM)数").sortable(false);
        beanModel.get("cpmMoney").label("销售(CPM)佣金").sortable(false);
        beanModel.get("moneycount").label("金额合计").sortable(false);
        beanModel.get("confirmRate").label("确认率(取最小)").sortable(false);
        beanModel.include("createTime", "cpc", "cpcMoney", "cpl", "cplMoney",
                "cps", "cpsMoney", "cpm", "cpmMoney", "moneycount",
                "confirmRate");
        return beanModel;
    }

    Object onSuccess() {
        return this;
    }

    /**
     * 页面激活
     * 
     * @param id
     */
    void onActivate(Object... objects) {
        int userId = user.getId();
        this.sites = siteService.getSiteByUserId(userId);
        List<Campaign> campaignList = campaignService.getCampaignByUserId(userId);
        this.setCampaigns(campaignList);
        
        if (site == null) {
            site = new Site();
        }
        if (campaign == null) {
            campaign = new Campaign();
        }

        Map<String, Object> map = CollectionFactory.newMap();
        CritQueryObject query = new CritQueryObject(map);
        CritQueryObject query1 = new CritQueryObject(map);

        if (date != null && date1 != null) {
            query.addCriterion(Restrictions.between("createTime", date, date1));
            query1.addCriterion(Restrictions.between("createTime", date, date1));
        }

        // 站点过滤
        if (site.getId() != null) {
            map.put("advertiserId", site.getId().toString());
        }
        
        if(campaign.getId() != null) {
            map.put("campaingId",campaign.getId().toString());
        }
        
        if(getUser().getUserGroup().getName().equals(Constants.USER_GROUP_ADVERTISER)){
            map.put("advertiserId", getUser().getId().toString());
        } else if(getUser().getUserGroup().getName().equals(Constants.USER_GROUP_AFFILIATE)){
            map.put("affiliateId", getUser().getId().toString());
        }
        
        
        // 
        query1.addProjection(Projections.sum("cpc"));
        query1.addProjection(Projections.sum("confirmedCpc"));

        query1.addProjection(Projections.sum("cpl"));
        query1.addProjection(Projections.sum("confirmedCpl"));

        query1.addProjection(Projections.sum("cps"));
        query1.addProjection(Projections.sum("confirmedCps"));

        query1.addProjection(Projections.sum("cpm"));
        query1.addProjection(Projections.sum("confirmedCpm"));

        query1.addProjection(Projections.sum("cpcMoney"));
        query1.addProjection(Projections.sum("confirmedCpcMoney"));

        query1.addProjection(Projections.sum("cplMoney"));
        query1.addProjection(Projections.sum("confirmedCplMoney"));

        query1.addProjection(Projections.sum("cpsMoney"));
        query1.addProjection(Projections.sum("confirmedCpsMoney"));

        query1.addProjection(Projections.sum("cpmMoney"));
        query1.addProjection(Projections.sum("confirmedCpmMoney"));

        Object[] obj = (Object[]) effectReportService.findObject(query1);

        if (obj != null && obj.length > 0) {
            setCpcSum(StringUtils.empty2Zero(obj[0]));
            setConfirmedCpcSum(StringUtils.empty2Zero(obj[1]));

            setCplSum(StringUtils.empty2Zero(obj[2]));
            setConfirmedCplSum(StringUtils.empty2Zero(obj[3]));

            setCpsSum(StringUtils.empty2Zero(obj[4]));
            setConfirmedCpsSum(StringUtils.empty2Zero(obj[5]));

            setCpmSum(StringUtils.empty2Zero(obj[6]));
            setConfirmedCpmSum(StringUtils.empty2Zero(obj[7]));

            setCpcMoney(StringUtils.empty2Zero(obj[8]));
            setConfirmedCpcMoney(StringUtils.empty2Zero(obj[9]));

            setCplMoney(StringUtils.empty2Zero(obj[8]));
            setConfirmedCplMoney(StringUtils.empty2Zero(obj[9]));

            setCpsMoney(StringUtils.empty2Zero(obj[10]));
            setConfirmedCpsMoney(StringUtils.empty2Zero(obj[11]));

            setCpmMoney(StringUtils.empty2Zero(obj[12]));
            setConfirmedCpmMoney(StringUtils.empty2Zero(obj[13]));
        }

        dataSource = new HibernateDataSource(effectReportService, query);
    }

    /**
     * 总计佣金(全部)
     * @return String
     */
    public String getTotalMoney() {
        double totalMoney = Double.parseDouble(cpcMoney) + Double.parseDouble(cplMoney) + Double.parseDouble(cpsMoney) + Double.parseDouble(cpmMoney);
        return String.valueOf(totalMoney);
    }
    
    /**
     * 总计佣金(已确认的)
     * @return String
     */
    public String getconfirmedTotalMoney() {
        double confirmedTotalMoney = Double.parseDouble(confirmedCpcMoney) + Double.parseDouble(confirmedCplMoney) + Double.parseDouble(confirmedCpsMoney) + Double.parseDouble(confirmedCpmMoney);
        return String.valueOf(confirmedTotalMoney);
    }
    
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNoOfRowsPerPage() {
        return noOfRowsPerPage;
    }

    public void setNoOfRowsPerPage(int noOfRowsPerPage) {
        this.noOfRowsPerPage = noOfRowsPerPage;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public String getCplSum() {
        return cplSum;
    }

    public void setCplSum(String cplSum) {
        this.cplSum = cplSum;
    }

    public String getCpcSum() {
        return cpcSum;
    }

    public void setCpcSum(String cpcSum) {
        this.cpcSum = cpcSum;
    }

    public String getCpsSum() {
        return cpsSum;
    }

    public void setCpsSum(String cpsSum) {
        this.cpsSum = cpsSum;
    }

    public String getCpmSum() {
        return cpmSum;
    }

    public void setCpmSum(String cpmSum) {
        this.cpmSum = cpmSum;
    }

    public String getConfirmedCpcSum() {
        return confirmedCpcSum;
    }

    public void setConfirmedCpcSum(String confirmedCpcSum) {
        this.confirmedCpcSum = confirmedCpcSum;
    }

    public String getConfirmedCplSum() {
        return confirmedCplSum;
    }

    public void setConfirmedCplSum(String confirmedCplSum) {
        this.confirmedCplSum = confirmedCplSum;
    }

    public String getConfirmedCpsSum() {
        return confirmedCpsSum;
    }

    public void setConfirmedCpsSum(String confirmedCpsSum) {
        this.confirmedCpsSum = confirmedCpsSum;
    }

    public String getConfirmedCpmSum() {
        return confirmedCpmSum;
    }

    public void setConfirmedCpmSum(String confirmedCpmSum) {
        this.confirmedCpmSum = confirmedCpmSum;
    }

    public String getCplMoney() {
        return cplMoney;
    }

    public void setCplMoney(String cplMoney) {
        this.cplMoney = cplMoney;
    }

    public String getCpcMoney() {
        return cpcMoney;
    }

    public void setCpcMoney(String cpcMoney) {
        this.cpcMoney = cpcMoney;
    }

    public String getCpsMoney() {
        return cpsMoney;
    }

    public void setCpsMoney(String cpsMoney) {
        this.cpsMoney = cpsMoney;
    }

    public String getCpmMoney() {
        return cpmMoney;
    }

    public void setCpmMoney(String cpmMoney) {
        this.cpmMoney = cpmMoney;
    }

    public String getConfirmedCpcMoney() {
        return confirmedCpcMoney;
    }

    public void setConfirmedCpcMoney(String confirmedCpcMoney) {
        this.confirmedCpcMoney = confirmedCpcMoney;
    }

    public String getConfirmedCplMoney() {
        return confirmedCplMoney;
    }

    public void setConfirmedCplMoney(String confirmedCplMoney) {
        this.confirmedCplMoney = confirmedCplMoney;
    }

    public String getConfirmedCpsMoney() {
        return confirmedCpsMoney;
    }

    public void setConfirmedCpsMoney(String confirmedCpsMoney) {
        this.confirmedCpsMoney = confirmedCpsMoney;
    }

    public String getConfirmedCpmMoney() {
        return confirmedCpmMoney;
    }

    public void setConfirmedCpmMoney(String confirmedCpmMoney) {
        this.confirmedCpmMoney = confirmedCpmMoney;
    }

    public EffectReport getEffectReport() {
        return effectReport;
    }

    public void setEffectReport(EffectReport effectReport) {
        this.effectReport = effectReport;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

}
