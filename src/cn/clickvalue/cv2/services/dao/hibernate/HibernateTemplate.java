package cn.clickvalue.cv2.services.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 廢棄模板 以后也不會用
 */
@Deprecated
public class HibernateTemplate {
	public static Object run(HibernateCallBack _callBack)
			throws HibernateException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			Object result = _callBack.execute(session);
			tx.commit();
			session.flush();
			return result;
		} catch (HibernateException e) {
			tx.rollback();
			return null;
		} finally {
			// HibernateUtil.closeSession();
		}
	}
}