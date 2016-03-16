package cn.clickvalue.cv2.services.logic;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.CampaignZhuanti;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class CampaignZhuantiService extends BaseService<CampaignZhuanti> {

	/**
	 * 初始化 CampaignZhuanti 对象
	 * 
	 * @return CampaignZhuanti
	 */
	public CampaignZhuanti createZhuanti() {
		CampaignZhuanti zhuanti = new CampaignZhuanti();
		zhuanti.setDeleted(0);
		return zhuanti;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllTitles() {
		String sql = " select zhuanti.title from CampaignZhuanti zhuanti; ";
		return this.getHibernateTemplate().find(sql);
	}

	public void delete(Integer zhuantiId) {
		this.getHibernateTemplate().bulkUpdate("update CampaignZhuanti zhuanti set deleted=1 where zhuanti.id=?", zhuantiId);
	}

	/**
	 * @param campaign
	 * @return 验证唯一性 name
	 */
	public boolean vaildateCampaignNameUnique(CampaignZhuanti zhuanti) {
		Criterion[] criterions = new Criterion[3];
		criterions[0] = Restrictions.eq("campaign", zhuanti.getCampaign());
		if (zhuanti.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", zhuanti.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllSubjects() {
		String sql = "select distinct zhuanti.subject from CampaignZhuanti zhuanti where zhuanti.deleted = 0";
		List<String> names = getHibernateTemplate().find(sql);
		return names;
	}
}
