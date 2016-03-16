package cn.clickvalue.cv2.tracking.configs;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.tracking.configs.entities.Advertise;
import cn.clickvalue.cv2.tracking.configs.entities.Banner;
import cn.clickvalue.cv2.tracking.configs.entities.Campaign;
import cn.clickvalue.cv2.tracking.configs.entities.LandingPage;
import cn.clickvalue.cv2.tracking.configs.loader.LoadManager;

import com.darwinmarketing.TrackLogger;
import com.darwinmarketing.configs.ConfigReader;

public class Generater {

	public void excute() {
		String rootPath = ConfigReader.getString("app.cv2.campaign-config.static-path.publish-root");
		String templatePath = ConfigReader.getString("app.cv2.campaign-config.static-path.template-path");
		String trustTemplatePath = ConfigReader.getString("app.cv2.campaign-config.static-path.trust-template-path");

		final Pattern campaign_js_regex = Pattern.compile("\\$\\{campaign_js\\}");
		final Pattern player_js_regex = Pattern.compile("\\$\\{player_js\\}");
		final Pattern trust_js_regex = Pattern.compile("\\$\\{trust_js\\}");
		String file = "";
		String trustFile = "";
		String campaign_js = "";
		String player_js = "";
		StringBuilder trust_js = new StringBuilder();
		try {
			file = FileUtils.readFileToString(new File(templatePath), "utf-8");
			trustFile = FileUtils.readFileToString(new File(trustTemplatePath), "utf-8");
		} catch (IOException e) {
			TrackLogger.error("Can't read template file:", e);
		}
		try {
			if (StringUtils.isBlank(file) || StringUtils.isBlank(trustFile)) {} else {
				LoadManager loadManager = new LoadManager();
				List<Campaign> allCampaign = loadManager.getAllCampaign();
				for (Campaign campaign : allCampaign) {
					campaign_js = getCampaignJs(campaign);
					player_js = getPlayerJs(campaign);
					String resultFile = file;
					Matcher mc = campaign_js_regex.matcher(resultFile);
					resultFile = mc.replaceAll(campaign_js);
					Matcher mp = player_js_regex.matcher(resultFile);
					resultFile = mp.replaceAll(player_js);
					try {
						FileUtils.writeStringToFile(new File(getDirForCampaign(rootPath, campaign) + "C" + campaign.getId() + ".html"), resultFile, "utf-8");
					} catch (IOException e) {
						TrackLogger.error("Failed to generate campaign files:", e);
					}
					if (campaign.getIntrust() != null && campaign.getIntrust() == 1) {
						buildTrustJs(trust_js, campaign);
					}
				}
				Matcher mt = trust_js_regex.matcher(trustFile);
				trustFile = mt.replaceAll(trust_js.toString());
				try {
					FileUtils.writeStringToFile(new File(rootPath + "dwap.html"), trustFile, "utf-8");
				} catch (IOException e) {
					TrackLogger.error("Failed to generate trust file:", e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO 处理消息日志，发邮件给监控人员
	}

	/**
	 * @param campaign
	 * @return 创建campaign的js
	 */
	private String getCampaignJs(Campaign campaign) {

		Date today = DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.ISO_EXPANDED_DATE_FORMAT));

		StringBuffer sbf = new StringBuffer();
		sbf.append("<script>\r\n");
		boolean passed = false;

		// 为了admin测试广告活动，所以放开了Verified=1的限制条件，以及时间的限制
		if (campaign.getEndDate().before(today) || campaign.getVerified() == 0) {
			passed = false;
		} else {
			passed = true;
		}
		sbf.append("var passed = ");
		sbf.append(passed);
		sbf.append(";\r\n");

		sbf.append(getRefusedSitesJs(campaign));
		sbf.append("\r\n");

		// rejected site id list

		sbf.append("var cp = new Campaign(");
		sbf.append(campaign.getId()).append(",");
		sbf.append("\"").append(campaign.getName()).append("\"").append(",");
		sbf.append("passed);\r\n");
		for (Advertise advertise : campaign.getAdvertises()) {
			sbf.append("cp.addAd(");
			sbf.append(getAdvertiseJs(advertise));
			sbf.append(");\r\n");
		}
		sbf.append("</script>");
		return sbf.toString();
	}

	private void buildTrustJs(StringBuilder sb, Campaign campaign) {
		Date current = new Date();
		if (current.before(DateUtil.dateIncreaseByDay(campaign.getEndDate(), 1)) && current.after(campaign.getStartDate())) {
			sb.append("pos = cps.length;\r\n");
			sb.append(String.format("cps[pos] = new Campaign(%d,\"%s\",true,%d);\r\n", campaign.getId(), campaign.getName(), campaign.getSemId()));
			sb.append(String.format("categories.put(%d,['%s']);\r\n", campaign.getId(), campaign.getCategories().replaceAll(",", "','")));
			for (Advertise advertise : campaign.getAdvertises()) {
				sb.append("cps[pos].addAd(");
				sb.append(getAdvertiseJs(advertise));
				sb.append(");\r\n");
			}
			sb.append("\r\n");
		}
	}

	private String getRefusedSitesJs(Campaign campaign) {
		StringBuffer sbf = new StringBuffer();
		List<Integer> refusedSiteIds = campaign.getRefusedSiteIds();
		sbf.append("var siteIds = [");
		if (refusedSiteIds != null) {
			for (int i = 0; i < refusedSiteIds.size(); i++) {
				if (i != 0) {
					sbf.append(", ");
				}
				sbf.append(refusedSiteIds.get(i));
			}
		}
		sbf.append("];\r\n");
		sbf.append("var cSiteId = Player._rsPP[\"_cv_code.4\"];\r\n");
		sbf.append("for(var p=0;p<siteIds.length;p++){\r\n");
		sbf.append("\tif(siteIds[p] == cSiteId){\r\n");
		sbf.append("\t\t passed = false;\r\n");
		sbf.append("\t\t break;\r\n");
		sbf.append("\t}\r\n");
		sbf.append("}\r\n");
		return sbf.toString();
	}

	/**
	 * @param advertise
	 * @return 创建广告的js
	 */
	private String getAdvertiseJs(Advertise advertise) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("new Ad(");
		sbf.append(advertise.getId());
		sbf.append(getBannerJs(advertise.getBanner()));
		sbf.append(getLandingPageJs(advertise.getLandingPage()));
		sbf.append(")");
		return sbf.toString();
	}

	/**
	 * @param banner
	 * @return 创建banner的js
	 */
	private String getBannerJs(Banner banner) {
		if (banner == null) {
			return "";
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(",new Banner(");
		sbf.append(banner.getId()).append(",");
		sbf.append("\"").append(banner.getName()).append("\"").append(",");
		sbf.append(banner.getBannerType()).append(",");
		// 替换"和换行符
		String content = banner.getContent();
		if ("1".equals(banner.getBannerType())) {
			content = content.replaceAll("\"", "\\\\\\\\\"");
			content = content.replaceAll("\n\r", "<br />");
			content = content.replaceAll("\r\n", "<br />");
			content = content.replaceAll("\n", "<br />");
			content = content.replaceAll("\r", "<br />");
		} else if ("3".equals(banner.getBannerType())) {
			content = content.replaceAll("\"", "\\\\\\\\\"");
			content = content.replaceAll("\n\r", "");
			content = content.replaceAll("\r\n", "");
			content = content.replaceAll("\n", "");
			content = content.replaceAll("\r", "");
		}

		sbf.append("\"").append(content).append("\"").append(",");
		sbf.append(banner.getWidth()).append(",");
		sbf.append(banner.getHeight());
		sbf.append(")");
		return sbf.toString();
	}

	/**
	 * @param landingPage
	 * @return 新建landdingPage的js
	 */
	private String getLandingPageJs(LandingPage landingPage) {
		if (landingPage == null) {
			return "";
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(",new Landing(");
		sbf.append(landingPage.getId()).append(",");
		sbf.append("\"").append(landingPage.getName()).append("\"").append(",");
		sbf.append("\"").append(landingPage.getUrl()).append("\"");
		sbf.append(")");
		return sbf.toString();
	}

	/**
	 * @param campaign
	 * @return PlayerJs campaign的parameters的结构为：key|value,key|value,key|value......
	 */
	private String getPlayerJs(Campaign campaign) {
		String paramstr = campaign.getParameters();
		paramstr = (paramstr == null) ? "" : paramstr;
		String[] params = StringUtils.split(paramstr, ',');
		StringBuffer pb = new StringBuffer("");
		String[] vals = null;
		for (int i = 0; i < params.length; i++) {
			vals = StringUtils.split(params[i], '|');
			if (vals.length == 2) {
				pb.append(",[\"");
				pb.append(vals[0]);
				pb.append("\",\"");
				pb.append(vals[1]);
				pb.append("\"]");
			}
		}
		String res = pb.toString();
		return res;
	}

	/**
	 * @param rootPath
	 * @param campaign
	 * @return 路径规则为：rootPath/cpd/campaignId%100/
	 */
	private String getDirForCampaign(String rootPath, Campaign campaign) {
		String dir = rootPath.concat("cpd/");
		dir = dir.concat(String.valueOf(campaign.getId() % 100)).concat("/");
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}

	public static void main(String[] args) throws IOException {
		Generater generater = new Generater();
		generater.excute();
	}
}
