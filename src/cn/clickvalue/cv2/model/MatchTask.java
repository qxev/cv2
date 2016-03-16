package cn.clickvalue.cv2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@javax.persistence.Entity
@Table(name = "match_task")
public class MatchTask implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "task_id")
	private String taskId;
	
	@Column(name = "task_name")
	private String taskName;
	
	@Column(name = "task_date")
	private Date taskDate;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "campaign_id")
	private Integer campaignId;
	
	@Column(name = "match_total")
	private Integer matchTotal;
	
	@Column(name = "check_success")
	private Integer checkSuccess;
	
	@Column(name = "check_failed")
	private Integer checkFailed;
	
	@Column(name = "match_success")
	private Integer matchSuccess;

	@Column(name = "match_failed")
	private Integer matchFailed;

	@Column(name = "check_message")
	private String checkMessage;
	
	@Column(name = "match_message")
	private String matchMessage;
	
	@Column(name = "exe_status")
	private Integer exeStatus;
	
	@Column(name = "restart_task")
	private Integer restartTask;
	
	@Column(name = "no_confirm_data")
	private Integer dataFrom;
	
	@Column(name = "match_action")
	private String matchAction;
	
	@Column(name = "task_startdate")
	private Date taskStartdate;
	
	@Column(name = "task_enddate")
	private Date taskEnddate;
	
	@Column(name = "exe_starttime")
	private Date exeStarttime;
	
	@Column(name = "exe_endtime")
	private Date exeEndtime;
	
	@OneToMany(mappedBy = "matchTask")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<MatchData> matchDatas;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getMatchTotal() {
		return matchTotal;
	}

	public void setMatchTotal(Integer matchTotal) {
		this.matchTotal = matchTotal;
	}

	public Integer getCheckSuccess() {
		return checkSuccess;
	}

	public void setCheckSuccess(Integer checkSuccess) {
		this.checkSuccess = checkSuccess;
	}

	public Integer getCheckFailed() {
		return checkFailed;
	}

	public void setCheckFailed(Integer checkFailed) {
		this.checkFailed = checkFailed;
	}

	public Integer getMatchSuccess() {
		return matchSuccess;
	}

	public void setMatchSuccess(Integer matchSuccess) {
		this.matchSuccess = matchSuccess;
	}

	public Integer getMatchFailed() {
		return matchFailed;
	}

	public void setMatchFailed(Integer matchFailed) {
		this.matchFailed = matchFailed;
	}

	public String getCheckMessage() {
		return checkMessage;
	}

	public void setCheckMessage(String checkMessage) {
		this.checkMessage = checkMessage;
	}

	public String getMatchMessage() {
		return matchMessage;
	}

	public void setMatchMessage(String matchMessage) {
		this.matchMessage = matchMessage;
	}

	public Integer getExeStatus() {
		return exeStatus;
	}

	public void setExeStatus(Integer exeStatus) {
		this.exeStatus = exeStatus;
	}

	public Integer getRestartTask() {
		return restartTask;
	}

	public void setRestartTask(Integer restartTask) {
		this.restartTask = restartTask;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getMatchAction() {
		return matchAction;
	}

	public void setMatchAction(String matchAction) {
		this.matchAction = matchAction;
	}

	public Date getTaskStartdate() {
		return taskStartdate;
	}

	public void setTaskStartdate(Date taskStartdate) {
		this.taskStartdate = taskStartdate;
	}

	public Date getTaskEnddate() {
		return taskEnddate;
	}

	public void setTaskEnddate(Date taskEnddate) {
		this.taskEnddate = taskEnddate;
	}

	public Date getExeStarttime() {
		return exeStarttime;
	}

	public void setExeStarttime(Date exeStarttime) {
		this.exeStarttime = exeStarttime;
	}

	public Date getExeEndtime() {
		return exeEndtime;
	}

	public void setExeEndtime(Date exeEndtime) {
		this.exeEndtime = exeEndtime;
	}

	public Integer getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(Integer dataFrom) {
		this.dataFrom = dataFrom;
	}

	public List<MatchData> getMatchDatas() {
		return matchDatas;
	}

	public void setMatchDatas(List<MatchData> matchDatas) {
		this.matchDatas = matchDatas;
	}
	
}
