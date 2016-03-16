package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.model.City;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CityService extends BaseService<City> {

	/**
	 * 根据 省id获取城市
	 * 
	 * @param provinceId
	 * @return List
	 */
	public List<City> getCityByProvinceId(Integer provinceId) {
		try {
			List<City> list = new ArrayList<City>();
			list = this.findBy("father", provinceId);
			return list;
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
