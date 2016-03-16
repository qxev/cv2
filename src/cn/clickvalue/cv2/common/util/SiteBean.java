package cn.clickvalue.cv2.common.util;

import java.io.Serializable;

public class SiteBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String url;
	private String all;
	private Integer pvPerDay;

	public SiteBean(int id, String name, String url, Integer pvPerDay) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.pvPerDay = pvPerDay;
		this.all = id + "_" + name + "_" + url;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getAll() {
		return all;
	}

	public Integer getPvPerDay() {
		return pvPerDay;
	}

	public void setAll(String all) {
		this.all = all;
	}
}
