package cn.clickvalue.cv2.pages.admin.commissionRule;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionLadder;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionLadderService;

public class CommissionLadderListPage extends BasePage {

	@Property
	private CommissionLadder commissionLadder;

	private Integer campaignId;
	
	@Property
	private Campaign campaign;

	private GridDataSource dataSource;

	private BeanModel<CommissionLadder> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CommissionLadderService commissionLadderService;
	
	@Inject
	private CampaignService campaignService;
	
	@InjectPage
	private MessagePage messagePage;

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}
	
	Object onActivate(Integer campaignId,Integer commissionId,String event){
		if(event.equals("delete")){
			return delete(commissionId);
		}
		return this;
	}

	private Object delete(Integer commissionId) {
		CommissionLadder commissionLadder = commissionLadderService.get(commissionId);
		Campaign campaign = commissionLadder.getCampaign();
		commissionLadder.setDeleted(1);
		commissionLadderService.save(commissionLadder);
		StringBuffer sbf = new StringBuffer("admin/commissionrule/commissionLadderlistpage/");
		sbf.append(campaign.getId());
		String str = sbf.toString();
		messagePage.setMessage("删除成功！");
		messagePage.setNextPage(str);
		return messagePage;
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
		beanModel = beanModelSource.create(CommissionLadder.class, true,
				componentResources);
		beanModel.get("siteCommissionValue").label("网站主佣金").sortable(false);
		beanModel.get("darwinCommissionValue").label("达闻佣金").sortable(false);
		beanModel.add("rangeCommissionValue",null).label("范围佣金").sortable(false);
		beanModel.get("isRange").label("是否范围佣金").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.get("startDate").label("起始时间");
		beanModel.get("endDate").label("结束时间");
		beanModel.add("operate", null).label("操作");
		beanModel.include("siteCommissionValue", "darwinCommissionValue",
				"rangeCommissionValue", "isRange","description", "startDate", "endDate",
				"operate");

	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("deleted", 0);
		if (campaignId != null && campaignId != 0) {
			conditions.put("campaign.id", campaignId);
		}
		query.setCondition(conditions);
		this.dataSource = new HibernateDataSource(commissionLadderService,
				query);
	}
	
	public String getRangeCommissionValue(){
		StringBuffer sbf = new StringBuffer();
		BigDecimal startCommission = commissionLadder.getStartCommission();
		BigDecimal endCommission = commissionLadder.getEndCommission();
		if(startCommission != null){
			sbf.append(startCommission);
			if(commissionLadder.getIsRange() == 1){
				sbf.append("--");
				sbf.append(endCommission);
			}
		}
		return sbf.toString();
	}
	
	public String getIsRange(){
		if(commissionLadder.getIsRange()==0){
			return "否";
		}else{
			return "是";
		}
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CommissionLadder> getBeanModel() {
		return beanModel;
	}
}
