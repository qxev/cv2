package cn.clickvalue.cv2.pages.admin.cheating;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Cheating;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CheatingService;

public class CheatingListPage extends BasePage {
	
	@Property
	private Cheating cheating;
	
	@Persist
	@Property
	private String formAffiliateName;
	
	@Persist
	@Property
	private String formSiteUrl;
	
	@Persist
	@Property
	private String formCampaignName;
	
	@Persist
	@Property
	private Date formCheatBeginAt;
	
	@Persist
	@Property
	private Date formCheatEndAt;
	
	private GridDataSource dataSource;

    private BeanModel<Cheating> beanModel;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;
    
    @Inject
    private CheatingService cheatingService;
    
    @InjectPage
    private CheatingNewPage cheatingNewPage;
    
    @InjectPage
    private MessagePage messagePage;
    
    @SetupRender
    void setupRender(){
    	initQuery();
    	initBeanModel();
    }
    
    Object onActivate(int cheatingId,String event){
    	if("delete".equals(event)){
    		deleteCheating(cheatingId);
    	}
    	return messagePage;
    }
    
    private void deleteCheating(int cheatingId) {
    	Cheating deleteCheating = cheatingService.get(cheatingId);
    	deleteCheating.setDeleted(1);
    	cheatingService.save(deleteCheating);
    	messagePage.setMessage("删除成功");
    	messagePage.setNextPage("admin/cheating/listpage");
	}

	//整理表单提交后表单项内容
    void onSuccess(){
    	if(formAffiliateName != null){
    		formAffiliateName = formAffiliateName.trim();
    	}
    	if(formSiteUrl != null){
    		formSiteUrl = formSiteUrl.trim();
    	}
    	if(formCampaignName != null){
    		formCampaignName = formCampaignName.trim();
    	}
    }

	private void initBeanModel() {
		beanModel = beanModelSource.create(Cheating.class, true,componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("siteUrl").label("网站").sortable(false);
		beanModel.get("campaignName").label("广告活动").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.get("cheatBeginAt").label("开始时间").sortable(true);
		beanModel.get("cheatEndAt").label("结束时间").sortable(true);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("affiliate.name","siteUrl","campaignName","cheatBeginAt","cheatEndAt","description","operate");
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);
		if(StringUtils.isNotBlank(formAffiliateName)){
			c.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(formSiteUrl)){
			c.addCriterion(Restrictions.like("siteUrl", formSiteUrl, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(formCampaignName)){
			c.addCriterion(Restrictions.like("campaignName", formCampaignName, MatchMode.ANYWHERE));
		}
		if(formCheatBeginAt != null){
			c.addCriterion(Restrictions.le("cheatEndAt", formCheatBeginAt));
		}
		if(formCheatEndAt != null){
			c.addCriterion(Restrictions.ge("cheatBeginAt", formCheatEndAt));
		}
		c.addCriterion(Restrictions.eq("deleted", 0));
		c.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(cheatingService,c);
	}
	
	Object onActionFromCreate(){
		cheatingNewPage.initSearchForm();
		return cheatingNewPage;
	}

	public BeanModel<Cheating> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

}
