package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class HPBlockContentService extends BaseService<HPBlockContent> {

	public List<HPBlockContent> findAll() {
		return this.find("from HPBlockContent content left join fetch content.hpBlock;");
	}

	public void delete(HPBlockContent hpBlockContent) {
		this.getHibernateTemplate().delete(hpBlockContent);
	}

	public void delete(Integer id) {
		this.getHibernateTemplate().bulkUpdate("delete from HPBlockContent content where content.id=?", id);
	}
}
