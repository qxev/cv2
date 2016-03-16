package cn.clickvalue.cv2.tracking;

public class TrackerModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int siteId;

	private String siteName;

	private String siteUrl;

	private boolean autoAdManage;

	private int affId;

	private int aId;

	private int campaignId;

	private int advHeight;

	private int advWidth;

	private String type;

	private String landPageUrl;
	
	private String text;

	private int targetWindow;
	
	private String pictureUrl;
	
	private String partnerId;
	
	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public boolean isAutoAdManage() {
		return autoAdManage;
	}

	public void setAutoAdManage(boolean autoAdManage) {
		this.autoAdManage = autoAdManage;
	}

	public int getAffId() {
		return affId;
	}

	public void setAffId(int affId) {
		this.affId = affId;
	}

	public int getAId() {
		return aId;
	}

	public void setAId(int id) {
		aId = id;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public int getAdvHeight() {
		return advHeight;
	}

	public void setAdvHeight(int advHeight) {
		this.advHeight = advHeight;
	}

	public int getAdvWidth() {
		return advWidth;
	}

	public void setAdvWidth(int advWidth) {
		this.advWidth = advWidth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLandPageUrl() {
		return landPageUrl;
	}

	public void setLandPageUrl(String landPageUrl) {
		this.landPageUrl = landPageUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTargetWindow() {
		return targetWindow;
	}

	public void setTargetWindow(int targetWindow) {
		this.targetWindow = targetWindow;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPartnerId() {
		return partnerId==null? "" : partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
}
