package cn.clickvalue.cv2.tracking.configs.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.entities.Advertise;
import cn.clickvalue.cv2.tracking.configs.entities.Banner;
import cn.clickvalue.cv2.tracking.configs.entities.Campaign;
import cn.clickvalue.cv2.tracking.configs.entities.LandingPage;

import com.darwinmarketing.TrackLogger;

/**
 * @author larry.lang 查找所有campaign，并把关联的Advertise，banner，landingPage设置在campaign中
 */
public class LoadManager {

	public List<Campaign> getAllCampaign() {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		try {
			campaigns = CampaignLoader.loadAllCampaign();
			Map<Integer, List<Advertise>> advertises = getAllAdvertise();
			Map<Integer, String> categories = AffiliateCategoryCampaignLoader.getAllAffiliateCategoryByCampaign();
			Map<Integer, Integer> semIds = SemClientLoader.getSemIdByTrustCampaign();
			Map<Integer, List<Integer>> refusedSiteIdsForCampaign = CampaignSiteLoader.getAllRefusedSiteIdsForCampaign();
			for (Campaign campaign : campaigns) {
				List<Advertise> list = advertises.get(campaign.getId());
				if (list != null) {
					campaign.setAdvertises(list);
				}
				List<Integer> list2 = refusedSiteIdsForCampaign.get(campaign.getId());
				if (list != null) {
					campaign.setRefusedSiteIds(list2);
				}
				if (campaign.getIntrust() != null && campaign.getIntrust() == 1) {
					campaign.setCategories(categories.get(campaign.getId()));
					campaign.setSemId(semIds.get(campaign.getId()));
				}
			}
		} catch (Exception e) {
			TrackLogger.error("Failed to load Campaign", e);
		}
		return campaigns;
	}

	/**
	 * @return Map<CampaignId,该所有campaignId==Key的Advertise的集合> 查找所有Advertise，并设置其关联的banner，landingPage
	 */
	private Map<Integer, List<Advertise>> getAllAdvertise() {
		Map<Integer, List<Advertise>> map = new HashMap<Integer, List<Advertise>>();
		try {
			Map<Integer, Banner> banners = BannerLoader.loadAllBanners();
			Map<Integer, LandingPage> landingPages = LandingPageLoader.loadAllLandingPage();
			List<Advertise> advertises = AdvertiseLoader.loadAllAdvertise();
			for (Advertise advertise : advertises) {
				advertise.setBanner(banners.get(advertise.getBannerId()));
				advertise.setLandingPage(landingPages.get(advertise.getLandingPageId()));
				Integer campaignId = advertise.getCampaignId();
				if (!map.containsKey(campaignId)) {
					List<Advertise> item = new ArrayList<Advertise>();
					map.put(campaignId, item);
				}
				map.get(campaignId).add(advertise);
			}
		} catch (Exception e) {
			TrackLogger.error("Failed to load Advertiser", e);
		}
		return map;
	}
}
