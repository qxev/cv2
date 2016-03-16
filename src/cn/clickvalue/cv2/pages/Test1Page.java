package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.TransformUtils;

/**
 * 测试自定义事件
 */
public class Test1Page {

	@Component(id = "form")
	private Form form;

	@Property
	private String idString = "猫猫";
	
	@Inject
	private ComponentResources _resources;

	void onSelectedFromTheSubmit(String idString) {
		System.out.println(idString);
	}
	
	/**
	 * 我的第一个自定义 组件事件名称
	 * @param idString
	 */
	void onMaomaoFromTheSubmit(String idString) {
		System.out.println(TransformUtils.getUnwrapperMethodName("string"));
		System.out.println(idString);
		System.out.println(_resources.getCompleteId());
		System.out.println(_resources.getComponent());
		System.out.println(_resources.getAnnotationProvider("form"));
		System.out.println(_resources.getBoundType("idString"));
		System.out.println(_resources.getEmbeddedComponent("form"));
	}
}
