package cn.clickvalue.cv2.model;

import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "userinfo")
public class UserInfo extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private String city;

	private String company;

	private String contact;

	private String country;

	private String description;

	private String fax;

	private String mobile;

	private String msn;

	private String phone;

	private String postcode;

	private Integer qq;

	private String state;

	private String street;

	public UserInfo() {
	}

	public String getCity() {
		return this.city;
	}

	public String getCompany() {
		return this.company;
	}

	public String getContact() {
		return this.contact;
	}

	public String getCountry() {
		return this.country;
	}

	public String getDescription() {
		return this.description;
	}

	public String getFax() {
		return this.fax;
	}

	public String getMsn() {
		return this.msn;
	}

	public String getPhone() {
		return this.phone;
	}

	public Integer getQq() {
		return this.qq;
	}

	public String getState() {
		return this.state;
	}

	public String getStreet() {
		return this.street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setQq(Integer qq) {
		this.qq = qq;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}