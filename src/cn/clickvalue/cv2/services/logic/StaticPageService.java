package cn.clickvalue.cv2.services.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import cn.clickvalue.cv2.common.Enum.HPBlockEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignZhuanti;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.velocity.MsgBean;

public class StaticPageService {

	private MsgBean msgBean;

	private CampaignService campaignService;

	private BulletinService bulletinService;

	private HPBlockService hpBlockService;

	private static final String CURR_CLASS_PATH;

	static {
		CURR_CLASS_PATH = "/".concat(StaticPageService.class.getPackage().getName().replace(".", "/")).concat("/");
	}

	public String buildHomePage() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("campaigns", getRecommendCampaignModel());
		model.put("newestCampaigns", getNewestCampaignModel());
		model.put("sysnews", getSysnewsModel());
		model.put("campnews", getCampnewsModel());
		model.put("seos", getSeoModel());
		model.put("exchanges", getExchangeModel());
		model.put("designs", getDesignModel());
		model.put("placards", getPlacardModel());
		model.put("links", getHomeLinks());
		msgBean.setTemplateLocation(copyFileToCurrentClassPath("/public/index/indexTemplate.html", "indexTemplate.vm"));
		msgBean.setModel(model);
		try {
			FileUtils.writeStringToFile(new File(RealPath.getRealPath("/index.html")), msgBean.getMsg(), "utf-8");
			FileUtils.writeStringToFile(new File(RealPath.getRealPath("/index1.html")), msgBean.getMsg(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgBean.getMsg();
	}

	public String buildCarousel() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("banners", getCarouselModel());
		msgBean.setTemplateLocation(copyFileToCurrentClassPath("/public/index/viewerData.xml", "viewerData.vm"));
		msgBean.setModel(model);
		try {
			FileUtils
					.writeStringToFile(new File(RealPath.getRealPath("/public/carousel/other/viewerData.xml")), msgBean.getMsg(), "gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgBean.getMsg();
	}

	public String buildZhuanti(CampaignZhuanti zhuanti) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("zhuanti", zhuanti);
		model.put("startDate", DateUtil.dateToString(zhuanti.getCampaign().getStartDate()));
		model.put("endDate", DateUtil.dateToString(zhuanti.getCampaign().getEndDate()));
		msgBean.setTemplateLocation(copyFileToCurrentClassPath("/public/zhuanti/other/zhuantiTemplate.html", "zhuantiTemplate.vm"));
		msgBean.setModel(model);
		String path = "";
		try {
			path = String.format("/public/zhuanti/%s.html", String.valueOf(zhuanti.getCampaign().getId()));
			File file = new File(RealPath.getRealPath(path));
			FileUtils.writeStringToFile(file, msgBean.getMsg(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public void buildLinks() {
		Map<String, Object> model = new HashMap<String, Object>();
		HPBlock innerlink = hpBlockService.findUniqueBy("name", HPBlockEnum.INNER_LINKS.name());
		HPBlock imagelink = hpBlockService.findUniqueBy("name", HPBlockEnum.INNER_BANNER_LINKS.name());
		model.put("innerlinks", innerlink.getHpBlockContents());
		model.put("imagelinks", imagelink.getHpBlockContents());
		msgBean.setTemplateLocation(copyFileToCurrentClassPath("/public/link/other/indexTemplate.html", "linkTemplate.vm"));
		msgBean.setModel(model);
		try {
			File file = new File(RealPath.getRealPath("/public/link/index.html"));
			FileUtils.writeStringToFile(file, msgBean.getMsg(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, String>> getRecommendCampaignModel() {
		HPBlock hpBlock = hpBlockService.findUniqueBy("name", HPBlockEnum.RECOMMEND_CAMPAIGNS.name());
		List<HPBlockContent> hpBlockContents = hpBlock.getHpBlockContents();
		List<Map<String, String>> caps = new ArrayList<Map<String, String>>();
		for (HPBlockContent hpBlockContent : hpBlockContents) {
			Campaign campaign = campaignService.get(hpBlockContent.getEntityId());
			String ruleDisplay = "";
			if (campaign != null && campaign.getCommissionRules() != null && campaign.getCommissionRules().size() > 0) {
				List<CommissionRule> commissionRules = campaign.getCommissionRules();
				Date current = new Date();
				for (CommissionRule cr : commissionRules) {
					if (cr.getStartDate().before(current) && cr.getEndDate().after(current)) {
						ruleDisplay = Constants.formatCommissionTypeAndValue(cr);
						break;
					}
				}
				if (StringUtils.isEmpty(ruleDisplay)) {
					ruleDisplay = Constants.formatCommissionTypeAndValue(commissionRules.get(commissionRules.size() - 1));
				}

				String zhuanti = "/public/zhuanti/".concat(String.valueOf(campaign.getId())).concat(".html");
				if (StringUtils.isNotEmpty(hpBlockContent.getUrl())) {
					zhuanti = hpBlockContent.getUrl();
				}

				String simpleName = campaign.getName();
				if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleName) > 24) {
					simpleName = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleName, 20).concat(". . .");
				}

				Map<String, String> cap = new HashMap<String, String>();
				cap.put("name", campaign.getName());
				cap.put("simpleName", simpleName);
				cap.put("logo", campaign.getSite().getLogo());
				cap.put("no_logo", campaign.getSite().getName());
				cap.put("cpa", ruleDisplay);
				cap.put("startDate", DateUtil.dateToString(campaign.getStartDate()));
				cap.put("endDate", DateUtil.dateToString(campaign.getEndDate()));
				cap.put("zhuanti", zhuanti);
				caps.add(cap);
			}
		}
		return caps;
	}

	public List<Map<String, String>> getNewestCampaignModel() {
		List<Campaign> campaigns = campaignService.findNewestCampaigns();
		List<Map<String, String>> caps = new ArrayList<Map<String, String>>();
		for (Campaign campaign : campaigns) {
			String ruleDisplay = "";
			if (campaign.getCommissionRules() != null && campaign.getCommissionRules().size() > 0) {
				CommissionRule rule = campaign.getCommissionRules().get(0);
				ruleDisplay = Constants.formatCommissionTypeAndValue(rule);
			}
			String simpleName = campaign.getName();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleName) > 24) {
				simpleName = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleName, 20).concat(". . .");
			}
			Map<String, String> cap = new HashMap<String, String>();
			cap.put("name", campaign.getName());
			cap.put("simpleName", simpleName);
			cap.put("logo", campaign.getSite().getLogo());
			cap.put("no_logo", campaign.getSite().getName());
			cap.put("cpa", ruleDisplay);
			cap.put("startDate", DateUtil.dateToString(campaign.getStartDate()));
			cap.put("endDate", DateUtil.dateToString(campaign.getEndDate()));
			cap.put("zhuanti", "/public/zhuanti/".concat(String.valueOf(campaign.getId())).concat(".html"));
			caps.add(cap);
		}
		return caps;
	}

	public List<Map<String, String>> getSysnewsModel() {
		List<Bulletin> bulletins = bulletinService.findSysnews(8);
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		for (Bulletin bulletin : bulletins) {
			Date date = bulletin.getUpdatedAt();
			String simpleSubject = bulletin.getSubject();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 22) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 18).concat(". . .");
			}
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("date_y", DateUtil.dateToString(date, "yyyy"));
			mode.put("date_m", DateUtil.dateToString(date, "MM"));
			mode.put("date_d", DateUtil.dateToString(date, "dd"));
			mode.put("subject", bulletin.getSubject());
			mode.put("simpleSubject", simpleSubject);
			mode.put("id", String.valueOf(bulletin.getId()));
			results.add(mode);
		}
		return results;
	}

	private Object getCampnewsModel() {
		List<Bulletin> bulletins = bulletinService.findCampnews(8);
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		for (Bulletin bulletin : bulletins) {
			Date date = bulletin.getUpdatedAt();
			String simpleSubject = bulletin.getSubject();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 22) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 18).concat(". . .");
			}
			String tag = "";
			String color = "";
			switch (bulletin.getTag()) {
			case 1:
				tag = "上线";
				color = "#6EAA24";
				break;
			case 2:
				tag = "下线";
				color = "gray";
				break;
			case 3:
				tag = "促销";
				color = "#FF7F00";
				break;
			case 4:
				tag = "调整";
				color = "black";
				break;

			default:
				tag = "";
				color = "";
				break;
			}

			Map<String, String> mode = new HashMap<String, String>();
			mode.put("date_y", DateUtil.dateToString(date, "yyyy"));
			mode.put("date_m", DateUtil.dateToString(date, "MM"));
			mode.put("date_d", DateUtil.dateToString(date, "dd"));
			mode.put("tag", tag);
			mode.put("color", color);
			mode.put("subject", bulletin.getSubject());
			mode.put("simpleSubject", simpleSubject);
			mode.put("id", String.valueOf(bulletin.getId()));
			results.add(mode);
		}
		return results;
	}

	public List<Map<String, String>> getSeoModel() {
		HPBlock seo = hpBlockService.findUniqueBy("name", HPBlockEnum.SEO.name());

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<HPBlockContent> contents = seo.getHpBlockContents();
		for (HPBlockContent content : contents) {
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("url", content.getUrl());
			String simpleSubject = content.getCdata();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 30) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 26).concat(". . .");
			}
			mode.put("simpleSubject", simpleSubject);
			mode.put("subject", content.getCdata());
			results.add(mode);
		}

		return results;
	}

	public List<Map<String, String>> getExchangeModel() {
		HPBlock seo = hpBlockService.findUniqueBy("name", HPBlockEnum.EXCHANGE.name());

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<HPBlockContent> contents = seo.getHpBlockContents();
		for (HPBlockContent content : contents) {
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("url", content.getUrl());
			String simpleSubject = content.getCdata();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 30) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 26).concat(". . .");
			}
			mode.put("simpleSubject", simpleSubject);
			mode.put("subject", content.getCdata());
			results.add(mode);
		}

		return results;
	}

	public List<Map<String, String>> getDesignModel() {
		HPBlock seo = hpBlockService.findUniqueBy("name", HPBlockEnum.WEBSITE_DESIGN.name());

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<HPBlockContent> contents = seo.getHpBlockContents();
		for (HPBlockContent content : contents) {
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("url", content.getUrl());
			String simpleSubject = content.getCdata();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 30) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 26).concat(". . .");
			}
			mode.put("simpleSubject", simpleSubject);
			mode.put("subject", content.getCdata());
			results.add(mode);
		}

		return results;
	}

	public List<Map<String, String>> getPlacardModel() {
		HPBlock seo = hpBlockService.findUniqueBy("name", HPBlockEnum.HOT_PLACARD.name());

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<HPBlockContent> contents = seo.getHpBlockContents();
		for (HPBlockContent content : contents) {
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("url", content.getUrl());
			String simpleSubject = content.getCdata();
			if (cn.clickvalue.cv2.common.util.StringUtils.getSpecialLength(simpleSubject) > 30) {
				simpleSubject = cn.clickvalue.cv2.common.util.StringUtils.specialTrim(simpleSubject, 26).concat(". . .");
			}
			mode.put("simpleSubject", simpleSubject);
			mode.put("subject", content.getCdata());
			results.add(mode);
		}

		return results;
	}

	private Object getHomeLinks() {
		HPBlock link = hpBlockService.findUniqueBy("name", HPBlockEnum.HOME_LINKS.name());
		return link.getHpBlockContents();
	}

	public List<Map<String, String>> getCarouselModel() {
		HPBlock banners = hpBlockService.findUniqueBy("name", HPBlockEnum.BANNER.name());

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<HPBlockContent> contents = banners.getHpBlockContents();
		for (HPBlockContent content : contents) {
			Map<String, String> mode = new HashMap<String, String>();
			mode.put("url", content.getUrl());
			mode.put("img", content.getImage());
			mode.put("desc", content.getCdata());
			results.add(mode);
		}
		return results;
	}

	private String copyFileToCurrentClassPath(String source, String fileName) {
		String templateClassPath = CURR_CLASS_PATH.concat(fileName);
		File template = new File(RealPath.getRealPath("classpath:".concat(templateClassPath)));
		File sourceFile = new File(RealPath.getRealPath(source));
		try {
			FileUtils.copyFile(sourceFile, template);
		} catch (IOException e) {
		}
		return templateClassPath;
	}

	public void setMsgBean(MsgBean msgBean) {
		this.msgBean = msgBean;
	}

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public void setBulletinService(BulletinService bulletinService) {
		this.bulletinService = bulletinService;
	}

	public void setHpBlockService(HPBlockService hpBlockService) {
		this.hpBlockService = hpBlockService;
	}
}
