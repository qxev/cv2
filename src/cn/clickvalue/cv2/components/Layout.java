package cn.clickvalue.cv2.components;

import org.apache.tapestry5.annotations.Parameter;

public class Layout {

	@Parameter(defaultPrefix = "literal")
	private String pageTitle = " clickvalue.cn 达闻广告管理平台 ";

	public String getPageTitle() {
		return pageTitle;
	}

}
