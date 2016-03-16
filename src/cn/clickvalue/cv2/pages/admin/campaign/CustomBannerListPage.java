package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CustomBannerLog;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CustomBannerLogService;

public class CustomBannerListPage extends BasePage {


	@Property
	private CustomBannerLog customBannerLog;
	
	private Integer campaignId;
	
	@Property
	private Campaign campaign;

	private GridDataSource dataSource;

	private BeanModel<CustomBannerLog> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CampaignService campaignService;

	@Inject
	private CustomBannerLogService customBannerLogService;

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}
	
	Integer onPassivate() {
		return campaignId;
	}

	void setupRender() {
		campaign = campaignService.get(campaignId);
		initQuery();
		initBeanModel();
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(CustomBannerLog.class, true,
				componentResources);
		beanModel.add("user.id").label("网站主ID").sortable(false);
		beanModel.add("user.name").label("网站主名称").sortable(false);
		beanModel.add("user.email").label("Email").sortable(false);
		beanModel.add("user.userInfo.city").label("联系地址").sortable(false);
		beanModel.add("user.userInfo.phone").label("电话号码").sortable(false);
		beanModel.add("user.userInfo.mobile").label("手机号码").sortable(false);
		beanModel.add("user.userInfo.qq").label("QQ").sortable(false);
		beanModel.add("user.userInfo.msn").label("MSN").sortable(false);
		beanModel.get("type").label("获取自定义广告类型").sortable(false);
		beanModel.get("content").label("图片链接地址/文字内容").sortable(false);
		beanModel.get("landpageUrl").label("目标页面地址").sortable(false);
		beanModel.get("createdAt").label("用户使用时间").sortable(false);
		beanModel.include("user.id", "user.name", "user.email", "user.userInfo.city", 
				"user.userInfo.phone", "user.userInfo.mobile", "user.userInfo.qq", 
				"user.userInfo.msn", "type", "content", "landpageUrl","createdAt");
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (campaignId != null && campaignId != 0) {
			conditions.put("campaign.id", campaignId);
		}
		query.setCondition(conditions);
		this.dataSource = new HibernateDataSource(customBannerLogService,
				query);
	}
	
	public String getTypeDisplay(){
		if("1".equals(customBannerLog.getType())){
			return "图片";
		}else{
			return "文字";
		}
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CustomBannerLog> getBeanModel() {
		return beanModel;
	}
}
