package cn.clickvalue.cv2.email;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.services.logic.MailTemplateService;

public class SystemMailSender {
	
	Log logger = LogFactory.getLog(SystemMailSender.class);

    private JavaMailSender mailSender;

    private String systemMail;

    private String encode;

    private MailTemplateService mailTemplateService;

    /**
     * @param subject
     * @param content
     * @param receivers
     *            给接收者发邮件
     */
    public void send(String subject, String content, String... receivers) throws BusinessException {
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        if (receivers.length == 0)
            return;
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true,
                    encode);
            helper.setFrom(systemMail);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(receivers);
            mailSender.send(mimeMsg);
        } catch (Exception e) {
        	logger.debug("邮件发送失败", e);
        }
    }

    /**
     * @param mailTemplate
     *            邮件模板
     * @param mailDate
     *            模板中变量对应的数据
     * @param receivers
     *            接收者
     * 
     * 自动把用户传入的模板用mailDate中的数据替换掉后发送给接受者
     */
    public void send(MailTemplate mailTemplate, Map<String, String> mailDate,
            String... receivers) throws BusinessException {
        // 生成替换后的模板
        mailTemplateService.replaceMailTemplateBody(mailTemplate, mailDate);
        send(mailTemplate.getSubject(), mailTemplate.getBody(), receivers);
    }

    /**
     * @param mailTypeId
     * @param mailDate
     * @param receivers
     * 
     * 用户传入要发送的邮件类型和数据，自动替换生成模板，发送邮件
     */
    public void send(int mailTypeId, String language,
            Map<String, String> mailDate, String... receivers) {
        // 根据邮件类型生成替换后的模板
        MailTemplate mailTemplate = mailTemplateService
                .replaceMailTemplateBody(mailTypeId, language, mailDate);
        send(mailTemplate.getSubject(), mailTemplate.getBody(), receivers);
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSystemMail(String systemMail) {
        this.systemMail = systemMail;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setMailTemplateService(MailTemplateService mailTemplateService) {
        this.mailTemplateService = mailTemplateService;
    }
}
