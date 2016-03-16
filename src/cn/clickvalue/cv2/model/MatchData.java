package cn.clickvalue.cv2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "match_data")
public class MatchData implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", referencedColumnName = "task_id" )
	private MatchTask matchTask;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "advertiser_id")
	private Integer advertiserId;
	
	@Column(name = "campaign_id")
	private Integer campaignId;
	
	@Column(name = "advertis_id")
	private Integer advertisId;
	
	@Column(name = "affilliate_id")
	private Integer affilliateId;
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "affilliate_advertise_id")
	private Integer affilliateAdvertiseId;
	
	@Column(name = "subsite_id")
	private String subsiteId;
	
	@Column(name = "effect_date")
	private Date effectDate;
	
	@Column(name = "effect_ip")
	private String effectIp;
	
	@Column(name = "effect_cid")
	private String effectCid;
	
	@Column(name = "effect_aid")
	private String effectAid;
	
	@Column(name = "effect_type")
	private Integer effectType;
	
	@Column(name = "effect_quantity")
	private Integer effectQuantity;
	
	@Column(name = "effect_amount")
	private float effectAmount;

	@Column(name = "effect_commision")
	private float effectCommision;

	@Column(name = "match_action")
	private String matchAction;
	
	@Column(name = "matched_check")
	private Integer matchedCheck;
	
	@Column(name = "matched_done")
	private Integer matchedDone;
	
	@Column(name = "matched_message")
	private String matchedMessage;
	
	@Column(name = "mapping_id")
	private Integer mappingId;
	
	@Column(name = "mapping_aid")
	private Integer mappingAid;
	
	@Column(name = "disabled")
	private Integer disabled;
	
	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public Integer getMappingAid() {
		return mappingAid;
	}

	public void setMappingAid(Integer mappingAid) {
		this.mappingAid = mappingAid;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getAdvertisId() {
		return advertisId;
	}

	public void setAdvertisId(Integer advertisId) {
		this.advertisId = advertisId;
	}

	public Integer getAffilliateId() {
		return affilliateId;
	}

	public void setAffilliateId(Integer affilliateId) {
		this.affilliateId = affilliateId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getAffilliateAdvertiseId() {
		return affilliateAdvertiseId;
	}

	public void setAffilliateAdvertiseId(Integer affilliateAdvertiseId) {
		this.affilliateAdvertiseId = affilliateAdvertiseId;
	}

	public String getSubsiteId() {
		return subsiteId;
	}

	public void setSubsiteId(String subsiteId) {
		this.subsiteId = subsiteId;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public String getEffectIp() {
		return effectIp;
	}

	public void setEffectIp(String effectIp) {
		this.effectIp = effectIp;
	}

	public String getEffectCid() {
		return effectCid;
	}

	public void setEffectCid(String effectCid) {
		this.effectCid = effectCid;
	}

	public String getEffectAid() {
		return effectAid;
	}

	public void setEffectAid(String effectAid) {
		this.effectAid = effectAid;
	}

	public Integer getEffectType() {
		return effectType;
	}

	public void setEffectType(Integer effectType) {
		this.effectType = effectType;
	}

	public Integer getEffectQuantity() {
		return effectQuantity;
	}

	public void setEffectQuantity(Integer effectQuantity) {
		this.effectQuantity = effectQuantity;
	}

	public float getEffectAmount() {
		return effectAmount;
	}

	public void setEffectAmount(float effectAmount) {
		this.effectAmount = effectAmount;
	}

	public String getMatchAction() {
		return matchAction;
	}

	public void setMatchAction(String matchAction) {
		this.matchAction = matchAction;
	}

	public Integer getMatchedCheck() {
		return matchedCheck;
	}

	public void setMatchedCheck(Integer matchedCheck) {
		this.matchedCheck = matchedCheck;
	}

	public Integer getMatchedDone() {
		return matchedDone;
	}

	public void setMatchedDone(Integer matchedDone) {
		this.matchedDone = matchedDone;
	}

	public String getMatchedMessage() {
		return matchedMessage;
	}

	public void setMatchedMessage(String matchedMessage) {
		this.matchedMessage = matchedMessage;
	}


	public float getEffectCommision() {
		return effectCommision;
	}

	public void setEffectCommision(float effectCommision) {
		this.effectCommision = effectCommision;
	}

	public MatchTask getMatchTask() {
		return matchTask;
	}

	public void setMatchTask(MatchTask matchTask) {
		this.matchTask = matchTask;
	}
	
}
