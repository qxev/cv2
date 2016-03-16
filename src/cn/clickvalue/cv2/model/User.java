package cn.clickvalue.cv2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "user")
public class User extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Account> accounts;

	private Integer actived;

	private Date activedAt;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AdvertiserDeposit> advertiserDeposits;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<AffiliateCampaign> affiliateCampaigns;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Bonus> bonuses;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Campaign> campaigns;

	private Integer commissionApplied;
	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<CommissionIncome> commissionIncomes;

	@Transient
	private String confirmPassword;

	private Integer deleted;

	private Date deletedAt;

	private String email;

	private Integer hasBank;

	/**
	 * 用户是否已经添加联系方法
	 */
	private Integer hasContact;

	/**
	 * 用户是否已经添加站点
	 */
	private Integer hasSite;

	/**
	 * 用户自定义菜单语言 0 => CH 1 => EN
	 */
	@Column(nullable = false)
	private Integer language;
	private Integer logined;

	private Date loginedAt;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<MailTypeUser> mailTypeUsers;

	private String name;

	@Column(unique = true)
	private String nickName;

	private String password;

	@OneToMany(mappedBy = "advertiser")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<SemClient> semClient;

	@OneToMany(mappedBy = "user")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<Site> sites;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userGroupId", nullable = false)
	private UserGroup userGroup;

	/**
	 * 级联更新在用户注册和用户信息修改中用到，不要删除级联关系
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userInfoId", nullable = false, unique = true)
	@Cascade({CascadeType.SAVE_UPDATE})
	private UserInfo userInfo;
	
	private String authKey;

	@Transient
	private String validateCode;
	
	private Integer verified;

	private Date verifiedAt;

	public User() {
	}
	
	public User(Integer id, String name){
		super.setId(id);
		this.name = name;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public Integer getActived() {
		return actived;
	}

	public Date getActivedAt() {
		return activedAt;
	}

	public List<AdvertiserDeposit> getAdvertiserDeposits() {
		return advertiserDeposits;
	}

	public List<AffiliateCampaign> getAffiliateCampaigns() {
		return affiliateCampaigns;
	}

	public List<Bonus> getBonuses() {
		return bonuses;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public Integer getCommissionApplied() {
		return commissionApplied;
	}

	public List<CommissionIncome> getCommissionIncomes() {
		return commissionIncomes;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public String getEmail() {
		return email;
	}

	public Integer getHasBank() {
		return hasBank;
	}

	public Integer getHasContact() {
		return hasContact;
	}

	public Integer getHasSite() {
		return hasSite;
	}

	public Integer getLanguage() {
		return language;
	}

	public Integer getLogined() {
		return logined;
	}

	public Date getLoginedAt() {
		return loginedAt;
	}

	public List<MailTypeUser> getMailTypeUsers() {
		return mailTypeUsers;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getPassword() {
		return password;
	}

	public List<Site> getSites() {
		return sites;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public Integer getVerified() {
		return verified;
	}

	public Date getVerifiedAt() {
		return verifiedAt;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public void setActivedAt(Date activedAt) {
		this.activedAt = activedAt;
	}

	public void setAdvertiserDeposits(List<AdvertiserDeposit> advertiserDeposits) {
		this.advertiserDeposits = advertiserDeposits;
	}

	public void setAffiliateCampaigns(List<AffiliateCampaign> affiliateCampaigns) {
		this.affiliateCampaigns = affiliateCampaigns;
	}

	public void setBonuses(List<Bonus> bonuses) {
		this.bonuses = bonuses;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public void setCommissionApplied(Integer commissionApplied) {
		this.commissionApplied = commissionApplied;
	}

	public void setCommissionIncomes(List<CommissionIncome> commissionIncomes) {
		this.commissionIncomes = commissionIncomes;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setHasBank(Integer hasBank) {
		this.hasBank = hasBank;
	}

	public void setHasContact(Integer hasContact) {
		this.hasContact = hasContact;
	}

	public void setHasSite(Integer hasSite) {
		this.hasSite = hasSite;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public void setLogined(Integer logined) {
		this.logined = logined;
	}

	public void setLoginedAt(Date loginedAt) {
		this.loginedAt = loginedAt;
	}

	public void setMailTypeUsers(List<MailTypeUser> mailTypeUsers) {
		this.mailTypeUsers = mailTypeUsers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public void setVerifiedAt(Date verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public void setSemClient(List<SemClient> semClient) {
		this.semClient = semClient;
	}

	public List<SemClient> getSemClient() {
		return semClient;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
}