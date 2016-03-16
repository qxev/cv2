package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class Account extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@SuppressWarnings("unused")
	@Transient
	private String defaultAccounts;

	/**
	 *0:银行 1:支付宝
	 */
	private Integer type;

	private Integer defaultAccount;

	private String refuseReason;

	private Integer deleted;

	private Integer verified;

	private String bankName;

	private String province;

	private String city;

	private String subBank;

	private String cardNumber;

	private String idCardNumber;

	private String ownerName;

	private String ownerTelephone;

	private String ownerAddress;

	private String postcode;

	@OneToMany(mappedBy = "account")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CommissionExpense> affiliateCommission;

	public Account() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public String getVerifiedDisplay() {
		String str = "";
		switch (verified) {
		case 0:
			str = "未提交";
			break;
		case 1:
			str = "申请下线";
			break;
		case 2:
			str = "已批准";
			break;
		case 3:
			str = "已拒绝";
			break;
		case 4:
			str = "申请下线";
			break;
		}
		return str;
	}

	/**
	 * @param account
	 * @return
	 * 
	 *         从业务角度比较2个银行账号是否相同
	 */
	@Deprecated
	public boolean compareWith(Account account) {
		if (this.getId() == account.getId()) {
			return true;
		}
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.cardNumber, account.cardNumber);
		eb.append(this.idCardNumber, account.idCardNumber);
		eb.append(this.ownerName, account.ownerName);
		eb.append(this.province, account.province);
		eb.append(this.city, account.city);
		eb.append(this.bankName, account.bankName);
		eb.append(this.subBank, account.subBank);
		eb.append(this.postcode, account.postcode);
		return eb.isEquals();
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Integer getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(Integer defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerTelephone() {
		return ownerTelephone;
	}

	public void setOwnerTelephone(String ownerTelephone) {
		this.ownerTelephone = ownerTelephone;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public List<CommissionExpense> getAffiliateCommission() {
		return affiliateCommission;
	}

	public void setAffiliateCommission(List<CommissionExpense> affiliateCommission) {
		this.affiliateCommission = affiliateCommission;
	}

	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	public String getSubBank() {
		return subBank;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getDefaultAccounts() {
		return getDefaultAccount() == null || getDefaultAccount() == 0 ? "否" : "是";
	}

	public void setDefaultAccounts(String defaultAccounts) {
		this.defaultAccounts = defaultAccounts;
	}

	public String getAddress() {
		String str = getProvince().concat(" ").concat(getCity());
		return str;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}