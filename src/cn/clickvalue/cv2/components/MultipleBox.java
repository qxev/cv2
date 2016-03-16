package cn.clickvalue.cv2.components;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class MultipleBox extends AbstractField {
	/**
	 * The value to be read or updated. If not bound, the Checkbox will attempt
	 * to edit a property of its container whose name matches the component's
	 * id.
	 */
	@Parameter
	private String value;

	private StringBuffer sb = new StringBuffer();

	@Inject
	private Request request;

	@SuppressWarnings("unused")
	@Mixin
	private RenderDisabled renderDisabled;

	@Inject
	private ComponentResources resources;

	@Environmental
	private ValidationTracker tracker;

	Binding defaultValue() {
		return createDefaultParameterBinding("value");
	}

	@BeginRender
	void begin(MarkupWriter writer) {

		if (sb.length() > 0) {
			sb.setLength(0);
		}

		writer.element("input", "type", "checkbox", "name", getControlName(),
				"id", getClientId(), "checked", false ? "checked" : null,
				"value", value);
		resources.renderInformalParameters(writer);

		decorateInsideField();
	}

	@AfterRender
	void after(MarkupWriter writer) {
		writer.end(); // input
	}
	
	
	public String getValue(){
		return sb.toString();
	}
	
	public void setValue(){
		sb.setLength(0);
	}

	@Override
	protected void processSubmission(String elementName) {
		String postedValue = request.getParameter(elementName);
		tracker.recordInput(this, postedValue == null ? "" : postedValue);
		if (postedValue != null && !"".equals(postedValue)) {
			sb.append(postedValue);
			sb.append(",");
		}
	}
}
