package cn.clickvalue.cv2.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;

public class ContextBindListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("shut down");
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		BeanFactory beanFactory = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		DefaultBeanFactory.setFactory(beanFactory);
	}

}
