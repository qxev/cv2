package cn.clickvalue.cv2.services.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

@Deprecated
/**
 * 廢棄方法
 */
public class HibernateBase<T> implements IHibernateBase<T> {

    private List list;

    private static final int pageSize = 30;

    public void save(final T object) throws HibernateException {
        HibernateTemplate.run(new HibernateCallBack() {
            public T execute(Session session) throws HibernateException {
                session.save(object);
                return null;
            }
        });
    }

    public void delete(final T object) throws HibernateException {
        HibernateTemplate.run(new HibernateCallBack() {
            public T execute(Session session) throws HibernateException {
                // session.delete(object);
                return null;
            }
        });
    }

    public void update(final T object) throws HibernateException {
        HibernateTemplate.run(new HibernateCallBack() {
            public T execute(Session session) throws HibernateException {
                session.update(object);
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public T findById(final int id, final Class clazz)
            throws HibernateException {
        return (T) HibernateTemplate.run(new HibernateCallBack() {
            public T execute(Session session) throws HibernateException {
                T result = (T) session.load(clazz, id);
                return result;
            }

        });
    }

    public Query pagination(Query query, int page) {
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query;
    }

    public Session getSession() {
        return HibernateSessionFactory.getSession();
    }

    public void closeSession() {
        HibernateSessionFactory.closeSession();
    }

    // public Object executeNamedQuery(final String namedQuery, final int id,
    // final int type) throws HibernateException {
    // return HibernateTemplate.run(new HibernateCallBack() {
    // public Object execute(Session session) throws HibernateException {
    // Query query = session.getNamedQuery(namedQuery);
    // if (type == 1) {
    // query.setInteger("id", id);
    // }
    // Object obj = (Object) query.list().get(0);
    // return obj;
    // }
    // });
    // }

}
