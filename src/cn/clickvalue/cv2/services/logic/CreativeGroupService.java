package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.Map;

import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class CreativeGroupService extends BaseService<LandingPage> {
	
	/**
	 * 根据landingPageid和campaignId获取 LandingPage
	 * @param landingPageId
	 * @param campaignId
	 * @return LandingPage
	 */
	public LandingPage getLandingPage(Integer landingPageId, Integer campaignId){
		CritQueryObject query = new CritQueryObject();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", landingPageId);
		map.put("campaign.id", campaignId);
		query.setCondition(map);
		return this.findUniqueBy(query);
	}
}
