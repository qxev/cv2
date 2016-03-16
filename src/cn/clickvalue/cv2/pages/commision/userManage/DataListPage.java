package cn.clickvalue.cv2.pages.commision.userManage;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Cookies;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MatchData;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.MatchDataService;
import cn.clickvalue.cv2.services.logic.MatchTaskService;

public class DataListPage extends BasePage {

	@Persist
	@Property
	private String uid;
	
	@Property
	private MatchData matchData;
	
	@SuppressWarnings("unused")
	@Property
	@Persist
	private Integer formMatchedDone,formDisabled;

	@Property
	private String taskId;

	@SuppressWarnings("unused")
	@Property
	private Integer campaignId;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<MatchData> beanModel;

	private GridDataSource dataSource;

	@Inject
	private Cookies cookies;
	   
	@Inject
	private MatchDataService matchDataService;

	@Inject
	private MatchTaskService matchTaskService;

	void onActivate(String taskId) {
		if (taskId != null) {
			this.taskId = taskId;
		}
	}

	String onPassivate() {
		return taskId;
	}

	@SetupRender
	void SetupRender() {
		this.uid = cookies.readCookieValue("uid");
		campaignId = matchTaskService.findUniqueBy("taskId", taskId)
				.getCampaignId();
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("matchTask.taskId", taskId);
		if(formMatchedDone != null){
			conditions.put("matchedDone", formMatchedDone);
		}
		if(formDisabled != null){
			conditions.put("disabled", formDisabled);
		}
		query.setCondition(conditions);
		dataSource = new HibernateDataSource(matchDataService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(MatchData.class, true,
				componentResources);
		beanModel.get("effectDate").label("绩效日期").sortable(true);
		beanModel.get("effectIp").label("绩效IP").sortable(false);
		beanModel.get("effectCid").label("绩效唯一编号").sortable(false);
		beanModel.get("effectAid").label("TrackingCode").sortable(false);
		beanModel.get("effectType").label("绩效类型").sortable(false);
		beanModel.get("effectQuantity").label("绩效数").sortable(true);
		beanModel.get("effectAmount").label("绩效金额").sortable(true);
		beanModel.get("effectCommision").label("绩效佣金").sortable(true);
		beanModel.get("matchAction").label("操作类型").sortable(false);
		beanModel.get("matchedCheck").label("检查状态").sortable(false);
		beanModel.get("matchedDone").label("操作状态").sortable(false);
		beanModel.get("matchedMessage").label("操作消息").sortable(false);
		beanModel.get("disabled").label("数据状态").sortable(false);

		beanModel.include("effectDate", "effectIp", "effectCid", "effectAid",
				"effectType", "effectQuantity", "effectAmount",
				"effectCommision", "matchAction", "matchedDone",
				"matchedMessage", "disabled");
	}

	public String getEffectType() {
		int effectType = matchData.getEffectType();

		switch (effectType) {
		case 100:
			return "CPC";
		case 101:
			return "CPL";
		case 102:
			return "CPS";
		case 105:
			return "CPM";
		default:
			return "";
		}
	}
	public String getMatchedDone() {
		int matchDone = matchData.getMatchedDone();
		
		switch (matchDone) {
		case 0:
			return "待扫描";
		case 1:
			return "匹配映射成功";
		case 2:
			return "匹配补入成功";
		case 3:
			return "数据补入成功";
		default:
			return "";
		}
	}
	
	public String getDisabled(){
		if(matchData.getDisabled()==0){
			return "正常";
		}else{
			return "无效";
		}
	}

	public BeanModel<MatchData> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

}