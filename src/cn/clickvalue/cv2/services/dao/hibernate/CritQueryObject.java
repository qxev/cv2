package cn.clickvalue.cv2.services.dao.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

/**
 * Criteria方式给定条件<br>
 * 主要有3中方式:1 example; 2 condition; 3 criterions
 * 
 */
public class CritQueryObject extends QueryObject {

	/**
	 * CritFinderObject
	 */
	private static final long serialVersionUID = 3452568761842467739L;

	/** 聚合条件集合 */
	private List<Projection> projections = new ArrayList<Projection>();

	/**
	 * 对字符串按like方式查询
	 */
	private boolean byLike = true;

	/**
	 * byLike时的匹配方式
	 */
	private MatchMode matchMode = MatchMode.ANYWHERE;

	/**
	 * order by 条件. eg: addOrder(Order.asc("name"))
	 */
	private List<Order> orders = new ArrayList<Order>();

	/**
	 * 以map方式给定的条件
	 */
	private Map<String, Object> condition;

	private List<Criterion> criterionList = new ArrayList<Criterion>();

	private Set<CritQueryObjectJoin> joins = new HashSet<CritQueryObjectJoin>();
	/**
	 * 以example方式给定的条件
	 */
	private Object example;

	public void addCriterion(Criterion c) {
		criterionList.add(c);
	}

	public void addCriterions(Criterion[] criterions) {
		for (int i = 0; i < criterions.length; i++) {
			addCriterion(criterions[i]);
		}
	}


	/**
	 * 添加聚合关系
	 * 
	 * @param associationPath
	 * @param alias
	 * @param joinType
	 */
	public void addProjection(Projection projection) {
		projections.add(projection);
	}
	
	public List<Projection> getProjections() {
		return projections;
	}

	public void setProjections(List<Projection> projections) {
		this.projections = projections;
	}

	/**
	 * 添加链接关系
	 * 
	 * @param associationPath
	 * @param alias
	 * @param joinType
	 */
	public void addJoin(String associationPath, String alias, int joinType) {
		joins.add(new CritQueryObjectJoin(associationPath, alias, joinType));
	}

	public Set<CritQueryObjectJoin> getJoins() {
		return joins;
	}

	public void setJoins(Set<CritQueryObjectJoin> joins) {
		this.joins = joins;
	}

	public CritQueryObject() {
		super();
	}

	public CritQueryObject(Integer firstResult, Integer maxResults) {
		super(firstResult, maxResults);
	}

	public CritQueryObject(Map<String, Object> condition) {
		super();
		this.condition = condition;
	}

	public CritQueryObject(Object example) {
		super();
		this.example = example;
	}

	public CritQueryObject(Criterion[] criterions) {
		super();
		addCriterions(criterions);
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}

	public QueryObject addOrder(Order o) {
		orders.add(o);
		return this;
	}

	public boolean isByLike() {
		return byLike;
	}

	public CritQueryObject setByLike(boolean byLike) {
		this.byLike = byLike;
		return this;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	public CritQueryObject setMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
		return this;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public CritQueryObject setCondition(Map<String, Object> condition) {
		this.condition = condition;
		return this;
	}

	public Criterion[] getCriterions() {
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	public CritQueryObject setCriterions(Criterion... criterions) {
		criterionList.clear();
		addCriterions(criterions);
		return this;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
