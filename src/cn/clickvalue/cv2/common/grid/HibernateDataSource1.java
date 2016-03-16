package cn.clickvalue.cv2.common.grid;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;

public class HibernateDataSource1 implements GridDataSource {

	private static Log logger = LogFactory.getLog(HibernateDataSource1.class);

	private BaseService baseService;

	private HqlQueryObject hqlQueryObject;

	private String hqlCountString;

	private int rowCount = -1;

	private Iterator pageRowList;

	private Class clazz;

	public HibernateDataSource1(BaseService baseService, Class clazz,
			HqlQueryObject hqlQueryObject) {
		this(baseService, hqlQueryObject, clazz, null);
	}

	public HibernateDataSource1(BaseService baseService,
			HqlQueryObject hqlQueryObject, Class clazz,
			String hqlSpecialCountString) {
		this.baseService = baseService;
		this.hqlQueryObject = hqlQueryObject;
		this.clazz = clazz;
		this.hqlCountString = hqlSpecialCountString;
	}

	// @Override
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {

		hqlQueryObject.setFirstResult(startIndex);
		hqlQueryObject.setMaxResults(endIndex + 1);
		StringBuffer hql = new StringBuffer(hqlQueryObject.getHql());
		
		
		for (SortConstraint constraint : sortConstraints) {
			final ColumnSort sort = constraint.getColumnSort();

			if (sort == ColumnSort.UNSORTED)
				continue;

			final PropertyModel propertyModel = constraint.getPropertyModel();
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
				if(hql.toString().indexOf("order by ") > 0){
					hql.append(" ,");
					hql.append(sortName);
					hql.append(" asc");
				} else {
					hql.append(" order by ");
					hql.append(sortName);
					hql.append(" asc");
				}
			} else {
				if(hql.toString().indexOf("order by ") > 0){
					hql.append(" ,");
					hql.append(sortName);
					hql.append(" desc");
				} else {
					hql.append(" order by ");
					hql.append(sortName);
					hql.append(" desc");
				}
			}
			// 设置
		}
		hqlQueryObject.setHql(hql.toString());
		pageRowList = baseService.find(hqlQueryObject).iterator();
	}

	// @Override
	public int getAvailableRows() {
		if (rowCount < 0)
			rowCount = getMaximumResultObjects();
		return rowCount;
	}

	// @Override
	public Class getRowType() {
		return clazz.getClass();
	}

	// @Override
	public Object getRowValue(int index) {
		Object entityObject = null;
		if (pageRowList.hasNext()) {
			entityObject = pageRowList.next();
		}
		return entityObject;
	}

	private int getMaximumResultObjects() {
		String tempQuery1;
		String queryString = hqlQueryObject.getHql();

		try {
			// 判断是否 自己传入了count语句,偷懒做法
			if (hqlCountString != null) {
				tempQuery1 = hqlCountString;
			} else {
				// 程序自己的count
				tempQuery1 = convertQueryString(queryString);
			}

			Long result = baseService.count(tempQuery1,hqlQueryObject.getParams());
			if (result == null) {
				return 0;
			}
			return result.intValue();
		} catch (RuntimeException ex) {
			throw new TapestryException(
					"entity not \"mapped\" in hibernate.cfg.xml ?", this, ex);
		}
	}

	public static String convertQueryString(String originalQueryString) {
		String tempQueryString = StringUtils.substring(originalQueryString,
				StringUtils.indexOf(StringUtils.upperCase(originalQueryString),
						"FROM"));

		StringBuilder convertedQueryString = new StringBuilder("SELECT COUNT(*) ");
		Pattern p = Pattern.compile("[\\s]+");
		String[] result = p.split(tempQueryString);

		// 这里 去除 order
		for (String queryWord : result) {

			if (queryWord.equalsIgnoreCase("FETCH"))
				continue;

			if (queryWord.equalsIgnoreCase("ORDER"))
				break;

			convertedQueryString.append(queryWord).append(" ");
		}

		if (logger.isInfoEnabled())
			logger.info("source: " + originalQueryString
					+ System.getProperty("line.separator") + "dest: "
					+ convertedQueryString);
		return convertedQueryString.toString();
	}

//	public static void main(String[] args) {
//		System.out.println(convertQueryString(" FROM user"));
//	}
}
