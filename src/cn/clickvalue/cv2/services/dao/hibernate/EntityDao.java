package cn.clickvalue.cv2.services.dao.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * 针对单个Entity对象的操作定义. 不依赖于具体ORM实现方案.
 * 
 * 废弃,使用GeneralHibernateDao
 * @author calvin
 */
@Deprecated
public interface EntityDao<T> {

	T get(Serializable id);

	List<T> findAll();

	void save(Object o);

	void remove(Object o);

	void removeById(Serializable id);

	String getIdName(Class entityClass);
}
