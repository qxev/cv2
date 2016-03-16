package cn.clickvalue.cv2.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;

public class SliderDoorSubmit {

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String value;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String form = "form";

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String style = null;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private boolean clear = false;

	/**
	 * Example:
	 * 
	 * <t:SliderDoorSubmit form="form" value="获取广告代码"/>
	 * 
	 * @param writer
	 */
	@BeforeRenderTemplate
	void beforeRenderTemplate(MarkupWriter writer) {
		Element div = writer.element("div", "class", "css_button");
		if (style != null) {
			div.attribute("style", style);
		}

		Element a = div.element("a", "class", "without_decoration", "href", "#", "onclick",
				"javascript:document.getElementById('" + form + "').submit();");
		a.text(value);
		writer.end();

		if (clear) {
			writer.element("div", "class", "clear");
			writer.end();
		}
	}
}
