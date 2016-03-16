package cn.clickvalue.cv2.services.dao.hibernate;

/**
 * hql方式给定条件
 * 
 */
public class HqlQueryObject extends QueryObject {
	/**
	 * HqlQueryObject
	 */
	private static final long serialVersionUID = 9000441186124426573L;

	/**
	 * 以hql方式给定的条件
	 */
	private String hql;

	/**
	 * 参数
	 */
	private Object[] params;

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public HqlQueryObject(String hql) {
		this.hql = hql;
	}

	public HqlQueryObject() {
		super();
	}

	public HqlQueryObject(Integer firstResult, Integer maxResults) {
		super(firstResult, maxResults);
	}

	public HqlQueryObject(Integer firstResult, Integer maxResults, String hql) {
		super(firstResult, maxResults);
		this.hql = hql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

}
