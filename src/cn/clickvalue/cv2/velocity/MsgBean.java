package cn.clickvalue.cv2.velocity;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class MsgBean {
	private VelocityEngine velocityEngine;

	private String msg;

	private Map<String, Object> model; // 用来保存velocity中的参数值

	private String encoding; // 编码

	private String templateLocation; // 注入的velocity模块

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getMsg() {
		// return title;
		// 将参数值注入到模块后的返回值
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				templateLocation, encoding, model);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}
