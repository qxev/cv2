package cn.clickvalue.cv2.pages.admin.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.PromotionMailService;
import cn.clickvalue.cv2.velocity.MsgBean;

public class MailPreviewPage extends BasePage {

	@Persist
	private Map<String, Object> model;
	
	@Persist
	private String subject;

	@Inject
	private MsgBean msgBean;

	@Property
	@InjectSelectionModel(labelField = "label", idField = "value")
	private List<LabelValueModel> templateNames = new ArrayList<LabelValueModel>();

	@Persist
	@Property
	private LabelValueModel templateName;

	@Inject
	private PromotionMailService promotionMailService;
	
	@Inject
	private BusinessMailSender businessMailSender;
	
	@InjectPage
	private MessagePage messagePage;

	void setupRender() {

		if (model == null) {
			addError("没有要发送的内容", false);
		}

		// 模板
		List<String> templates = promotionMailService.getTemplateNames();
		templateNames.clear();
		for (int i = 0; i < templates.size(); i++) {
			LabelValueModel labelValueModel = new LabelValueModel();
			labelValueModel.setLabel("模板".concat(String.valueOf(i+1)));
			labelValueModel.setValue(templates.get(i));
			templateNames.add(labelValueModel);
		}

		// 选中到模板，默认为第一个
		String tempaltePath = null;
		if (templateName == null) {
			tempaltePath = promotionMailService.getTemplatePathByName(templates.get(0));
		} else {
			tempaltePath = promotionMailService.getTemplatePathByName(templateName.getValue());
		}
		
		//初始化msgBean
		msgBean.setTemplateLocation(tempaltePath);
		msgBean.setModel(model);

	}
	
	public Object onSend(){
		try {
			businessMailSender.sendPromotionMail(msgBean.getMsg(),subject);
			messagePage.setMessage("邮件发送成功");
			messagePage.setNextPage("admin/mail/promotionmailpage");
			return messagePage;
		} catch (BusinessException e) {
			this.addError(e.getMessage(), false);
			return this;
		}
	}
	
	public Object onTest(String str){
		if(StringUtils.isBlank(str)){
			addError("收件人邮箱不能为空", false);
			return this;
		}
		String[] receivers = str.split(",");
		StringBuilder err = new StringBuilder();
		for(String receiver : receivers){
			if(!ValidateUtils.isEmail(receiver)){
				err.append(receiver);
				err.append("\n\r");
			}
		}
		if(err.length() > 0){
			addError(err.insert(0, "邮箱格式不正确:\n\r").toString(), false);
			return this;
		}
		
		businessMailSender.sendMail(subject, msgBean.getMsg(), receivers);
		messagePage.setMessage("邮件发送成功");
		messagePage.setNextPage("admin/mail/previewpage");
		return messagePage;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public MsgBean getMsgBean() {
		return msgBean;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
