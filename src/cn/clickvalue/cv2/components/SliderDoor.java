package cn.clickvalue.cv2.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;

public class SliderDoor {

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String text;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String href;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String id = null;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String style = null;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private boolean clear = false;

	/**
	 * Example: <t:sliderdoor text="testsfsdfsdfsfsdfsdfsdfs"
	 * href="/affiliate/selectlandingpage/9/18" clear="true"
	 * style="float:right"/>
	 * 
	 * @param writer
	 */
	@BeforeRenderTemplate
	void beforeRenderTemplate(MarkupWriter writer) {
		Element div = writer.element("div", "class", "css_button");
		if (style != null) {
			div.attribute("style", style);
		}
		if (id != null) {
			div.attribute("id", id);
		}

		Element a = div.element("a", "href", href, "class", "without_decoration");
		a.text(text);
		writer.end();

		if (clear) {
			writer.element("div", "class", "clear");
			writer.end();
		}
	}
}
