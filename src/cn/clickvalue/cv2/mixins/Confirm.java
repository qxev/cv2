package cn.clickvalue.cv2.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

@IncludeJavaScriptLibrary("confirm.js")
public class Confirm {
	
	@Parameter(value = "Are you sure?", defaultPrefix = BindingConstants.LITERAL)
	private String message;

	@Inject
	private RenderSupport renderSupport;

	@InjectContainer
	private ClientElement element;

	@AfterRender
	public void afterRender() {
		renderSupport.addScript(String.format("new Confirm('%s', '%s');",
				element.getClientId(), this.message));
	}

}
