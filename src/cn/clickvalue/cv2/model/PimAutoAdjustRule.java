package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class PimAutoAdjustRule extends PersistentObject {

	private static final long serialVersionUID = -5173965291450117413L;

	private Integer actionType;

	private Long actionValue;

	private Integer actionValueType;

	private String actionDesc;

	private Integer stateActiveMonth;

	private Integer stateUnActiveMonth;

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
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

	public Integer getStateActiveMonth() {
		return stateActiveMonth;
	}

	public void setStateActiveMonth(Integer stateActiveMonth) {
		this.stateActiveMonth = stateActiveMonth;
	}

	public Integer getStateUnActiveMonth() {
		return stateUnActiveMonth;
	}

	public void setStateUnActiveMonth(Integer stateUnActiveMonth) {
		this.stateUnActiveMonth = stateUnActiveMonth;
	}
}
