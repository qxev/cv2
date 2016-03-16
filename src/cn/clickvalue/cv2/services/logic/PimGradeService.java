package cn.clickvalue.cv2.services.logic;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.PimGrade;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class PimGradeService extends BaseService<PimGrade> {

	public void delete(PimGrade grade) {
		this.getHibernateTemplate().delete(grade);
	}

	public PimGrade getGradeByPoints(Long points) {
		CritQueryObject c = new CritQueryObject();
		c.addCriterion(Restrictions.le("point", points));
		c.addOrder(Order.desc("grade"));
		c.setFirstResult(0);
		c.setMaxResults(1);
		List<PimGrade> grades = this.find(c);
		if (grades.size() > 0) {
			return grades.get(0);
		} else {
			return null;
		}

	}
}
