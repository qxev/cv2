package cn.clickvalue.cv2.services.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.Messages;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.FileUtils;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class BannerService extends BaseService<Banner> {

	/**
	 * 初始化 Banner 对象
	 * 
	 * @return Banner
	 */
	public Banner createBanner() {
		Banner banner = new Banner();
		banner.setActived(new Integer(0));
		banner.setVerified(new Integer(0));
		banner.setDeleted(Integer.valueOf(0));
		return banner;
	}

	/**
	 * 生成 Campaign下的图片的文件夹目录
	 * 
	 * @param campaign
	 * @return String
	 */
	public String destFolder(Campaign campaign) {
		StringBuffer sb = new StringBuffer("c");
		sb.append(campaign.getId().toString());
		String destFolder = sb.toString();
		return destFolder;
	}

	public String getBannerType(Banner banner, Messages message) {
		return Constants.formatBannerType(banner, message);
	}

	public void createIframeFile(Campaign campaign, Banner banner)
			throws IOException {
		StringBuffer fileName = new StringBuffer();
		fileName.append(campaign.getId().toString());
		fileName.append("_");
		fileName.append(banner.getId().toString());
		FileUtils.createHtmlFile(destFolder(campaign), banner.getContent(),
				fileName.toString());
	}

	public List<Banner> findByCampaignIdAndVerified(int campaignId, int verified) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("deleted", 0);
		conditions.put("verified", verified);
		c.setCondition(conditions);
		List<Banner> banners = find(c);
		return banners;
	}

	public List<Banner> findByCampaignId(int campaignId) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("deleted", 0);
		c.setCondition(conditions);
		List<Banner> banners = find(c);
		return banners;
	}

	public boolean hasBanner(int bannerId, int campaignId, int deleted,
			int verified) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("id", bannerId);
		conditions.put("deleted", 0);
		conditions.put("verified", verified);
		c.setCondition(conditions);
		return count(c) > 0 ? true : false;
	}

	public Banner getBanner(int bannerId, int campaignId, int deleted,
			int verified) {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("id", bannerId);
		conditions.put("deleted", 0);
		conditions.put("verified", verified);
		query.setCondition(conditions);
		return findUniqueBy(query);
	}

}
