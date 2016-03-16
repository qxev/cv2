package cn.clickvalue.cv2.report.api;

public class Field implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name = "";
	private boolean canOrder = false;
	private String chinese = "";
	private String english = "";
	private String order = "";
	private String compared = "";
	private boolean hasCompared = false;

	public boolean isHasCompared() {
		return hasCompared;
	}

	public void setHasCompared(boolean hasCompared) {
		this.hasCompared = hasCompared;
	}

	public String getCompared() {
		return compared;
	}

	public void setCompared(String compared) {
		this.compared = compared;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getCanOrder() {
		return canOrder;
	}

	public void setCanOrder(boolean canOrder) {
		this.canOrder = canOrder;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}
}
