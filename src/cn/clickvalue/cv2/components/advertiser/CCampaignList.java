package cn.clickvalue.cv2.components.advertiser;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class CCampaignList {
private Campaign campaign;
    
    /**
     * 用户选择的操作
     */
    private String operate;
    
    @Inject
    @Service("campaignService")
    private CampaignService campaignService;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @SuppressWarnings("unused")
    @Property
    private int noOfRowsPerPage = 15;

    @Persist
    private GridDataSource dataSource;
    
    private BeanModel<Campaign> beanModel;
    
    public GridDataSource getDataSource() throws Exception {
        if (dataSource == null) {
            dataSource = new HibernateDataSource(campaignService);
        }
        return dataSource;
    }
    
    public BeanModel<Campaign> getBeanModel() {
        beanModel = beanModelSource.create(Campaign.class, true, componentResources);
        beanModel.add("commissionRule",null).label("佣金").sortable(false);
        beanModel.add("bannersCount",null).label("广告数").sortable(false);
        beanModel.add("site.name").label("网站名称").sortable(false);
        beanModel.add("operate",null).label("操作").sortable(false);
        beanModel.get("cookieMaxage").label("cookie期限(天)");
        beanModel.get("name").label("广告活动名称");
        beanModel.get("region").label("广告投放区域");
        beanModel.get("startDate").label("起始日期");
        beanModel.get("endDate").label("结束日期");
        beanModel.include("name","cookieMaxage","commissionRule","site.name","bannersCount","region","verified","startDate","endDate","operate");
        beanModel.reorder("name","cookieMaxage","commissionRule","site.name","bannersCount","region","verified","startDate","endDate","operate");
        return beanModel;
    }
    
    /**
     * @return 佣金
     */
    public String getCommissionRule() {
        List<CommissionRule> commissionRules = campaign.getCommissionRules();
        StringBuffer commissionRule = new StringBuffer("");
        for(CommissionRule obj : commissionRules) {
            String formatCommissionRule = Constants.formatCommissionRule(obj);
            commissionRule.append(formatCommissionRule);
            commissionRule.append("\n");
        }
        return commissionRule.toString();
    }
    
    /**
     * @return 字符串的optionModel
     * 直接在页面上写的时候的格式为：literal:value=label,value=label,......
     */
    public String getOperateModel(){
        StringBuffer str = new StringBuffer("");
        str.append("a=查看,");
        str.append("b=修改,");
        str.append("c=删除");
        return str.toString();
    }

    public Campaign getCampaign() {
        return campaign;
    }
 
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public CampaignService getCampaignService() {
        return campaignService;
    }

    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
    
}
