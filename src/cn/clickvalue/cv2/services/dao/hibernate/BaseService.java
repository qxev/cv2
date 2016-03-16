package cn.clickvalue.cv2.services.dao.hibernate;

import java.util.Date;

import cn.clickvalue.cv2.model.base.PersistentObject;


/**
 * manager 的基类
 * 
 */
public class BaseService<T> extends GeneralHibernateDao<T> {
	@Override
	protected void onBeforeCreate(T m) {
		if (!(m instanceof PersistentObject)) {
			return;
		}
		PersistentObject model = (PersistentObject) m;
		model.setCreatedAt(new Date());
		model.setUpdatedAt(new Date());
	}
	
	@Override
	protected void onBeforeSave(T m) {
		if (!(m instanceof PersistentObject)) {
			return;
		}
		PersistentObject model = (PersistentObject) m;
		model.setUpdatedAt(new Date());
	}
}
