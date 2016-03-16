package cn.clickvalue.cv2.pages.admin.userManage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignHistoryService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.UserService;

public class CampaignHistoryListPage extends BasePage {

	@Persist
	@Property
	private String formAdvertiserName;

	@Persist
	@Property
	private String formCampaignName;

	@Property
	private List<String> advertiserNames = new ArrayList<String>();

	@Property
	private List<String> campaignNames = new ArrayList<String>();

	@Inject
	private UserService userService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CampaignHistoryService campaignHistoryService;

	@Inject
	private CampaignService campaignService;

	@Property
	private GridDataSource dataSource;

	@Property
	private CampaignHistory campaignHistory;

	@Property
	private BeanModel<CampaignHistory> beanModel;
	
	private Object result;

	void onPrepare() {
		initForm();
	}

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CampaignHistory.class, true,
				componentResources);
		beanModel.add("campaign.user.name").label("广告主").sortable(false);
		beanModel.add("campaign.name").label("广告活动").sortable(false);
		beanModel.get("startDate").label("开始时间").sortable(true);
		beanModel.get("endDate").label("结束时间").sortable(true);
		beanModel.get("affiliateCommission").label("网站主佣金").sortable(true);
		beanModel.get("darwinProfit").label("达闻佣金").sortable(true);
		beanModel.get("confirmedAffiliateCommission").label("总佣金").sortable(true);
		beanModel.get("confirmDate").label("支付时间").sortable(true);

		beanModel.include("campaign.user.name", "campaign.name", "startDate",
				"endDate", "affiliateCommission", "darwinProfit",
				"confirmedAffiliateCommission", "confirmDate");

	}

	private void initQuery() {
		CritQueryObject c = setCrit();
		dataSource = new HibernateDataSource(campaignHistoryService, c);
	}

	private CritQueryObject setCrit() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		c.addJoin("campaign.user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(formCampaignName)) {
			c.addCriterion(Restrictions.eq("campaign.name", formCampaignName
					.trim()));
		}
		if (StringUtils.isNotBlank(formAdvertiserName)) {
			c.addCriterion(Restrictions.like("user.name",
					formAdvertiserName));
		}

		c.addCriterion(Restrictions.isNotNull("confirmDate"));
		c.setCondition(map);
		return c;
	}

	private void initForm() {
		advertiserNames = userService.findAllAdvertiserName();
		campaignNames = campaignService
				.findAllCampaignNameByHql(" select campaign.name from Campaign campaign where campaign.verified>0 and campaign.deleted=0 order by campaign.name ");
	}
	
	@OnEvent(component="export", value="selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		String realPath = RealPath.getRoot();
		CritQueryObject c = setCrit();
		List <CampaignHistory>datas = campaignHistoryService.find(c);
		String outputName = "CampaignHistoryListPage" + System.currentTimeMillis();
		ExcelUtils.mergerXLS(datas, "CampaignHistoryListPage", outputName);
		FileInputStream fileInputStream = new FileInputStream(realPath+"excel/"+outputName+".xls");
		result = new XLSAttachment(fileInputStream, "report");
	}
	
	Object onSubmit(){
		return result;
	}
}