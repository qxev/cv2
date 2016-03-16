package cn.clickvalue.cv2.DTO;

import java.util.Date;

public class PimAutoAdjustRuleDTO {

	private Integer id;
	private int actionType;
	private Long actionValue;
	private Integer actionValueType;
	private String actionDesc;
	private Integer conditionValue;
	private int condition;
	private Date createdAt;
	private Date updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public Long getActionValue() {
		return actionValue;
	}

	public void setActionValue(Long actionValue) {
		this.actionValue = actionValue;
	}

	public Integer getActionValueType() {
		return actionValueType;
	}

	public void setActionValueType(Integer actionValueType) {
		this.actionValueType = actionValueType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public Integer getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(Integer conditionValue) {
		this.conditionValue = conditionValue;
	}
}
