package cn.clickvalue.cv2.common.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CheckBoxItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private boolean selected;

	private String name;

	public CheckBoxItem() {
	}

	public CheckBoxItem(Integer id, boolean selected, String name) {
		this.id = id;
		this.selected = selected;
		this.name = name;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).append("name",
				getName()).append("selected", isSelected()).toString();
	}
}
