package cn.clickvalue.cv2.services.dao.hibernate;

import java.io.Serializable;

/**
 * 查询对象,参见<<企业应用架构模式-查询对象>> <br>
 * HqlQueryObject <br>
 * CritQueryObject
 * 
 */
public class QueryObject implements Serializable {
	/**
	 * QueryObject
	 */
	private static final long serialVersionUID = -9064846297452084582L;

	/**
	 * 开始记录
	 */
	private Integer firstResult;

	/**
	 * 最大数
	 */
	private Integer maxResults;

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	// default constructor
	public QueryObject() {
		super();
	}

	// full field constructor
	public QueryObject(Integer firstResult, Integer maxResults) {
		super();
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

}
