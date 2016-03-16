package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.PageDetached;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.SetupRender;

public class TestPage {
	
	@SetupRender
	public void setupRender() {
		System.out.println("setupRender");
	}

	@AfterRender
	public void afterRender() {
		System.out.println("afterRender");
	}

	@PageLoaded
	public void pageLoaded() {
		System.out.println("pageLoaded");
	}

	@BeginRender
	public void beginRender() {
		System.out.println("beginRender");
	}

	@CleanupRender
	public void cleanupRender() {
		System.out.println("cleanupRender");
	}

	@PageAttached
	public void pageAttached() {
		System.out.println("pageAttached");
	}

	@PageDetached
	public void pageDetached() {
		System.out.println("pageDetached");
	}

	@AfterRenderBody
	public void afterRenderBody() {
		System.out.println("afterRenderBody");
	}

	@BeforeRenderBody
	public void beforeRenderBody() {
		System.out.println("beforeRenderBody");
	}

	@AfterRenderTemplate
	public void afterRenderTemplate() {
		System.out.println("afterRenderTemplate");
	}

	@BeforeRenderTemplate
	public void beforeRenderTemplate() {
		System.out.println("beforeRenderTemplate");
	}
	
	public void onActivate(){
		System.out.println("onActivate");
	}
	
	
}
