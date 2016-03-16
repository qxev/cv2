package cn.clickvalue.cv2.tracking;

import org.apache.tapestry5.ioc.Messages;

import cn.clickvalue.cv2.model.Campaign;

/**
 * 用法示例： Tracker trk = new Tracker(0,2,Tracker.TYPE_OF_PUBLISHER);
 * trk.addPublisherAdvertising(1, "Jackie", "http://www.jackie.com", false, 1,1,
 * 11, 60, 500,"0"); trk.addPublisherAdvertising(2, "David",
 * "http://www.David.com", false, 2,1,11, 60, 500,"1"); String page =
 * trk.getPublisherTrackingCodePage();
 * 
 * @author jackie
 * 
 */
public class Tracker {
	public static final int TRACK_FOR_CPC = 100;
	public static final int TRACK_FOR_CPL = 101;
	public static final int TRACK_FOR_CPS = 102;

	public Publisher publisher = null;
	public Advertiser advertiser = null;

	/**
	 * 构造一个跟踪代码创建对象
	 * 
	 * @param lang
	 *            语言版本
	 * @param advertiserId
	 *            广告主编号
	 * @param publisherId
	 *            发布者编号
	 */
	public Tracker(int lang, int advertiserId, int publisherId) {
		advertiser = new Advertiser();
		advertiser.LANG = lang;
		advertiser.id = advertiserId;

		publisher = new Publisher();
		publisher.LANG = lang;
		publisher.id = publisherId;
	}

	/**
	 * 构造一个广告托管跟踪代码创建对象
	 * 
	 * @param lang
	 *            语言版本
	 * @param publisherId
	 *            发布者编号
	 */
	public Tracker(int lang, int publisherId) {
		publisher = new Publisher();
		publisher.LANG = lang;
		publisher.id = publisherId;
	}

	// public void addPublisherAdvertising(int siteId,String siteName,String
	// siteUrl,int affId,int aId,int campaignId,int advHeight,int
	// advWidth,String type,String landingURL,int campaignType, String
	// partnerId) {
	// addPublisherAdvertising(siteId,siteName,siteUrl,false,affId,aId,campaignId,advHeight,advWidth,type,landingURL,campaignType,
	// partnerId);
	// }

	/**
	 * 添加一个网站要做的广告信息 !!!!!!!!!注意：在调用本方法前，一定要设置 tracker.advertiser.semId
	 * 的值，该值代表广告主在SEM系统中的ID值 ！！！！！！！必须在获取代码后更新关系表中的trackcode字段为对应顺序的tracker.
	 * getAffilliateTrackCodeIds()集合中的值
	 * 
	 * @param siteId
	 *            网站编号
	 * @param siteName
	 *            网站名称
	 * @param siteUrl
	 *            网站网址
	 * @param autoAdManage
	 *            是否允许自动托管
	 * @param affId
	 *            网站与广告的组合ID
	 * @param aId
	 *            广告的ID，指Banner与Landing Page组合的ID
	 * @param campaignId
	 *            广告活动ID
	 * @param advHeight
	 *            广告高度
	 * @param advWidth
	 *            广告宽度
	 * @param type
	 *            广告的类型：0：图片；1：文本；2：Flash；3：HTML
	 * @param landingURL
	 *            广告Landing地址
	 * @param parameters
	 *            广告活动级别的参数
	 */
	public void addPublisherAdvertising(int siteId, String siteName, String siteUrl, boolean autoAdManage, int affId, int aId,
			int campaignId, int advHeight, int advWidth, String type, int partnerId, String parameters) {
		Website ws = new Website();
		ws.id = siteId;
		ws.site_name = siteName;
		ws.site_url = siteUrl;
		ws.enableAutoAd = autoAdManage;
		ws.ad.campaign_id = campaignId;
		ws.ad.affId = affId;
		ws.ad.aid = aId;
		ws.ad.height = advHeight;
		ws.ad.width = advWidth;
		ws.ad.type = type;
		ws.parameters = parameters;
		ws.campaignType = partnerId;
		ws.partnerId = "123.45.6";
		publisher.sites.add(ws);
	}

	/**
	 * 添加一个要托管的网站主网站
	 * 
	 * @param siteId
	 * @param siteName
	 * @param siteUrl
	 * @param affId
	 * @param advHeight
	 * @param advwidth
	 * @param categoryIds
	 */
	public void addTrustPublisher(int siteId, String siteName, String siteUrl, int affId, int advHeight, int advwidth, int... categoryIds) {
		Website ws = new Website();
		ws.id = siteId;
		ws.site_name = siteName;
		ws.site_url = siteUrl;
		ws.ad.affId = affId;
		ws.ad.height = advHeight;
		ws.ad.width = advwidth;
		ws.categoryIds = categoryIds;
		publisher.sites.add(ws);
	}

	public void addPublisherAdvertising(TrackerModel model, Campaign campaign, boolean autoAdManage) {
		Website ws = new Website();
		ws.id = model.getSiteId();
		ws.site_name = model.getSiteName();
		ws.site_url = model.getSiteUrl();
		ws.enableAutoAd = autoAdManage;
		ws.ad.campaign_id = model.getCampaignId();
		ws.ad.affId = model.getAffId();
		ws.ad.aid = model.getAId();
		ws.ad.height = model.getAdvHeight();
		ws.ad.width = model.getAdvWidth();
		ws.ad.type = model.getType();
		ws.ad.link = model.getLandPageUrl();
		ws.parameters = campaign.getParameters();
		ws.campaignType = campaign.getPartnerType();
		ws.partnerId = model.getPartnerId();
		publisher.sites.add(ws);
	}

	public void addCustomAdvertising(int siteId, String siteName, String siteUrl, boolean autoAdManage, int affId, int aId, int campaignId,
			int advHeight, int advWidth, String type, String landingURL, String text, int targetWindow, String pictureUrl, String parameters) {
		Website ws = new Website();
		ws.id = siteId;
		ws.site_name = siteName;
		ws.site_url = siteUrl;
		ws.parameters = parameters;
		ws.enableAutoAd = autoAdManage;
		ws.ad.campaign_id = campaignId;
		ws.ad.affId = affId;
		ws.ad.aid = aId;
		ws.ad.height = advHeight;
		ws.ad.width = advWidth;
		ws.ad.type = type;
		ws.ad.link = landingURL;
		ws.ad.text = text;
		ws.ad.targetWindow = targetWindow;
		ws.ad.pictureUrl = pictureUrl;
		publisher.sites.add(ws);
	}

	public void addIframeAdvertising(int siteId, String siteName, String siteUrl, boolean autoAdManage, int affId, int aId, int campaignId,
			int advHeight, int advWidth, String type, String landingURL, String text, String headHeight, String headUrl, String parameters,
			String pictureUrl) {
		Website ws = new Website();
		ws.id = siteId;
		ws.site_name = siteName;
		ws.site_url = siteUrl;
		ws.parameters = parameters;
		ws.enableAutoAd = autoAdManage;
		ws.ad.campaign_id = campaignId;
		ws.ad.affId = affId;
		ws.ad.aid = aId;
		ws.ad.height = advHeight;
		ws.ad.width = advWidth;
		ws.ad.type = type;
		ws.ad.link = landingURL;
		ws.ad.text = text;
		ws.ad.headHeight = headHeight;
		ws.ad.headUrl = headUrl;
		ws.ad.pictureUrl = pictureUrl;
		publisher.sites.add(ws);
	}

	/**
	 * 获取网站主跟踪代码
	 * 
	 * @return HTML格式文件源码，内部包含各网站所做广告的跟踪代码
	 */
	public String getPublisherTrackingCodePage() {
		String code = "";
		if (publisher == null) {
		} else if (advertiser == null) {
			code = publisher.getTrackingCode();
		} else {
			code = publisher.getTrackingCode(this.advertiser);
		}
		return code;
	}

	/**
	 * 网站主获得广告链接
	 * 
	 * @return
	 */
	public String getPublisherTrackingLink() {
		String code = "";
		if (publisher == null) {
		} else {
			code = publisher.getTrackingLink(this.advertiser);
		}
		return code;
	}

	/**
	 * 网站主获得自定义广告链接
	 * 
	 * @return
	 */
	public String getCustomLink() {
		String code = "";
		if (publisher == null) {
		} else {
			code = publisher.getCustomLink(this.advertiser);
		}
		return code;
	}

	/**
	 * 网站主获得Iframe广告链接
	 * 
	 * @return
	 */
	public String getIframeLink() {
		String code = "";
		if (publisher == null) {
		} else {
			code = publisher.getIframeLink(this.advertiser);
		}
		return code;
	}

	/**
	 * 获得广告主跟踪代码
	 * 
	 * @param forStepId
	 *            为哪一个Step的跟踪 cpc = 0 ,cpl = 1, cps = 2
	 * @param forStepType
	 *            跟踪的类型，调用Tracker.TRACK_FOR_?来指定对应目的
	 * @return 跟踪代码
	 */
	public String getAdvertiserTrackingCodes(int forStepId, int forStepType, Messages message) {
		String code = "";
		if (advertiser == null) {
		} else {
			code = advertiser.getTrackingCode(forStepId, forStepType, message);
		}
		return code;
	}

	/**
	 * 返回网站与广告组合后的跟踪代码唯一性ID，该集合与调用tracker.addPublisherAdvertising方法的顺序对应获取
	 * ！！！！！！！必须在获取代码后更新关系表中的trackcode字段
	 * 
	 * @return
	 */
	public String[] getAffilliateTrackCodeIds() {
		int tlen = this.publisher.sites.size();
		Website wcs = null;
		String[] ids = new String[tlen];
		for (int i = 0; i < tlen; i++) {
			wcs = this.publisher.sites.get(i);
			ids[i] = wcs.getTrackCodeId();
		}
		return ids;
	}
}
