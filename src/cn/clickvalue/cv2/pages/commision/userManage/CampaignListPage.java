package cn.clickvalue.cv2.pages.commision.userManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;
import cn.clickvalue.cv2.web.ClientSession;

public class CampaignListPage extends BasePage {
	
	@Persist
	@Property
	private Integer advertiseId;

	@Persist
	@Property
	private Integer userId;
	
	@Persist
	@Property
	private String uid;


    private Campaign campaign;
    
    @Persist
    private String formCampaignName;
    
    @Persist
    private String formAdvertiserName;
    
    @Persist
    private LabelValueModel formVerified;

    @Inject
    private CampaignService campaignService;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Persist
    private GridDataSource dataSource;

    private BeanModel<Campaign> beanModel;
    
    @InjectPage
    private MessagePage messagePage;
    
    @Inject
    private UserService userService;
    
    @Inject
    private Cookies cookies;
    
    @Inject
    private Request request;
    
    @ApplicationState
    private ClientSession clientSession;

    @Property
    private List<String> advertiserNames = new ArrayList<String>();
    
    @Property
    private List<String> campaignNames = new ArrayList<String>();

    void onActivate() {
       	this.userId = clientSession.getId();
    }
    
    /**
     * @param event 事件标示
     * @param id 广告活动ID
     */
    Object onActivate(String event,int id) {
    	messagePage.setNextPage("admin/campaign/listPage");
    	return messagePage;
    }
    
    void cleanupRender() {
    	formCampaignName = null;
	}
    
    void onPrepare() {
	    this.uid = cookies.readCookieValue("uid");
        initForm();
        initQuery();
    }
    
    private void initForm() {
    	advertiserNames = userService.findAllAdvertiserName("select u.name from User u left join u.campaigns c where c.deleted = 0 and c.verified=2 group by u order by u.name");
    	campaignNames = campaignService.findAllCampaignNameByHql(" select campaign.name from Campaign campaign inner join campaign.user u where campaign.verified=2 and campaign.deleted=0 order by campaign.name ");
    }
    
    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("user", "user", Criteria.INNER_JOIN);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verified", 2);
        map.put("deleted", 0);
        
        if(StringUtils.isNotBlank(formCampaignName)) {
            map.put("name", formCampaignName.trim());
        }
        
        if(StringUtils.isNotBlank(formAdvertiserName)) {
        	map.put("user.name", formAdvertiserName.trim());
        }
        
        c.setCondition(map);
        c.addOrder(Order.desc("createdAt"));
        this.dataSource = new HibernateDataSource(campaignService,c);
    }

    public GridDataSource getDataSource() throws Exception {
        return dataSource;
    }

    public BeanModel<Campaign> getBeanModel() {
        beanModel = beanModelSource.create(Campaign.class, true,componentResources);
        beanModel.add("user.name").label("广告主").sortable(false);
        beanModel.add("site.name").label("网站名称").sortable(false);
        beanModel.get("cpa").label("佣金").sortable(false);
        beanModel.get("name").label("广告活动名称").sortable(false);
        beanModel.get("startDate").label("起始日期");
        beanModel.get("endDate").label("结束日期");
        beanModel.add("operate", null).label("操作").sortable(false);
        beanModel.include("user.name", "name", "cpa","site.name","startDate","endDate", "operate");
        return beanModel;
    }

    /**
     * @return 佣金
     */
    public String getCommissionRule() {
        List<CommissionRule> commissionRules = campaign.getCommissionRules();
        StringBuffer commissionRule = new StringBuffer("");
        for (CommissionRule obj : commissionRules) {
            String formatCommissionRule = Constants.formatCommissionRule(obj);
            commissionRule.append(formatCommissionRule);
            commissionRule.append("\n");
        }
        return commissionRule.toString();
    }
    
    public String getVerified() {
        Integer verified = campaign.getVerified();
        String str = Constants.formatVerified(getMessages(), verified);
        return str;
    }
    
    public String getAffiliateVerified() {
        Integer affiliateVerified = campaign.getAffiliateVerified();
        String str = Constants.formatAffiliateVerified(affiliateVerified);
        return str;
    }

    /**
     * @return 字符串的optionModel
     *         直接在页面上写的时候的格式为：literal:value=label,value=label,......
     */
    public List<String> getViewParameters(){
        List<String> list = new ArrayList<String>();
        list.add(campaign.getId().toString());
        list.add("admin/campaign/listPage");
        return list;
    }
    
    public String getOnlineStatus() {
        String str = Constants.formatPublishStatus(getMessages(), campaign);
        return str;
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

    public String getFormCampaignName() {
        return formCampaignName;
    }

    public void setFormCampaignName(String formCampaignName) {
        this.formCampaignName = formCampaignName;
    }

    public LabelValueModel getFormVerified() {
        return formVerified;
    }

    public void setFormVerified(LabelValueModel formVerified) {
        this.formVerified = formVerified;
    }

    public String getFormAdvertiserName() {
        return formAdvertiserName;
    }


    public void setFormAdvertiserName(String formAdvertiserName) {
        this.formAdvertiserName = formAdvertiserName;
    }
}