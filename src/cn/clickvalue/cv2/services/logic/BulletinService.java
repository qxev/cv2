package cn.clickvalue.cv2.services.logic;

import java.util.List;

import org.hibernate.Query;

import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;

public class BulletinService extends BaseService<Bulletin> {

	/**
	 * @return 初始化默认Bulletin
	 */
	public Bulletin createBulletin() {
		Bulletin bulletin = new Bulletin();
		bulletin.setType(0);
		return bulletin;
	}

	public List<Bulletin> getBulletins(Integer type) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from Bulletin a ");
		sb.append(" where a.type=? ");
		sb.append(" order by a.createdAt desc");
		HqlQueryObject hqlQueryObject = new HqlQueryObject(sb.toString());
		hqlQueryObject.setFirstResult(0);
		hqlQueryObject.setMaxResults(5);
		hqlQueryObject.setParams(new Object[] { type });
		List<Bulletin> bulletins = find(hqlQueryObject);
		return bulletins;
	}

	@SuppressWarnings("unchecked")
	public List<Bulletin> findSysnews(Integer limit) {
		Query query = this.getQuery("from Bulletin a where a.tag=0 order by a.updatedAt desc");
		query.setFirstResult(0);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Bulletin> findCampnews(Integer limit) {
		Query query = this.getQuery("from Bulletin a where a.tag>0 order by a.updatedAt desc");
		query.setFirstResult(0);
		query.setMaxResults(limit);
		return query.list();
	}
}
