package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.clickvalue.cv2.model.AdvertiseAffiliate;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AdvertiseAffiliateService extends BaseService<AdvertiseAffiliate> {
	public AdvertiseAffiliate createAdvertiseAffiliate() {
		AdvertiseAffiliate advertiseAffiliate = new AdvertiseAffiliate();
		advertiseAffiliate.setActived(0);
		advertiseAffiliate.setStatus(0);
		return advertiseAffiliate;
	}

	public boolean isExistByAdvertiseAndSiteId(Integer advertiseId,
			Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("advertise.id", advertiseId);
		map.put("site.id", siteId);
		return this.count(map) > 0 ? true : false;
	}

	public AdvertiseAffiliate getAdvertiseAffiliateByAdvertiseAndSiteId(
			Integer advertiseId, Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("advertise.id", advertiseId);
		map.put("site.id", siteId);
		
		List<AdvertiseAffiliate> advertiseAffiliates = this.find(map);
		if(advertiseAffiliates.size() > 0){
			return advertiseAffiliates.get(0);
		} else {
			return createAdvertiseAffiliate();
		}
	}
}
