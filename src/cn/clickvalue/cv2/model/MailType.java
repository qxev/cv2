package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class MailType extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * 是否强制用户接收此邮件 0 => 用户可选择是否接收 1 => 强制发送邮件
	 */
	private Integer forced;

	@OneToMany(mappedBy = "mailType")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<MailTemplate> mailTemplates;

	@OneToMany(mappedBy = "mailType")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<MailTypeUser> mailTypeUsers;

	public MailType() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MailTemplate> getMailTemplates() {
		return mailTemplates;
	}

	public void setMailTemplates(List<MailTemplate> mailTemplates) {
		this.mailTemplates = mailTemplates;
	}

	public Integer getForced() {
		return forced;
	}

	public void setForced(Integer forced) {
		this.forced = forced;
	}

	public List<MailTypeUser> getMailTypeUsers() {
		return mailTypeUsers;
	}

	public void setMailTypeUsers(List<MailTypeUser> mailTypeUsers) {
		this.mailTypeUsers = mailTypeUsers;
	}

}