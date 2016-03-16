package cn.clickvalue.cv2.services.dao.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import cn.clickvalue.cv2.common.util.GenericsUtils;

/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类. <p/> 子类只要在类定义时指定所管理Entity的Class,
 * 即拥有对单个Entity对象的CRUD操作. eg.
 * 
 * <pre>
 * public class UserManager extends HibernateEntityDao&lt;User&gt; {
 * }
 * </pre>
 * 
 */
@SuppressWarnings("unchecked")
public class GeneralHibernateDao<T> extends HibernateDaoSupport {
	
	public void evict(T o) {
		if(o != null && isNotNew(o)) {
			getHibernateTemplate().evict(o);
		}
	}

	public void refresh(T o) {
		if (o != null && isNotNew(o)) {
			getHibernateTemplate().refresh(o);
		}
	}

	public void initialize(Object o) {
		if (o != null && isNotNew(o)) {
			getHibernateTemplate().initialize(o);
		}
	}

	public void flush(){
		getHibernateTemplate().flush();
	}
	
	public void clear(){
		getHibernateTemplate().clear();
	}
	/**
	 * DAO所管理的Entity类型.
	 */
	protected Class<T> entityClass;

	/**
	 * 取得entityClass. JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 根据Map中的条件的Criteria查询
	 * 
	 * @param map
	 *            Map中仅包含条件名与条件值，默认全部相同,可重载。
	 */
	public List<T> find(Map map) {
		Criteria criteria = getEntityCriteria();
		return find(criteria, map);
	}

	/**
	 * 根据QueryObject qo查询
	 * 
	 */
	public List<T> find(QueryObject qo) {
		Criteria criteria = getEntityCriteria();
		if (qo.getMaxResults() != null) {
			criteria.setMaxResults(qo.getMaxResults());
		}
		if (qo.getFirstResult() != null) {
			criteria.setFirstResult(qo.getFirstResult());
		}
		return criteria.list();
	}

	/**
	 * 根据QueryObject qo查询
	 * 
	 */
	public List<T> find(HqlQueryObject qo) {
		Query query = null;
		if (qo.getParams() != null) {
			query = getQuery(qo.getHql(), qo.getParams());
		} else {
			query = getQuery(qo.getHql());
		}

		if (qo.getMaxResults() != null) {
			query.setMaxResults(qo.getMaxResults());
		}
		if (qo.getFirstResult() != null) {
			query.setFirstResult(qo.getFirstResult());
		}

		return query.list();
	}

	public long count(String hql, Object... objects) {
		Query query = getQuery(hql, objects);
		try {
			return ((Long) query.uniqueResult());
		} catch (Exception e) {
			return query.list().size();
		}
	}

	/**
	 * 返回唯一的对象值
	 * 
	 * @param qo
	 * @return T
	 */
	public T findUniqueBy(CritQueryObject qo) {
		Criteria crit = getEntityCriteria();
		Set<CritQueryObjectJoin> joins = qo.getJoins();
		for (Iterator iter = joins.iterator(); iter.hasNext();) {
			CritQueryObjectJoin join = (CritQueryObjectJoin) iter.next();
			crit.createAlias(join.getAssociationPath(), join.getAlias(), join
					.getJoinType());
		}
		if (qo.getMaxResults() != null) {
			crit.setMaxResults(qo.getMaxResults());
		}
		if (qo.getFirstResult() != null) {
			crit.setFirstResult(qo.getFirstResult());
		}

		if (qo.getCondition() != null) {
			crit.add(Expression.allEq(qo.getCondition()));
		}

		if (qo.getCriterions() != null) {
			for (int i = 0; i < qo.getCriterions().length; i++) {
				crit.add(qo.getCriterions()[i]);
			}
		}

		if (qo.getExample() != null) {
			Example example = Example.create(qo.getExample()).ignoreCase();
			if (qo.isByLike()) {
				example.enableLike(qo.getMatchMode());
			}
			crit.add(example);
		}

		if (qo.getOrders() != null && qo.getOrders().size() > 0) {
			for (int i = 0; i < qo.getOrders().size(); i++) {
				crit.addOrder(qo.getOrders().get(i));
			}
		}
		return (T) crit.uniqueResult();
	}

	/**
	 * 返回唯一的对象值
	 * 
	 * @param qo
	 * @return T
	 */
	public Object findObject(CritQueryObject qo) {
		Criteria crit = getEntityCriteria();
		Set<CritQueryObjectJoin> joins = qo.getJoins();
		for (Iterator iter = joins.iterator(); iter.hasNext();) {
			CritQueryObjectJoin join = (CritQueryObjectJoin) iter.next();
			crit.createAlias(join.getAssociationPath(), join.getAlias(), join
					.getJoinType());
		}

		if (qo.getProjections() != null && qo.getProjections().size() > 0) {
			ProjectionList projList = Projections.projectionList();
			for (int i = 0; i < qo.getProjections().size(); i++) {
				Projection projection = qo.getProjections().get(i);
				projList.add(projection);
			}
			crit.setProjection(projList);
		}

		if (qo.getCondition() != null) {
			crit.add(Expression.allEq(qo.getCondition()));
		}

		if (qo.getCriterions() != null) {
			for (int i = 0; i < qo.getCriterions().length; i++) {
				crit.add(qo.getCriterions()[i]);
			}
		}

		if (qo.getExample() != null) {
			Example example = Example.create(qo.getExample()).ignoreCase();
			if (qo.isByLike()) {
				example.enableLike(qo.getMatchMode());
			}
			crit.add(example);
		}

		if (qo.getOrders() != null && qo.getOrders().size() > 0) {
			for (int i = 0; i < qo.getOrders().size(); i++) {
				crit.addOrder(qo.getOrders().get(i));
			}
		}
		return crit.uniqueResult();
	}

	/**
	 * 根据CritQueryObject查询
	 * 
	 * @param qo
	 * @return
	 */
	public List<T> find(CritQueryObject qo) {
		Criteria crit = getEntityCriteria();
		Set<CritQueryObjectJoin> joins = qo.getJoins();

		for (Iterator iter = joins.iterator(); iter.hasNext();) {
			CritQueryObjectJoin join = (CritQueryObjectJoin) iter.next();
			crit.createAlias(join.getAssociationPath(), join.getAlias(), join
					.getJoinType());
		}
		if (qo.getMaxResults() != null) {
			crit.setMaxResults(qo.getMaxResults());
		}
		if (qo.getFirstResult() != null) {
			crit.setFirstResult(qo.getFirstResult());
		}

		if (qo.getCondition() != null) {
			crit.add(Expression.allEq(qo.getCondition()));
		}

		if (qo.getCriterions() != null) {
			for (int i = 0; i < qo.getCriterions().length; i++) {
				crit.add(qo.getCriterions()[i]);
			}
		}

		if (qo.getExample() != null) {
			Example example = Example.create(qo.getExample()).ignoreCase();
			if (qo.isByLike()) {
				example.enableLike(qo.getMatchMode());
			}
			crit.add(example);
		}

		if (qo.getOrders() != null && qo.getOrders().size() > 0) {
			for (int i = 0; i < qo.getOrders().size(); i++) {
				crit.addOrder(qo.getOrders().get(i));
			}
		}
		return crit.list();
	}

	/**
	 * 根据Map中的条件的Criteria查询
	 * 
	 * @param map
	 *            Map中仅包含条件名与条件值，默认全部相同,可重载。
	 */
	public List<T> find(Criteria criteria, Map map) {
		Assert.notNull(criteria);
		criteria.add(Expression.allEq(map));
		return criteria.list();
	}

	/**
	 * hql查询.
	 * 
	 * @param values
	 *            可变参数 用户可以如下四种方式使用 dao.find(hql) dao.find(hql,arg0);
	 *            dao.find(hql,arg0,arg1); dao.find(hql,new
	 *            Object[arg0,arg1,arg2])
	 */
	public List<T> find(String hql, Object... values) {
		Assert.hasText(hql);
		return getHibernateTemplate().find(hql, values);
	}

	/**
	 * 根据传入的条件查询
	 * 
	 * @param criterion
	 * @return
	 */
	public List<T> find(Criterion... criterion) {
		Criteria crit = getEntityCriteria();
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

	/**
	 * 获取全部对象
	 */
	public List<T> findAll() {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	/**
	 * 
	 * @param first
	 *            开始
	 * @param count
	 *            记录数
	 * @return
	 */
	public List<T> findAll(int first, int count) {
		Criteria crit = getEntityCriteria();
		crit.setFirstResult(first);
		crit.setMaxResults(count);
		return crit.list();
	}

	public final List<T> findAll(int first, int count, String property,
			boolean isAscending) {
		Criteria crit = getEntityCriteria();
		if (StringUtils.isNotBlank(property)) {
			if (isAscending) {
				crit.addOrder(Order.asc(property));
			} else {
				crit.addOrder(Order.desc(property));
			}
		}
		crit.setFirstResult(first);
		crit.setMaxResults(count);
		return crit.list();
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @return 符合条件的唯一对象
	 */
	public T findUniqueBy(String name, Object value) {
		Assert.hasText(name);
		return (T) getCriteria(getEntityClass(), Restrictions.eq(name, value))
				.uniqueResult();
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @return 符合条件的唯一对象
	 */
	public T findUniqueBy(Map<String, Object> map) {
		Assert.notEmpty(map, "map is null");
		return (T) getCriteria(getEntityClass(), Restrictions.allEq(map))
				.uniqueResult();
	}

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(String name, Object value) {
		Assert.hasText(name);
		return getCriteria(getEntityClass(), Restrictions.eq(name, value))
				.list();
	}

	/**
	 * 根据属性名和属性值以Like AnyWhere方式查询对象.
	 */
	public List<T> findByLike(String name, String value) {
		Assert.hasText(name);
		return getCriteria((Class<T>) getEntityClass(),
				Restrictions.like(name, value, MatchMode.ANYWHERE)).list();
	}

	/**
	 * qbe（query by example方式的findall）
	 * 
	 * @param example
	 *            如果example==null，则不会按example查询
	 * @param first
	 * @param count
	 * @param property
	 * @param isAscending
	 * @return
	 */
	public final List<T> qbeFind(Object example, int first, int count,
			String property, boolean isAscending) {
		Criteria crit = getEntityCriteria();

		if (example != null) {
			crit.add(Example.create(example).ignoreCase().enableLike(
					MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(property)) {
			if (isAscending) {
				crit.addOrder(Order.asc(property));
			} else {
				crit.addOrder(Order.desc(property));
			}
		}
		crit.setFirstResult(first);
		crit.setMaxResults(count);
		return crit.list();
	}

	/**
	 * 计算实体总数 select count(*)
	 * 
	 * @return
	 */
	public int count() {
		Criteria crit = getEntityCriteria().setProjection(
				Projections.rowCount());

		int totalCount = Integer.valueOf(crit.setProjection(
				Projections.rowCount()).uniqueResult().toString());
		return totalCount;
	}

	public int count(CritQueryObject qo) {
		Criteria crit = getEntityCriteria();
		Set<CritQueryObjectJoin> joins = qo.getJoins();
		for (Iterator iter = joins.iterator(); iter.hasNext();) {
			CritQueryObjectJoin join = (CritQueryObjectJoin) iter.next();
			crit.createAlias(join.getAssociationPath(), join.getAlias(), join
					.getJoinType());
		}
		if (qo.getCondition() != null) {
			crit.add(Expression.allEq(qo.getCondition()));
		}
		if (qo.getCriterions() != null) {
			for (int i = 0; i < qo.getCriterions().length; i++) {
				crit.add(qo.getCriterions()[i]);
			}
		}
		if (qo.getExample() != null) {
			Example example = Example.create(qo.getExample()).ignoreCase();
			if (qo.isByLike()) {
				example.enableLike(qo.getMatchMode());
			}
			crit.add(example);
		}

		int totalCount = 0;
		
		totalCount = Integer.valueOf(crit.setProjection(Projections.rowCount())
				.uniqueResult().toString());
		return totalCount;
	}

	/**
	 * 根据map中的条件count
	 * 
	 * @param map
	 * @return
	 */
	public int count(Map map) {
		Criteria crit = getEntityCriteria().setProjection(
				Projections.rowCount());
		crit.add(Expression.allEq(map));

		int totalCount = Integer.valueOf(crit.setProjection(
				Projections.rowCount()).uniqueResult().toString());

		return totalCount;
	}

	/**
	 * query by example方式的count
	 * 
	 * @param example
	 * @return
	 */
	public int qbeCount(Object example) {
		Criteria crit = getEntityCriteria();
		if (example != null) {
			crit.add(Example.create(example).ignoreCase().enableLike(
					MatchMode.ANYWHERE));
		}
		crit.setProjection(Projections.rowCount());
		int totalCount = Integer.valueOf(crit.setProjection(
				Projections.rowCount()).uniqueResult().toString());

		return totalCount;
	}

	/**
	 * 在构造函数中将泛型T.class赋给entityClass
	 */
	public GeneralHibernateDao() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 根据ID获取对象
	 */
	public T get(Serializable id) {
		Class<T> entityClass = (Class<T>) getEntityClass();
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	public T load(Serializable id) {
		Class<T> entityClass = (Class<T>) getEntityClass();
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	/**
	 * 不分大小写查询
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public List<T> ignoreCaseFindBy(String name, Object value) {
		Assert.hasText(name);
		return getEntityCriteria().add(
				Restrictions.eq(name, value).ignoreCase()).list();
	}

	/**
	 * 根据ID移除对象.
	 */
	// public void removeById(Serializable id) {
	// removeById(getEntityClass(), id);
	// }
	/**
	 * 取得Entity的Criteria.
	 */
	public Criteria getEntityCriteria() {
		return getCriteria(getEntityClass());
	}

	/**
	 * 获得id属性名
	 */
	public String getIdName(Class entityClass) {
		Assert.notNull(entityClass);
		SessionFactory sessionFactory = getSessionFactory();
		ClassMetadata classMetadata = sessionFactory
				.getClassMetadata(entityClass);
		String idName = classMetadata.getIdentifierPropertyName();
		Assert.hasText(idName, entityClass.getSimpleName()
				+ "has no id column define");
		return idName;
	}

	/**
	 * 判断对象某些属性的值在数据库中不存在重复
	 * 
	 * @param names
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	@SuppressWarnings("hiding")
	public <T> boolean isNotUnique(Object entity, String names) {
		Class<T> entityClass = (Class<T>) getEntityClass();
		Assert.hasText(names);
		Criteria criteria = getCriteria(entityClass).setProjection(
				Projections.rowCount());
		String[] nameList = names.split(",");
		try {
			// 循环加入
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(
						entity, name)));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getIdName(entityClass);
			// 通过反射取得entity的主键值
			Object id = PropertyUtils.getProperty(entity, idName);
			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));

		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() > 0;
	}

	/**
	 * 删除对象
	 * 
	 * @deprecated pls use destroy (参照rais的命名)
	 */
	// public void remove(Object o) {
	// getHibernateTemplate().delete(o);
	// }
	/**
	 * 删除实体对象
	 * 
	 * @param o
	 */
	// public void destroy(T o) {
	// onBeforeDestroy(o);
	// doDelete(o);
	// onAfterDestroy(o);
	// }
	protected void doDelete(T o) {
		getHibernateTemplate().delete(o);
	}

	/**
	 * 删除对象前的回掉方法
	 * 
	 * @param o
	 */
	protected void onBeforeDestroy(T o) {

	}

	/**
	 * 删除对象后的回调方法
	 * 
	 * @param o
	 */
	protected void onAfterDestroy(T o) {

	}

	/**
	 * 根据ID删除对象
	 */
	// @SuppressWarnings("deprecation")
	// public void removeById(Class<T> entityClass, Serializable id) {
	// remove(get(id));
	// }
	/**
	 * 获得所有第一级的对象
	 * 
	 * @return
	 */
	public List<T> getAllFirstLevel() {
		Criteria criteria = getEntityCriteria();
		criteria.add(Restrictions.isNull("parent"));
		return criteria.list();
	}

	/**
	 * 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以返回Query后自行设置.
	 * 留意可以连续设置,如 dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * 
	 * @param values
	 *            可变参数 用户可以如下四种方式使用 dao.getQuery(hql) dao.getQuery(hql,arg0);
	 *            dao.getQuery(hql,arg0,arg1); dao.getQuery(hql,new
	 *            Object[arg0,arg1,arg2])
	 */
	public Query getQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 创建Criteria对象
	 * 
	 * @param criterion
	 *            可变条件列表,Restrictions生成的条件
	 */
	@SuppressWarnings("hiding")
	public <T> Criteria getCriteria(Class<T> entityClass,
			Criterion... criterion) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 是否新增的，还没有保存的对象。一般根据id==null判断
	 * 
	 * @param entity
	 * @return
	 */
	public boolean isNew(Object entity) {
		Object id = null;
		try {
			String idName = getIdName(getEntityClass());
			// 通过反射取得entity的主键值
			id = PropertyUtils.getProperty(entity, idName);
			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (id == null);
	}

	/**
	 * 是否*【不是】*新增的，还没有保存的对象
	 * 
	 * @param entity
	 * @return
	 */
	public boolean isNotNew(Object entity) {
		return !isNew(entity);
	}

	protected void onBeforeCreate(T m) {

	}

	protected void onAfterCreate(T m) {

	}

	protected void doInsert(T m) {
		getHibernateTemplate().saveOrUpdate(m);
	}

	protected void doUpdate(T m) {
		getHibernateTemplate().saveOrUpdate(m);
	}

	public void merge(Object m) {
		getHibernateTemplate().merge(m);
	}

	public void save(T o) {
		T m = (T) o;
		boolean isnew = isNew(m);
		if (isnew) {
			onBeforeValidation(m);
			onBeforeValidationOnCreate(m);
			doValidation(m);
			onAfterValidation(m);
			onAfterValidationOnCreate(m);
			onBeforeSave(m);
			onBeforeCreate(m);
			doInsert(m);
			onAfterCreate(m);
			onAfterSave(m);
		} else {
			onBeforeValidation(m);
			onBeforeValidationOnUpdate(m);
			doValidation(m);
			onAfterValidation(m);
			onAfterValidationOnUpdate(m);
			onBeforeSave(m);
			onBeforeUpdate(m);
			doUpdate(m);
			onAfterUpdate(m);
			onAfterSave(m);
		}
	}

	protected void onAfterValidationOnUpdate(T m) {

	}

	protected void onBeforeValidationOnUpdate(T m) {

	}

	protected void onAfterValidationOnCreate(T m) {

	}

	protected void onAfterValidation(T m) {

	}

	protected void doValidation(T m) {

	}

	protected void onBeforeValidationOnCreate(T m) {

	}

	protected void onBeforeValidation(T m) {

	}

	protected void onAfterUpdate(T m) {

	}

	protected void onBeforeUpdate(T m) {

	}

	protected void onAfterSave(T m) {

	}

	protected void onBeforeSave(T m) {

	}
}
