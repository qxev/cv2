package cn.clickvalue.cv2.common.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DefaultBeanFactory {
	public static final String BEAN_REF_FACTORY_FILE_NAME="classpath:spring-main.xml";
	private static BeanFactory factory;
	private static boolean isInWebContext = true;

	public static void setFactory(BeanFactory beanFactory){
		if(beanFactory == null)
			return;
		isInWebContext = true;
		factory = beanFactory;
	}
	/**
	 * 根据beanName(对应于Bean配置的Id属性）获取Bean对象
	 * @param beanName
	 * @return
	 */
	public final static Object getBean(String beanName) {
		return getFactory().getBean(beanName);
	}
	public final static Object getBean(String beanName, String ctxName) {
		throw new UnsupportedOperationException();
	}
	public final static boolean isSingleton(String beanName) {
		return getFactory().isSingleton(beanName);
	}
	/**
	 * 获取Spring工厂
	 * @return
	 */

	public final static BeanFactory getFactory() {
		if(factory == null){
			isInWebContext = false;
			factory = createBeanFactory();
		}
		return factory;
	}
	protected static BeanFactory createBeanFactory(){
		return new ClassPathXmlApplicationContext(new String[]{BEAN_REF_FACTORY_FILE_NAME},true);
	}

	public static boolean isInWebContext() {
		return isInWebContext;
	}
}
