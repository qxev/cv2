package cn.clickvalue.cv2.pages.demo;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.Demo;
import cn.clickvalue.cv2.services.logic.demo.DemoService;


public class DemoEditPage {
	@Inject
	@Service("demoService")
	private DemoService demoService;
	
	private String id;

	@Persist
	private Demo demo;

	void onPrepare() {
		if (demo == null) {
			demo = new Demo();
		}
	}

	void cleanupRender() {
		demo = null;
	}

	void onActionFromClear() {
		demo = null;
	}

	Object onSuccessFromDemoForm() {
		demoService.save(getDemo());
		return DemoListPage.class;
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(String id) {
		this.id = id;
		demo = (Demo) demoService.get(id);
	}

	String onPassivate() {
		return id;
	}

	public DemoService getDemoService() {
		return demoService;
	}

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}

	public Demo getDemo() {
		return demo;
	}

	public void setDemo(Demo demo) {
		this.demo = demo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
