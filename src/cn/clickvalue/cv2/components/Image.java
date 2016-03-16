/**
 * 
 */
package cn.clickvalue.cv2.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;

@SupportsInformalParameters
public class Image {
	@Environmental
	private RenderSupport support;
	@Inject
	private ComponentResources resources;
	/** * The image asset to render. */
	@Parameter(required = true, defaultPrefix = "literal")
	private String src;
	@Parameter(required = false, defaultPrefix = "literal", value = "context")
	private String domain;
	@Inject
	private AssetSource assetSource;

	@BeginRender
	void begin(MarkupWriter writer) {
		String clientId = support.allocateClientId(resources.getId());
		Asset image = assetSource.getAsset(null, domain + ":" + src, null);
		writer.element("img", "src", image.toClientURL(), "id", clientId);
		resources.renderInformalParameters(writer);
	}

	@BeforeRenderBody
	boolean beforeRenderBody() {
		return false;
	}

	@AfterRender
	void after(MarkupWriter writer) {
		writer.end();
	}
}