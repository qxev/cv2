package cn.clickvalue.cv2.common.util;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.LandingPage;

public class SimpleData implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Banner banner;
	
	private Integer advertiseId;

	private List<LandingPage> list = new ArrayList<LandingPage>();
	
	public SimpleData() {
	}

	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public List<LandingPage> getList() {
		return list;
	}

	public void setList(List<LandingPage> list) {
		this.list = list;
	}

	public Integer getAdvertiseId() {
		return advertiseId;
	}

	public void setAdvertiseId(Integer advertiseId) {
		this.advertiseId = advertiseId;
	}
	
}
