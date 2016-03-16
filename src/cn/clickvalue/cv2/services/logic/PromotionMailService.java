package cn.clickvalue.cv2.services.logic;

import java.util.List;

public class PromotionMailService {

	private String templateRootPath;

	private List<String> templateNames;

	public String getTemplatePathByName(String templateName) {

		// if(!templateNames.contains(templateName)){
		// return
		// RealPath.getRoot().concat("/").concat(templateRootPath).concat("/").concat(templateNames.get(0));
		// }
		// return
		// RealPath.getRoot().concat("/").concat(templateRootPath).concat("/").concat(templateName);

		if (!templateNames.contains(templateName)) {
			return templateRootPath.concat("/").concat(templateNames.get(0));
		}
		return templateRootPath.concat("/").concat(templateName);

	}

	public String getTemplateRootPath() {
		return templateRootPath;
	}

	public void setTemplateRootPath(String templateRootPath) {
		this.templateRootPath = templateRootPath;
	}

	public List<String> getTemplateNames() {
		return templateNames;
	}

	public void setTemplateNames(List<String> templateNames) {
		this.templateNames = templateNames;
	}

}
