package cn.clickvalue.cv2.services.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 廢棄接口
 * @author ang.xu
 *
 * @param <T>
 */
@Deprecated
public interface IHibernateBase<T> {

    public void save(final T object) throws HibernateException;

    public void delete(final T object) throws HibernateException;

    public void update(final T object) throws HibernateException;

    @SuppressWarnings("unchecked")
    public T findById(final int id, final Class clazz)
            throws HibernateException;
    
    public Query pagination(Query query,int page);
    
    public Session getSession();

    public void closeSession();

    // public Object executeNamedQuery(final String namedQuery, final int id,
    // final int type) throws HibernateException;

}
