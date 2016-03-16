package cn.clickvalue.cv2.pages.admin;

import org.apache.tapestry5.annotations.Persist;

import cn.clickvalue.cv2.components.admin.BasePage;

public class BatchFileUpload extends BasePage {

	@Persist
	private String folder;

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
