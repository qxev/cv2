package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class SemClientService extends BaseService<SemClient> {
	public SemClient findSemClientByUserId(Integer userId) {
		return findUniqueBy("advertiser.id", userId);
	}
	
	
	/**
	 * @return
	 * 
	 * 返回所有semClient的clientId
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findAllClientIds(){
		String hql = "select clientId from SemClient";
		List<Integer> ids = getHibernateTemplate().find(hql);
		return ids;
	}
}
