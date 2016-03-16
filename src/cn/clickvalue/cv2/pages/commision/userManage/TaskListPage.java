package cn.clickvalue.cv2.pages.commision.userManage;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Cookies;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MatchTask;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.MatchDataService;
import cn.clickvalue.cv2.services.logic.MatchTaskService;

public class TaskListPage extends BasePage {

	@Persist
	@Property
	private String uid;

	@Property
	private MatchTask matchTask;

	@Property
	private Integer campaignId;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<MatchTask> beanModel;

	private GridDataSource dataSource;

	@Inject
	private MatchTaskService matchTaskService;

	@Inject
	private MatchDataService matchDataService;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private Cookies cookies;

	void onActivate(Integer campaignId) {
		if (campaignId != null && campaignId > 0) {
			this.campaignId = campaignId;
		}
	}

	Integer onPassivate() {
		return campaignId;
	}

	@SetupRender
	void SetupRender() {
		this.uid = cookies.readCookieValue("uid");
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaignId", campaignId);

		query.setCondition(conditions);
		query.addOrder(Order.desc("taskDate"));
		dataSource = new HibernateDataSource(matchTaskService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(MatchTask.class, true, componentResources);
		beanModel.get("taskName").label("任务名称").sortable(false);
		beanModel.get("matchAction").label("执行模式").sortable(false);
		beanModel.get("taskStartdate").label("数据开始时间").sortable(false);
		beanModel.get("taskEnddate").label("数据结束时间").sortable(false);
		beanModel.get("dataFrom").label("数据来源").sortable(false);
		beanModel.get("matchTotal").label("实际数据量").sortable(false);
		beanModel.get("matchSuccess").label("匹配成功").sortable(false);
		beanModel.get("exeStatus").label("操作状态").sortable(false);
		beanModel.get("checkMessage").label("检查消息").sortable(false);
		beanModel.get("matchMessage").label("匹配消息").sortable(false);
		beanModel.get("exeStarttime").label("任务执行开始时间").sortable(false);
		beanModel.get("exeEndtime").label("任务执行结束时间").sortable(false);
		beanModel.add("taskDataTotal", null).label("任务数据量").sortable(false);
		beanModel.add("operate", null).label("操作").sortable(false);

		beanModel.include("taskName", "matchAction", "exeStatus", "taskStartdate", "taskEnddate", "dataFrom", "taskDataTotal",
				"matchTotal", "matchSuccess", "checkMessage", "matchMessage", "exeStarttime", "exeEndtime", "operate");
	}

	public String getExeStatus() {
		int exeStatus = matchTask.getExeStatus();
		int enabled = matchTask.getRestartTask();

		if (enabled == 0) {
			return "不可用";
		}

		switch (exeStatus) {
		case 0:
			return "待启动";
		case 1:
			return "已启动";
		case 2:
			return "扫描分析数据";
		case 3:
			return "执行完毕";
		default:
			return "不可用";
		}
	}

	public String getDataFrom() {
		int dataFrom = matchTask.getDataFrom();
		if (dataFrom == 0) {
			return "客户提供";
		} else if (dataFrom == 1) {
			return "系统提供";
		} else if (dataFrom == 2) {
			return "IPD";
		} else {
			return "未知";
		}
	}

	public Integer getTaskDataTotal() {
		return matchTask.getMatchDatas().size();
	}

	/**
	 * @param taskId
	 * @return 启动任务 更新restartTask 字段为1，前提是restartTask==0
	 */
	Object onStartTask(String taskId) {
		MatchTask task = matchTaskService.findUniqueBy("taskId", taskId);
		if (task.getRestartTask() != 0) {
			messagePage.setMessage("任务已是启动状态");
			messagePage.setNextPage("commision/usermanage/tasklistpage/".concat(String.valueOf(campaignId)));
			return messagePage;
		}
		task.setRestartTask(1);
		matchTaskService.save(task);
		messagePage.setMessage("启动成功");
		messagePage.setNextPage("commision/usermanage/tasklistpage/".concat(String.valueOf(campaignId)));
		return messagePage;
	}

	/**
	 * @param taskId
	 * @return 重启任务，更新exeStatus字段为0，前提是exeStatus=3
	 */
	Object onRestartTask(String taskId) {
		MatchTask task = matchTaskService.findUniqueBy("taskId", taskId);
		if (task.getExeStatus() != 3) {
			messagePage.setMessage("任务未执行完毕");
			messagePage.setNextPage("commision/usermanage/tasklistpage/".concat(String.valueOf(campaignId)));
			return messagePage;
		}
		task.setRestartTask(1);
		task.setExeStatus(0);
		matchTaskService.save(task);
		messagePage.setMessage("操作成功");
		messagePage.setNextPage("commision/usermanage/tasklistpage/".concat(String.valueOf(campaignId)));
		return messagePage;
	}

	public BeanModel<MatchTask> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

}