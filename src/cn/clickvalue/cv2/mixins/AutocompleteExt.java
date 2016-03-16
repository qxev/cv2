package cn.clickvalue.cv2.mixins;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.CSSClassConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.internal.services.ResponseRenderer;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

@IncludeJavaScriptLibrary("${tapestry.scriptaculous}/controls.js")
public class AutocompleteExt {
	static final String EVENT_NAME = "autocomplete";

	@Inject
	private ComponentResources _resources;

	private static final String PARAM_NAME = "t:input";

	@Parameter(required = false, defaultPrefix = "literal")
	private String _updateElementHook;

	/**
	 * The field component to which this mixin is attached.
	 */
	@InjectContainer
	private Field field;

	@Inject
	private ComponentResources resources;

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private Request request;

	@Inject
	private TypeCoercer coercer;

	@Inject
	private MarkupWriterFactory factory;

	@Inject
	@Path("classpath:org/apache/tapestry5/ajax-loader.gif")
	private Asset loader;

	/**
	 * Overwrites the default minimum characters to trigger a server round trip
	 * (the default is 1).
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private int minChars;

	@Inject
	private ResponseRenderer responseRenderer;

	/**
	 * Overrides the default check frequency for determining whether to send a
	 * server request. The default is .4 seconds.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private double frequency;

	/**
	 * If given, then the autocompleter will support multiple input values,
	 * seperated by any of the individual characters in the string.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String tokens;

	/**
	 * Mixin afterRender phrase occurs after the component itself. This is where
	 * we write the &lt;div&gt; element and the JavaScript.
	 * 
	 * @param writer
	 */
	void afterRender(MarkupWriter writer) {
		String id = field.getClientId();

		String menuId = id + ":menu";
		String loaderId = id + ":loader";

		// This image is made visible while the request is being processed.
		// To be honest, I think Prototype hides it too soon, it should wait
		// until the <div> is fully positioned and updated.

		writer.element("img", "src", loader.toClientURL(), "class",
				CSSClassConstants.INVISIBLE, "id", loaderId);
		writer.end();

		writer.element("div", "id", menuId, "class", "t-autocomplete-menu");
		writer.end();

		Link link = resources.createActionLink(EVENT_NAME, false);

		JSONObject config = new JSONObject();
		config.put("paramName", PARAM_NAME);
		config.put("indicator", loaderId);

		if (resources.isBound("minChars"))
			config.put("minChars", minChars);

		if (resources.isBound("frequency"))
			config.put("frequency", frequency);

		if (resources.isBound("tokens")) {
			for (int i = 0; i < tokens.length(); i++) {
				config.accumulate("tokens", tokens.substring(i, i + 1));
			}
		}

		// Let subclasses do more.
		configure(config);

		renderSupport.addScript(
				"new Ajax.Autocompleter('%s', '%s', '%s', %s);", id, menuId,
				link.toAbsoluteURI(), config);
	}

	Object onAutocomplete() {
		String input = request.getParameter(PARAM_NAME);
		final Holder<List> matchesHolder = Holder.create();
		// Default it to an empty list.
		matchesHolder.put(Collections.emptyList());

		ComponentEventCallback callback = new ComponentEventCallback() {
			public boolean handleResult(Object result) {
				List matches = coercer.coerce(result, List.class);
				matchesHolder.put(matches);
				return true;
			}
		};

		resources.triggerEvent("providecompletions", new Object[] { input },
				callback);
		final ContentType contentType = responseRenderer.findContentType(this);
		final MarkupWriter writer = factory.newMarkupWriter(contentType);
		generateResponseMarkup(writer, matchesHolder.get());
		return new StreamResponse() {
			public void prepareResponse(Response response) {
			}
			public InputStream getStream() throws IOException {
				return new ByteArrayInputStream(writer.toString().getBytes(
						"UTF-8"));
			}
			public String getContentType() {
				return contentType.getMimeType();
			}
		};
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to
	 * the JavaScript Ajax.Autocompleter options. The values minChars, frequency
	 * and tokens my be pre-configured. Subclasses may override this method to
	 * configure additional features of the Ajax.Autocompleter. <p/> <p/> This
	 * implementation does nothing.
	 * 
	 * @param config
	 *            parameters object
	 */
	protected void configure(JSONObject setup) {
		if (_resources.isBound("updateElementHook"))
			setup.put("afterUpdateElement", _updateElementHook);
	}

	/**
	 * Generates the markup response that will be returned to the client; this
	 * should be an &lt;ul&gt; element with nested &lt;li&gt; elements.
	 * Subclasses may override this to produce more involved markup (including
	 * images and CSS class attributes).
	 * 
	 * @param writer
	 *            to write the list to
	 * @param matches
	 *            list of matching objects, each should be converted to a string
	 */
	protected void generateResponseMarkup(MarkupWriter writer, List matches) {
		writer.element("ul");

		for (Object o : matches) {
			Object[] array = (Object[]) o;
			writer.element("li", "id", array[0].toString());
			writer.write(array[1].toString());
			writer.end();
		}
		writer.end(); // ul
	}
}
