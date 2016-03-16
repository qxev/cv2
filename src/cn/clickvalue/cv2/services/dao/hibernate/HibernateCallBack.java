package cn.clickvalue.cv2.services.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;

@Deprecated
/**
 * 廢棄接口
 */
public interface HibernateCallBack {
	Object execute(Session session) throws HibernateException;
}
