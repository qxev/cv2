package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;

import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class LandingPageService extends BaseService<LandingPage> {

	/**
	 * 初始化 LandingPage 对象
	 * 
	 * @return LandingPage
	 */
	public LandingPage createLandingPage() {
		LandingPage landingPage = new LandingPage();
		landingPage.setActived(Integer.valueOf(0));
		landingPage.setDeleted(Integer.valueOf(0));
		landingPage.setVerified(Integer.valueOf(0));
		landingPage.setUrl("http://");
		return landingPage;
	}

	public List<LandingPage> findByCampaignId(Integer id) {
		List<LandingPage> list = new ArrayList<LandingPage>();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("deleted", 0);
		conditions.put("campaign.id", id);
		list = find(conditions);
		return list;
	}

	public List<LandingPage> findByCampaignIdAndVerified(int campaignId,
			int verified) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("deleted", 0);
		conditions.put("verified", verified);
		c.setCondition(conditions);
		List<LandingPage> landingPages = find(c);
		return landingPages;
	}

	public LandingPage getLandingPage(Integer landingPageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", landingPageId);
		CritQueryObject c = new CritQueryObject(map);
		c.addJoin("advertises", "advertise", Criteria.LEFT_JOIN);
		return findUniqueBy(c);
	}

	public int getBannerCount(LandingPage landingPage) {
		int count=0;
		List<Advertise> advertises = landingPage.getAdvertises();
		for(int i=advertises.size()-1; i>=0; i--){
			Advertise advertise = advertises.get(i);
			if(advertise.getDeleted() == 0 && advertise.getBanner().getDeleted() == 0){
				count++;
			}
		}
//		
//		Query query = getSession().createFilter(landingPage.getAdvertises(),
//				"where this.deleted = 0 and this.banner.deleted = 0 ");
		return count;
		
	}
}
