package cn.clickvalue.cv2.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Change implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 一般是beanModel的类名
	 */
	private String entityName;
	
	private int entityId;

	/**
	 * 属性名
	 */
	private String attrName;

	/**
	 * 旧值
	 */
	private String from;

	/**
	 * 新值
	 */
	private String to;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof Change == false) {
			return false;
		}
		Change o = (Change) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.entityName, o.entityName);
		eb.append(this.entityId, o.entityId);
		eb.append(this.attrName, o.attrName);
		eb.append(this.from, o.from);
		eb.append(this.to, o.to);
		return eb.isEquals();
	}
}
