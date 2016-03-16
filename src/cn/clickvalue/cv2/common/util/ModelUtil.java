package cn.clickvalue.cv2.common.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import cn.clickvalue.cv2.model.base.PersistentObject;

/**
 * 模型类的util,主要是hibernate的辅助函数
 * 
 */
public class ModelUtil {
	/**
	 * 判断一个对象是空,即 ==null 或者 id==null
	 * 
	 * @param m
	 * @return
	 */
	public static boolean isNull(PersistentObject m) {
		if (m == null) {
			return true;
		}
		if (m.getId() == null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断一个对象是否 **不是** null,即!=null 并且 id!=null
	 * 
	 * @param m
	 * @return
	 */
	public static boolean isNotNull(PersistentObject m) {
		return !isNull(m);
	}

	/**
	 * 请尽量使用#isNull
	 * 
	 * @param m
	 * @return
	 */
	public static boolean isNew(PersistentObject m) {
		return isNull(m);
	}

	/**
	 * 请尽量使用#isNotNull
	 * 
	 * @param m
	 * @return
	 */
	public static boolean isNotNew(PersistentObject m) {
		return isNotNull(m);
	}

	/**
	 * 初始化,如果是lazy方式load的数据,在使用前进行initialize
	 * 
	 * @param m
	 */
	public static void initialize(Object proxy) {
		if (proxy == null) {
			return;
		}
		if (proxy instanceof HibernateProxy) {
			PersistentObject o = (PersistentObject) proxy;
			if (o.isNull()) {
				return;
			}
		}
		Hibernate.initialize(proxy);
	}
}
