package cn.clickvalue.cv2.common.grid;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

@SuppressWarnings("unchecked")
public class HibernateDataSource implements GridDataSource {

	private static Log logger = LogFactory.getLog(HibernateDataSource.class);
	private BaseService manager;
	private Iterator pageRowList;

	/**
	 * 外部传入的条件
	 */
	private CritQueryObject preSetQueryObject;

	public HibernateDataSource(BaseService manager) {
		this(manager, new CritQueryObject());
	}

	public HibernateDataSource(BaseService manager,
			CritQueryObject preSetQueryObject) {
		this.manager = manager;
		this.preSetQueryObject = preSetQueryObject;
		initPreSetQueryObject();
	}

	/**
	 * 清除preSetQueryObject中不必要的属性,以防误传 <br>
	 * 腾出example出来供filter使用
	 */
	private void initPreSetQueryObject() {
		preSetQueryObject.setFirstResult(null);
		preSetQueryObject.setMaxResults(null);

		if (preSetQueryObject.getExample() != null) {
			Example ex = Example.create(preSetQueryObject.getExample())
					.ignoreCase().enableLike(MatchMode.ANYWHERE);
			if (preSetQueryObject.getCriterions() == null) {
				preSetQueryObject.setCriterions(new Criterion[] { ex });
			} else {
				Criterion[] oldCrit = preSetQueryObject.getCriterions();
				Criterion[] newCrit = new Criterion[oldCrit.length + 1];
				System.arraycopy(oldCrit, 0, newCrit, 0, oldCrit.length);
				newCrit[newCrit.length - 1] = ex;
			}
		}
	}

	/**
	 * Returns the number of rows available in the data source.
	 */
	// @Override
	public int getAvailableRows() {
		return getMaximumResultObjects();
	}

	/**
	 * Invoked to allow the source to prepare to present values. This gives the
	 * source a chance to pre-fetch data (when appropriate) and informs the
	 * source of the desired sort order.
	 * 
	 * @param startIndex
	 *            the starting index to be retrieved
	 * @param endIndex
	 *            the ending index to be retrieved
	 * @param sortModel
	 *            the property model that defines what data will be used for
	 *            sorting, or null if no sorting is required (in which case,
	 *            whatever natural order is provided by the underlying data
	 *            source will be used)
	 * @param ascending
	 *            if true, then sort ascending, else decending
	 * 
	 * 第3个参数有问题 怎么可能会是多呢......在5.0.12中还是个Model这里竟然变成个集合了......
	 */
	// @Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sorts) {

		// 条件收集器
		CritQueryObject qo = collectQueryCondition(sorts);

		if (logger.isInfoEnabled())
			logger.info(String.format("processing prepare(%d, %d) for '%s'",
					startIndex, endIndex, manager.getEntityClass().getName()));
		int maxResults = endIndex - startIndex + 1;
		qo.setFirstResult(startIndex);
		qo.setMaxResults(maxResults);
		List find = manager.find(qo);
		this.pageRowList = find.iterator();
	}

	/**
	 * 收集查询条件,带有排序等功能
	 * 
	 * @return CritQueryObject
	 */
	private CritQueryObject collectQueryCondition(
			List<SortConstraint> sortConstraints) {
		CritQueryObject ret = new CritQueryObject();

		// hibernate QBC 约束
		if (preSetQueryObject.getCondition() != null
				&& preSetQueryObject.getCondition().size() > 0) {
			ret.setCondition(preSetQueryObject.getCondition());
		}

		if (preSetQueryObject.getCriterions() != null
				&& preSetQueryObject.getCriterions().length > 0) {
			ret.setCriterions(preSetQueryObject.getCriterions());
		}

		ret.setJoins(preSetQueryObject.getJoins());

		for (SortConstraint constraint : sortConstraints) {
			final ColumnSort sort = constraint.getColumnSort();

			if (sort == ColumnSort.UNSORTED)
				continue;

			final PropertyModel propertyModel = constraint.getPropertyModel();
			Order o = null;

			int i = StringUtils.countMatches(propertyModel.getPropertyName(),
					".");

			String sortName = null;

			// cat这里注释,要睡觉了 先这样吧 明天再弄,解决了问题,吵完了架
			if (i > 1) {
				sortName = StringUtils.substring(propertyModel
						.getPropertyName(), propertyModel.getPropertyName()
						.indexOf(".") + 1);
			} else {
				sortName = propertyModel.getPropertyName();
			}

			if (sort.name().equals("ASCENDING")) {
				o = Order.asc(sortName);
			} else {
				o = Order.desc(sortName);
			}
			ret.addOrder(o);
		}
		ret.getOrders().addAll(preSetQueryObject.getOrders());
		return ret;
	}

	// @Override
	public Class getRowType() {
		return this.manager.getEntityClass();
	}

	/**
	 * Returns the row value at the provided index. This method will be invoked
	 * in sequential order.
	 */
	// @Override
	public Object getRowValue(int index) {
		Object entityObject = null;
		if (pageRowList != null && pageRowList.hasNext()) {
			entityObject = pageRowList.next();
		}
		return entityObject;
	}

	/**
	 * get the maximum count of rows to display.
	 */
	private int getMaximumResultObjects() {
		return this.manager.count(preSetQueryObject);
	}
}
