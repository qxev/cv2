package cn.clickvalue.cv2.common.constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.City;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.Province;
import cn.clickvalue.cv2.services.LabelValueModel;

public class Constants {

	public static final Integer PAGESIZE = 2000;

	public static final String IMAGE_DEF_PATH = "/public/uploads/def.gif";

	/**
	 * 用户组 广告主
	 */
	public static final String USER_GROUP_ADVERTISER = "advertiser";

	/**
	 * 用户组 网站主
	 */
	public static final String USER_GROUP_AFFILIATE = "affiliate";

	/** 審核狀態 */
	public static final String AUDIT[] = { "未申请", "待审核", "已通过", "已拒绝" };

	public static final String CAMPAIGNVERIFIED[] = { "未提交", "待审核", "已批准",
			"已拒绝", "下线申请中" };

	public static final String PAIDSTATE[] = { "否", "是" };

	/** 廣告類型 */
	public static final String BANNERTYPE[] = { "图片", "文字", "FLASH", "HTML", "IFRAME" };

	/** 省 */
	public static List<Province> PROVINCES = new ArrayList<Province>();

	/** 城市 */
	private static List<City> cities = new ArrayList<City>();

	public static String getPAIDSTATE(Messages messages, Integer index) {
		return PAIDSTATE[index];
	}

	/**
	 * 生成的excel的文件名
	 */
	public static final String DAILY_SUMMARY_EXCEL_NAME = "affiliate_daily_summary";
	public static final String WEEKLY_SUMMARY_EXCEL_NAME = "affiliate_weekly_summary";

	/**
	 * 日汇总
	 */
	public static final String DATE_SUMMARY = "date";

	/**
	 * 周汇总
	 */
	public static final String WEEK_SUMMARY = "week";

	public static final String BBS_URL_1 = "http://bbs.clickvalue.cn/viewthread.php?tid=";
	
	public static final String BBS_URL_2 = "&extra=page%3D1";
	
	public static final String WEBSITE = "http://www.clickvalue.cn";
	
	/**
	 * 廣告類型
	 * 
	 * @param messages
	 * @param index
	 * @return String
	 */
	public static String getBannerType(Integer index) {
		return BANNERTYPE[index];
	}

	/** 关注状态 */
	public static final String CONCERN[] = { "未关注", "已关注" };

	public static final String IMAGE[] = {
			"assets/images/public/ad/icon_joinprocess_notjoined.gif",
			"assets/images/public/ad/icon_joinprocess_appling.gif",
			"assets/images/public/ad/icon_joinprocess_joined.gif",
			"assets/images/public/ad/icon_joinprocess_rejected.gif" };

	public static final String IMAGE_CONCERN[] = {
			"assets/images/public/ad/none_fav.gif",
			"assets/images/public/ad/fav.gif" };

	public static String getImageConcern(boolean index) {
		if (index) {
			return IMAGE_CONCERN[1];
		} else {
			return IMAGE_CONCERN[0];
		}
	}

	public Constants() {
	}

	public static String getImage(Integer index) {
		return IMAGE[index];
	}

	/**
	 * 获取关注状态
	 * 
	 * @param messages
	 * @param index
	 * @return String
	 */
	public static String getConcern(Messages messages, Integer index) {
		return CONCERN[index];
	}

	/**
	 * 广告管理 申请状态
	 * 
	 * @param messages
	 * @param index
	 * @return String
	 */
	public static String getCampignVerified(Messages messages, Integer index) {
		return CAMPAIGNVERIFIED[index];
	}

	public static final String DEFAULT_STATE[] = { "否", "是" };

	public static final String dayFlow[] = { "0 - 100", "101 - 500",
			"501 - 2000", "2001 - 5000", "5001 - 10000", "10001 - 100000",
			"1000000以上" };

	public static String getDEFAULT_STATE(Messages messages, Integer index) {
		return DEFAULT_STATE[index];
	}

	/**
	 * 获取网站人流量
	 * 
	 * @param index
	 * @param messages
	 * @return String
	 */
	public static String getDayFlow(Messages messages, Integer index) {
		if (index == null || index < 0) {
			index = 0;
		} else if (index > 6) {
			index = 6;
		}
		return dayFlow[index];
	}

	/**
	 * 广告主的跳转页
	 */

	public static final String AD_REDIRECT_HOMEPAGE = "advertiser/homepage";
	public static final String AD_REDIRECT_SITE = "advertiser/sitelistpage";
	public static final String AD_REDIRECT_CAMPAIGN = "advertiser/campaignlistpage";
	public static final String AD_REDIRECT_RULE = "advertiser/commisionrulelistpage";
	public static final String AD_REDIRECT_CREATIVE_GROUP = "advertiser/landingpagelist";
	public static final String AD_REDIRECT_BANNER = "advertiser/bannerlistpage";
	public static final String AD_REDIRECT_AFFILIATE = "advertiser/affiliatelistpage";
	public static final String AD_REDIRECT_COMMISIONPAYMENT = "advertiser/commisionpaymentpage";

	/**
	 * 报表筛选条件
	 */
	public static final String REPORT_FILTER_BY_CAMPAIGN = "campaign";
	public static final String REPORT_FILTER_BY_SITE = "site";
	public static final String REPORT_FILTER_BY_BOTH = "both";

	/**
	 * 报表类型
	 */
	// 汇总(按天)
	public static final String REPORT_TYPE_OF_COLLECT_BY_DATE = "collectByDate";
	// 汇总(摘要)
	public static final String REPORT_TYPE_OF_COLLECT_BY_MEMO = "collectByMemo";

	/**
	 * 所有状态常量定义
	 */
	public static final String ALL_STATUS = "10";

	/**
	 * 广告活动申请状态
	 */

	public static final int NOT_SUBMITTED = 0;
	// 上线待审核
	public static final int PENDING_APPROVAL = 1;
	public static final int APPROVED = 2;
	public static final int REFUSED = 3;
	public static final int OFFLINE_PENDING_APPROVAL = 4;

	public static final int CAMPAIGN_ONLINE = 1;
	public static final int CAMPAIGN_OFFLINE = 0;

	/**
	 * 删除状态
	 */

	public static final int NOT_DELETED = 0;
	public static final int DELETED = 1;

	/**
	 * 激活状态
	 */
	public static final int UNACTIVATED = 0;
	public static final int ACTIVATED = 1;

	/**
	 * 佣金类型
	 */

	public static final String CPC = "100";
	public static final String CPL = "101";
	public static final String CPS = "102";
	public static final String CPM = "105";

	public static final String VALUETYPE_VALUE = "1";
	public static final String VALUETYPE_PERCENT = "2";

	/**
	 * 确认方式
	 */
	public static final String ADMIN_CONFIRM = "1";
	public static final String ADVERTISER_CONFIRM = "2";

	/**
	 * 关注状态
	 */

	public static final int CONCERN_STATUS_YES = 1;
	public static final int CONCERN_STATUS_NO = 0;

	/**
	 * 广告类型
	 */
	public static final int BANNER_TYPE_IMAGE = 0;
	public static final int BANNER_TYPE_TEXT = 1;
	public static final int BANNER_TYPE_FLASH = 2;
	public static final int BANNER_TYPE_HTML = 3;
	public static final int BANNER_TYPE_IFRAME = 4;

	/**
	 * 省 English->Chinese
	 */
	private static Map<String, String> enTochkey;

	/**
	 * 省 Chinese->English
	 */
	private static Map<String, String> chToenkey;

	/**
	 * 广告类型的option集合
	 */
	public static List<OptionModel> getBannerTypesOptions() {
		Object[] KV = { BANNER_TYPE_IMAGE, "图片", BANNER_TYPE_TEXT, "文字",
				BANNER_TYPE_FLASH, "FLASH", BANNER_TYPE_HTML, "HTML", BANNER_TYPE_IFRAME, "IFRAME" };
		List<OptionModel> bannerTypes = CollectionFactory.newList();
		OptionModel optionModel;
		for (int i = 0; i < KV.length / 2; i++) {
			optionModel = new OptionModelImpl((String) KV[i * 2 + 1],
					(Integer) KV[i * 2]);
			bannerTypes.add(optionModel);
		}
		return bannerTypes;
	}

	public static List<OptionModelImpl> getAffiliateDay(Messages messages) {
		List<OptionModelImpl> affiliateDayTypes = CollectionFactory.newList();
		affiliateDayTypes.add(new OptionModelImpl("0-100", 0));
		affiliateDayTypes.add(new OptionModelImpl("101-500", 1));
		affiliateDayTypes.add(new OptionModelImpl("501-2000", 2));
		affiliateDayTypes.add(new OptionModelImpl("2001-5000", 3));
		affiliateDayTypes
				.add(new OptionModelImpl("5001-10000", 4));
		affiliateDayTypes.add(new OptionModelImpl("10001-100000",
				5));
		affiliateDayTypes.add(new OptionModelImpl("1000000以上", 6));
		return affiliateDayTypes;
	}

	public static List<LabelValueModel> confirmationMethods = new ArrayList<LabelValueModel>();

	public static List<LabelValueModel> getConfirmationMethods(Messages message) {
		if (confirmationMethods.size() == 0) {
			confirmationMethods.add(new LabelValueModel(message
					.get("admin_confirm"), ADMIN_CONFIRM));
			confirmationMethods.add(new LabelValueModel(message
					.get("advertiser_confirm"), ADVERTISER_CONFIRM));
		}
		return confirmationMethods;
	}

	public static List<LabelValueModel> commissions = new ArrayList<LabelValueModel>();

	public static List<LabelValueModel> getCommissions(Messages message) {
		if (commissions.size() == 0) {
			commissions.add(new LabelValueModel("CPC", CPC));
			commissions.add(new LabelValueModel("CPL", CPL));
			commissions.add(new LabelValueModel("CPS", CPS));
			commissions.add(new LabelValueModel("CPM", CPM));
		}
		return commissions;
	}

	public static List<LabelValueModel> ruleTypes = new ArrayList<LabelValueModel>();

	public static List<LabelValueModel> getRuleTypes(Messages message) {
		if (ruleTypes.size() == 0) {
			ruleTypes.add(new LabelValueModel(message.get("flat"), "1"));
			ruleTypes.add(new LabelValueModel(message.get("percentage"), "2"));
		}
		return ruleTypes;
	}

	/**
	 * 审核网站主方式
	 * 
	 * @param affiliateVerified
	 * @return String
	 */
	public static String getVerifyState(Integer affiliateVerified) {
		if (affiliateVerified == null) {
			return "";
		}
		return affiliateVerified == 0 ? "人工" : "自动";
	}

	public static List<LabelValueModel> applicationStatus = new ArrayList<LabelValueModel>();

	public static List<LabelValueModel> getApplicationStatus(Messages message) {
		applicationStatus = new ArrayList<LabelValueModel>();
		applicationStatus.add(new LabelValueModel(message.get("not_submitted"),
				String.valueOf(NOT_SUBMITTED)));
		applicationStatus.add(new LabelValueModel(message
				.get("pending_approval"), String.valueOf(PENDING_APPROVAL)));
		applicationStatus.add(new LabelValueModel(message.get("approved"),
				String.valueOf(APPROVED)));
		applicationStatus.add(new LabelValueModel(message.get("refused"),
				String.valueOf(REFUSED)));
		applicationStatus.add(new LabelValueModel(message
				.get("offline_pending_approval"), String
				.valueOf(OFFLINE_PENDING_APPROVAL)));
		return applicationStatus;
	}

	/**
	 * @param commissionRule
	 *            :佣金规则
	 * @return String:格式化了的佣金规则 格式化佣金规则
	 */
	public static String formatCommissionRule(CommissionRule commissionRule) {

		StringBuffer str = new StringBuffer();
		String formatType = formatCommissionType(commissionRule);
		String formatValue = formatCommissionValue(commissionRule);
		str.append(formatType);
		str.append(": ");
		str.append(formatValue);
		str.append("<span style=\"margin-left: 10px;\">描述:");
		str.append(commissionRule.getDescription());
		str.append("</span>");

		return str.toString();
	}

	public static String formatCommissionRuleWithDate(
			CommissionRule commissionRule) {

		StringBuffer sbf = new StringBuffer();
		String item = Constants.formatCommissionRule(commissionRule);
		sbf.append(item);

		sbf.append(" ");

		sbf.append(DateUtil.dateToString(commissionRule.getStartDate()));
		sbf.append("--");
		sbf.append(DateUtil.dateToString(commissionRule.getEndDate()));
		return sbf.toString();
	}

	/**
	 * @param actionType
	 *            :佣金规则
	 * @return String:格式化了的佣金类型 格式化佣金类型
	 */
	public static String formatCommissionType(CommissionRule commissionRule) {
		String ruleType = commissionRule.getRuleType().toString();
		if (CPC.equals(ruleType)) {
			return "CPC";
		}
		if (CPL.equals(ruleType)) {
			return "CPL";
		}
		if (CPS.equals(ruleType)) {
			return "CPS";
		}
		if (CPM.equals(ruleType)) {
			return "CPM";
		}
		return "";
	}

	/**
	 * @param commissionValue
	 *            :佣金值
	 * @return String:格式化了的佣金值 格式化佣金值
	 */
	public static String formatCommissionTypeAndValue(
			CommissionRule commissionRule) {
		StringBuffer str = new StringBuffer();
		String formatType = formatCommissionType(commissionRule);
		String formatValue = formatCommissionValue(commissionRule);
		str.append(formatType);
		str.append(": ");
		str.append(formatValue);
		return str.toString();
	}

	/**
	 * @param commissionValue
	 *            :佣金值
	 * @return String:格式化了的佣金值 格式化佣金值
	 */
	public static String formatCommissionValue(CommissionRule commissionRule) {
		Integer commissionType = commissionRule.getCommissionType();
		String commissionValue = commissionRule.getCommissionValue().toString();
		if (VALUETYPE_PERCENT.equals(commissionType.toString())) {
			return commissionValue.concat("%");
		} else {
			return "￥".concat(commissionValue);
		}
	}

	/**
	 * @param commissionRule
	 * @return String: 格式化了的darwin的佣金值
	 */
	public static String formatDarwinCommissionValue(
			CommissionRule commissionRule) {
		Integer commissionType = commissionRule.getCommissionType();
		String commissionValue = commissionRule.getDarwinCommissionValue()
				.toString();
		if (VALUETYPE_PERCENT.equals(commissionType.toString())) {
			return commissionValue.concat("%");
		} else {
			return "￥".concat(commissionValue);
		}
	}

	public static String formatBannerType(Banner banner, Messages message) {
		Integer bannerType = Integer.parseInt(banner.getBannerType());
		String str = "";
		switch (bannerType) {
		case BANNER_TYPE_IMAGE:
			str = message.get("image_ad");
			break;
		case BANNER_TYPE_TEXT:
			str = message.get("text_ad");
			break;
		case BANNER_TYPE_FLASH:
			str = message.get("flash_ad");
			break;
		case BANNER_TYPE_HTML:
			str = message.get("html_ad");
			break;
		case BANNER_TYPE_IFRAME:
			str = message.get("iframe_ad");
			break;
		}
		return str;
	}

	/**
	 * 审批状态
	 * 
	 * @param message
	 * @param index
	 * @return String
	 */
	public static String getVerifiedState(Messages message, Integer index) {
		return AUDIT[index];
	}

	/**
	 * @param message
	 * @param verified
	 *            申请状态
	 * @return 格式化申请状态
	 */
	public static String formatVerified(Messages message, Integer verified) {

		String str = "";
		switch (verified) {
		case NOT_SUBMITTED:
			str = message.get("not_submitted");
			break;
		case PENDING_APPROVAL:
			str = message.get("pending_approval");
			break;
		case APPROVED:
			str = message.get("approved");
			break;
		case REFUSED:
			str = message.get("refused");
			break;
		case OFFLINE_PENDING_APPROVAL:
			str = message.get("offline_pending_approval");
			break;
		}

		return str;
	}

	public static String formatAffiliateVerified(Integer verified) {
		String str = "";
		switch (verified) {
		case 0:
			str = "人工";
			break;
		case 1:
			str = "自动";
			break;
		}
		return str;
	}

	/**
	 * @param campaign
	 * @return boolean 根据广告活动的verified和起始日期判断campaign是否上线
	 */
	public static boolean isOnline(Campaign campaign) {
		int verified = campaign.getVerified();

		Date now = DateUtil.getCurrentDateTime();
		Date startDate = campaign.getStartDate();
		Date endDate = campaign.getEndDate();

		if (verified == APPROVED) {
			if (now.after(startDate)
					&& now.before(DateUtil.dateIncreaseByDay(endDate, 1))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 广告活动线上状态
	 * 
	 */
	public static String formatPublishStatus(Messages message, Campaign campaign) {
		boolean isOnline = isOnline(campaign);
		if (isOnline) {
			return (message.get("campaign_online"));
		} else {
			return (message.get("campaign_offline"));
		}
	}

	/**
	 * @param message
	 * @param delete
	 * @return 格式化删除状态
	 */
	public static String formatDeleteStatus(Messages message, Integer delete) {
		if (delete == 0) {
			return "未删除";
		} else {
			return "已删除";
		}
	}

	/**
	 * @param message
	 * @param actived
	 * @return 格式化激活状态
	 */
	public static String formatActivedStatus(Messages message, Integer actived) {
		if (actived == 0) {
			return "未激活";
		} else {
			return "已激活";
		}
	}

	public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		return str.replaceAll("[(/>)<]", "");
	}

	public static String chToen(String key) {
		if (key==null)
			return "";
		return getChToenkey().get(key);
	}

	public static String enToch(String key) {
		if (key==null)
			return "";
		return getEnTochkey().get(key);
	}

	/**
	 * 省 Chinese -> English
	 * 
	 * @return
	 */
	public static Map<String, String> getChToenkey() {
		if (chToenkey == null) {
			chToenkey = new HashMap<String,String>();
			chToenkey.put("北京市", "Beijing");
			chToenkey.put("上海市", "Shanghai");
			chToenkey.put("天津市", "Tianjin");
			chToenkey.put("重庆市", "Chongqing");
			chToenkey.put("广东省", "Guangdong");
			chToenkey.put("福建省", "Fujian");
			chToenkey.put("浙江省", "Zhejiang");
			chToenkey.put("江苏省", "Jiangsu");
			chToenkey.put("山东省", "Shandong");
			chToenkey.put("辽宁省", "Liaoning");
			chToenkey.put("江西省", "Jiangxi");
			chToenkey.put("四川省", "Sichuan");
			chToenkey.put("陕西省", "Shanxi");
			chToenkey.put("湖北省", "Hubei");
			chToenkey.put("河南省", "Henan");
			chToenkey.put("河北省", "Hebei");
			chToenkey.put("山西省", "Shaanxi");
			chToenkey.put("内蒙古", "Inner Mongolia");
			chToenkey.put("吉林省", "Jilin");
			chToenkey.put("黑龙江", "Heilongjiang");
			chToenkey.put("安徽省", "Anhui");
			chToenkey.put("湖南省", "Hunan");
			chToenkey.put("广西区", "Guangxi");
			chToenkey.put("海南省", "Hainan");
			chToenkey.put("云南省", "Yunnan");
			chToenkey.put("贵州省", "Guizhou");
			chToenkey.put("西藏区", "Tibet");
			chToenkey.put("甘肃省", "Gansu");
			chToenkey.put("宁夏区", "Ningxia");
			chToenkey.put("青海省", "Qinghai");
			chToenkey.put("新疆区", "Xinjiang");
			chToenkey.put("香港区", "Hongkong");
			chToenkey.put("澳门区", "Macao");
			chToenkey.put("台湾省", "Taiwan");
			chToenkey.put("所有地区", "All Region");
		}
		return chToenkey;
	}

	/**
	 * 省 English -> Chinese
	 * 
	 * @return
	 */
	public static Map<String, String> getEnTochkey() {
		if (enTochkey == null) {
			enTochkey = new HashMap<String, String>();
			enTochkey.put("Beijing", "北京市");
			enTochkey.put("Shanghai", "上海市");
			enTochkey.put("Tianjin", "天津市");
			enTochkey.put("Chongqing", "重庆市");
			enTochkey.put("Guangdong", "广东省");
			enTochkey.put("Fujian", "福建省");
			enTochkey.put("Zhejiang", "浙江省");
			enTochkey.put("Jiangsu", "江苏省");
			enTochkey.put("Shandong", "山东省");
			enTochkey.put("Liaoning", "辽宁省");
			enTochkey.put("Jiangxi", "江西省");
			enTochkey.put("Sichuan", "四川省");
			enTochkey.put("Shanxi", "陕西省");
			enTochkey.put("Hubei", "湖北省");
			enTochkey.put("Henan", "河南省");
			enTochkey.put("Hebei", "河北省");
			enTochkey.put("Shaanxi", "山西省");
			enTochkey.put("Inner Mongolia", "内蒙古");
			enTochkey.put("Jilin", "吉林省");
			enTochkey.put("Heilongjiang", "黑龙江");
			enTochkey.put("Anhui", "安徽省");
			enTochkey.put("Hunan", "湖南省");
			enTochkey.put("Guangxi", "广西区");
			enTochkey.put("Hainan", "海南省");
			enTochkey.put("Yunnan", "云南省");
			enTochkey.put("Guizhou", "贵州省");
			enTochkey.put("Tibet", "西藏区");
			enTochkey.put("Gansu", "甘肃省");
			enTochkey.put("Ningxia", "宁夏区");
			enTochkey.put("Qinghai", "青海省");
			enTochkey.put("Xinjiang", "新疆区");
			enTochkey.put("Hongkong", "香港区");
			enTochkey.put("Macao", "澳门区");
			enTochkey.put("Taiwan", "台湾省");
			enTochkey.put("All Region", "所有地区");
		}
		return enTochkey;
	}

	public static Set<String> enTochRegion(Set<String> selectedRegions) {
		List<String> regions = new ArrayList<String>();
		for (String region : selectedRegions) {
			regions.add(enToch(region));
		}
		selectedRegions = new HashSet<String>();
		for (String region : regions) {
			if (region != null)
				selectedRegions.add(region);
		}
		return selectedRegions;
	}

	public static Set<String> chToenRegion(Set<String> selectedRegions) {
		List<String> regions = new ArrayList<String>();
		for (String region : selectedRegions) {
			regions.add(chToen(region));
		}
		selectedRegions = new HashSet<String>();
		for (String region : regions) {
			if (region != null)
				selectedRegions.add(region);
		}
		return selectedRegions;
	}

	public static String formatRegion(String region, String local) {
		if ("day".equals(local)) {
			String[] regions = region.split(";");
			StringBuffer resultRegion = new StringBuffer();
			boolean flag = false;
			for (int i = 0; i < regions.length; i++) {
				if (!flag) {
					resultRegion.append(Constants.chToen(regions[i]));
					flag = true;
				} else {
					resultRegion.append(";").append(
							Constants.chToen(regions[i]));
				}
			}
			return resultRegion.toString();
		} else {
			return region;
		}
	}
}