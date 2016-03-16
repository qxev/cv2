package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.RequestGlobals;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MatchData;
import cn.clickvalue.cv2.model.MatchTask;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.MatchDataService;
import cn.clickvalue.cv2.services.logic.MatchTaskService;

public class CommissionListPage extends BasePage {
	
	@Inject
	private SelectModelUtil selectModelUtil;
	
	
	@Component(id = "matchTaskSelect", parameters = {"value=matchTaskId","model=matchTasksModel","blankLabel=所有"} )
	private Select matchTaskSelect;
	
	@Property
	@Persist
	private Integer matchTaskId; 
	
	@Inject
	private RequestGlobals requestGlobals;

    @Persist
    @Property
    private MatchTask formMatchTask;
	
	@Persist
    @InjectSelectionModel(labelField="taskName",idField="taskId")
    private List<MatchTask> matchTasks = new ArrayList<MatchTask>();
	
	@Component(id = "grid", parameters = { "source=dataSource", "row=matchData",
			"model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;
	
	@Component(id = "cpa", parameters = {"value=effectType","model=literal:1=CPC,2=CPL,3=CPS,4=CPM","blankLabel=所有" } )
	private Select cpa;
	
	@Property
	@Persist
	private Integer effectType;

	@Component(id="form")
	private Form form;
	
	@Property
	private MatchData matchData;
	
	@Inject
	private MatchDataService matchDataService;

	@Inject
	private MatchTaskService matchTaskService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Property
	private GridDataSource dataSource;

	private BeanModel<MatchData> beanModel;

	@Property
	@Persist
	private String taskId;
	
	@Property
	@Persist
	private String taskName;
	
	@Property
	private String userAction;
	
	@Property
	private String operateId;
	 
	@Property
	@Persist
	private Integer advertiserId;
	
	@Property
	@Persist
	private Integer compaignId;
	
	public SelectModel getMatchTasksModel(){
		return selectModelUtil.getMatchTasksModel(matchTasks);
	}

	void onPrepare() {
	}
	
	@SetupRender
	public void setupRender() {
		
		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		query.addJoin("matchTask", "matchTask", Criteria.LEFT_JOIN);
		
		if (StringUtils.isNotBlank(this.taskId)) {
			map.put("matchTask.taskId", taskId);
		}
		
		if (this.compaignId  != null) {
			map.put("campaignId", compaignId);
		}
		
		if (StringUtils.isNotBlank(this.taskName)) {
			query.addCriterion(Restrictions.like("matchTask.taskName", this.taskName, MatchMode.ANYWHERE));
		}
		
		if (this.effectType != null) {
			map.put("effectType", effectType);
		}
		query.setCondition(map);
		dataSource = new HibernateDataSource(matchDataService, query);
		CritQueryObject critQueryObject = new CritQueryObject();
		Map<String, Object> condition = CollectionFactory.newMap();
		if (this.compaignId  != null) {
			condition.put("campaignId", this.compaignId);
		}
		critQueryObject.setCondition(condition);
		matchTasks = matchTaskService.find(critQueryObject);

	}
	
	void onSubmit() {
		//单个任务佣金匹配操作入口
		if ("match".equals(userAction)) {
//			System.out.println("match------->" + matchTaskId);
		//单条数据操作入口
		} else if ("update".equals(userAction)) {
//			System.out.println("update------->" + operateId);
		} else if ("delete".equals(userAction)) {
//			System.out.println("delete------->" + operateId);
		}
	}
	
	/*
	 * 页面初始化入口，佣金数据查询入口
	 */
    void onActivate(int campaignId) {
    	this.compaignId = campaignId;
    }

	public BeanModel<MatchData> getBeanModel() {
		beanModel = beanModelSource.create(MatchData.class, true,
				componentResources);
		
		beanModel.add("matchTask.taskId").label("任务号").sortable(false);
		beanModel.add("matchTask.taskName").label("任务名").sortable(false);
		beanModel.get("userId").label("管理员ID").sortable(false);
		beanModel.get("advertiserId").label("广告主ID").sortable(false);
		beanModel.get("campaignId").label("广告活动ID").sortable(false);
		beanModel.get("effectDate").label("广告效果发生的日期").sortable(false);
		beanModel.get("effectIp").label("IP").sortable(false);
		beanModel.get("effectCid").label("CID").sortable(false);
		beanModel.get("effectAid").label("AID").sortable(false);
		beanModel.add("effectTypeDisplay",null).label("绩效类型").sortable(false);
		beanModel.get("effectQuantity").label("总值").sortable(false);
		beanModel.get("effectAmount").label("消费总额").sortable(false);
		beanModel.get("effectCommision").label("佣金").sortable(false);
		beanModel.get("matchAction").label("操作").sortable(false);
		
		beanModel.include("matchTask.taskId","matchTask.taskName","userId","advertiserId","campaignId","effectDate",
				"effectIp","effectCid","effectAid","effectTypeDisplay","effectQuantity","effectAmount",
				"effectCommision","matchAction");
		return beanModel;
	}
	
	
	public String getEffectTypeDisplay(){
		Integer type = matchData.getEffectType();
		if (type == 100){
			return "CPC";
		} else if (type == 101){
			return "CPL";
		} else if (type == 102){
			return "CPS";
		} else if (type == 103){
			return "CPM";
		} else {
			return "";
		}
	}
	
}