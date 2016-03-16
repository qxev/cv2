package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.model.AdvertiserSumInfo;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AdvertiserSumInfoService extends BaseService<AdvertiserSumInfo> {

	/**
	 * 根据userId获取 AdvertiserSumInfo
	 * @return List
	 */
	public List<AdvertiserSumInfo> findAdvertiserSumInfoByUserId(Integer userId) {
		List<AdvertiserSumInfo> list = new ArrayList<AdvertiserSumInfo>();
		list = findBy("userId", userId);
		return list;
	}
}
