package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.clickvalue.cv2.model.PartnerId;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class PartnerIdService extends BaseService<PartnerId> {

	@Transactional(propagation = Propagation.NEVER)
	public void batchSave(List<PartnerId> partnerIds) {
		this.getHibernateTemplate().setFlushMode(HibernateAccessor.FLUSH_COMMIT);
		int num = 0;
		for (PartnerId partnerId : partnerIds) {
			save(partnerId);
			num++;
			if (num % 20 == 0) {
				this.flush();
			}
		}
		this.flush();
	}

	/**
	 * 判断是否已经存在typeId、ourId、partnerId的组合，保证typeId、ourId、partnerId联合唯一
	 * 
	 * 
	 * @param type
	 * @param partnerId
	 * @return
	 */
	public boolean isPartnerIdExist(int type, String partnerId) {
		List<PartnerId> result = find(" FROM PartnerId where type=? and partnerId=?", type, partnerId);
		if (result == null || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否已经存在typeId、ourId的组合，保证typeId、ourId联合唯一
	 * 
	 * 
	 * @param type
	 * @param ourId
	 * @return
	 */
	public boolean isOurIdExist(int type, String ourId) {
		List<PartnerId> result = find(" FROM PartnerId where type=? and ourId=?", type, ourId);
		if (result == null || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 查找某campaign对应所有ourId
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findOurIdsByType(int type) {
		return this.getHibernateTemplate().find("SELECT p.ourId FROM PartnerId p where p.type=?", type);
	}

	/**
	 * 查找某campaign对应所有partnerId
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findPartnerIdsByType(int type) {
		return this.getHibernateTemplate().find("SELECT p.partnerId FROM PartnerId p where p.type=?", type);
	}

	/**
	 * 根据网站Id查找partnerID
	 * 
	 * @param type
	 * @param ourId
	 * @return
	 */
	public PartnerId findByTypeAndOurId(int type, String ourId) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("type", type);
		condition.put("ourId", ourId);
		return this.findUniqueBy(condition);
	}
}
