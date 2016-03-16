package cn.clickvalue.cv2.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.Request;

/**
 * This is especially useful in loops, where the component name is dynamic.
 * 动态组件学习
 */
public final class SubmitContext extends AbstractField {

	public static final String SELECTED_EVENT = "maomao";

	/**
	 * If true, then any notification sent by the component will be deferred
	 * until the end of the form submission (this is usually desirable).
	 */
	@Parameter
	private boolean _defer = true;

	@Parameter
	private String _context;

	@Environmental
	private FormSupport _formSupport;

	@Environmental
	private Heartbeat _heartbeat;

	@Inject
	private ComponentResources _resources;

	@Inject
	private Request _request;

	public SubmitContext() {
	}

	SubmitContext(Request request) {
		_request = request;
	}

	@BeginRender
	void beginRender(MarkupWriter writer) {
		// write a hidden input for the context
		// String elementName = getElementName();
		String elementName = getControlName(); // Modified to work with 5.0.12
		writer.element("input", "type", "hidden", "name", elementName + "X",
				"value", _context);
		writer.end();

		// now the submit
		writer.element("input", "type", "submit", "name", elementName, "id",
				getClientId());
		_resources.renderInformalParameters(writer);
	}

	@AfterRender
	void afterRender(MarkupWriter writer) {
		writer.end();
	}

	@Override
	protected void processSubmission(String elementName) {
		String value = _request.getParameter(elementName);
		final String context = _request.getParameter(elementName + "X");

		if (value == null)
			return;

		Runnable sendNotification = new Runnable() {
			public void run() {
				_resources.triggerEvent(SELECTED_EVENT,
						new Object[] { context }, null);
			}
		};

		// When not deferred, don't wait, fire the event now (actually, at the
		// end of the current
		// heartbeat). This is most likely because the Submit is inside a Loop
		// and some contextual
		// information will change if we defer. Another option might be to wait
		// until the next
		// heartbeak?

		if (_defer)
			_formSupport.defer(sendNotification);
		else
			_heartbeat.defer(sendNotification);

	}

	void setDefer(boolean defer) {
		_defer = defer;
	}

	void setup(ComponentResources resources, FormSupport support,
			Heartbeat heartbeat) {
		_resources = resources;
		_formSupport = support;
		_heartbeat = heartbeat;
	}

}
