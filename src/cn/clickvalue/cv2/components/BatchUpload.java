/**
 * 
 */
package cn.clickvalue.cv2.components;

import java.io.File;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.util.RealPath;

@IncludeJavaScriptLibrary(value = { "context:/assets/swfupload/js/swfupload.js", "context:/assets/swfupload/js/swfupload.queue.js",
		"context:/assets/swfupload/js/fileprogress.js", "context:/assets/swfupload/js/handlers.js" })
@IncludeStylesheet("context:/assets/swfupload/style/default.css")
@SupportsInformalParameters
public class BatchUpload {

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private ComponentResources resources;

	@Inject
	@Path("context:/assets/swfupload/swfupload.swf")
	private Asset flashUrl;

	@Inject
	@Path("context:/assets/swfupload/images/TestImageNoText_65x29.png")
	private Asset buttonImageUrl;

	@Inject
	private MultipartDecoder decoder;

	@Parameter(required = true, defaultPrefix = BindingConstants.PROP)
	private String folder;
	
	@Inject
	private Cookies cookies;
	
	void beginRender(MarkupWriter writer) {
		writer.element("h2").cdata("目录：".concat(folder));
		writer.end();
		writer.element("br");
		writer.end();
		Element progress = writer.element("div", "class", "fieldset flash", "id", allocateClientId("process_div"));
		Element queue = progress.element("span", "class", "legend");
		queue.cdata("Upload Queue");
		writer.end();

		Element status = writer.element("div", "id", allocateClientId("status_div"));
		status.cdata("0 Files Uploaded");
		writer.end();

		Element operate = writer.element("div");
		Element browse = operate.element("span", "id", allocateClientId("_browse_div"));
		// Element cancel = operate.element("input", "type", "button", "value",
		// "Cancel All", "id", allocateClientId("cancel_button"));
		// cancel.attribute("style",
		// "margin-left: 2px; font-size: 8pt; height: 29px;");
		Element submit = operate.element("input", "type", "button", "value", "Upload", "id", allocateClientId("submit_button"));
		submit.attribute("style", "margin-left: 5px; font-size: 8pt; height: 29px;");
		writer.end();

		Link link = resources.createActionLink("upload", false, new Object[] { folder });

		JSONObject settings = new JSONObject();

		JSONObject customSettings = new JSONObject();
		customSettings.put("progressTarget", progress.getAttribute("id"));
		// customSettings.put("cancelButtonId", cancel.getAttribute("id"));
		customSettings.put("statusTarget", status.getAttribute("id"));
		
		//session问题没有解决，ie本来就是好的，ff加了jSESSIONID也没有好
		JSONObject postParams = new JSONObject();
		postParams.put("JSESSIONID", cookies.readCookieValue("JSESSIONID"));
		
		settings.put("custom_settings", customSettings);
		settings.put("flash_url", flashUrl.toClientURL());
		settings.put("upload_url", link.toURI());
		settings.put("post_params", postParams);
		settings.put("file_size_limit", "100 MB");
		settings.put("file_types", getFileTypes());
		settings.put("file_types_description", "Files");
		settings.put("file_upload_limit", 100);
		settings.put("file_queue_limit", 0);
		settings.put("debug", false);
		settings.put("button_image_url", buttonImageUrl.toClientURL());
		settings.put("button_width", 65);
		settings.put("button_height", 29);
		settings.put("button_placeholder_id", browse.getAttribute("id"));
		settings.put("button_text", "Browser");
		settings.put("button_text_style", ".theFont { font-size: 20; }");
		settings.put("button_text_left_padding", 12);
		settings.put("button_text_top_padding", 3);

		String varSetttings = allocateClientId("settings");
		String varSwfu = allocateClientId("swfu");
		StringBuilder sb = new StringBuilder();
		sb.append("var ").append(varSetttings).append(" = eval(%s);");
		sb.append(varSetttings).append(".file_queued_handler=fileQueued;");
		sb.append(varSetttings).append(".file_queue_error_handler=fileQueueError;");
		sb.append(varSetttings).append(".file_dialog_complete_handler=function(){};");
		sb.append(varSetttings).append(".upload_start_handler=uploadStart;");
		sb.append(varSetttings).append(".upload_progress_handler=uploadProgress;");
		sb.append(varSetttings).append(".upload_error_handler=uploadError;");
		sb.append(varSetttings).append(".upload_success_handler=uploadSuccess;");
		sb.append(varSetttings).append(".upload_complete_handler=uploadSuccess;");
		sb.append(varSetttings).append(".queue_complete_handler=queueComplete;");
		sb.append("var ").append(varSwfu).append(" = new SWFUpload(").append(varSetttings).append(");");

		renderSupport.addScript(sb.toString(), settings);
		// renderSupport.addScript("document.getElementById('%s').onclick=function(){%s.cancelQueue();}",
		// cancel.getAttribute("id"), varSwfu);
		renderSupport.addScript("document.getElementById('%s').onclick=function(){%s.startUpload();}", submit.getAttribute("id"), varSwfu);

	}

	// folder之所以要从页面传回来，而不直接拿实例变量里的folder，是因为swfupload应该是用类似httpclient的东西在上传东西，这个时候上传功能和UI是两个客户端
	// 我观察过这两种请求，Cookie里的JSESSIONID确实是不一样的。
	// tapestry里的persist在此时就不起作用了。但是我奇怪的是，管上传的客户端是什么时候登录系统的。原来是事件请求没有被权限系统管到，已经加上事件的权限管理。
	Object onUpload(String folder) {
		UploadedFile file = decoder.getFileUpload("Filedata");
		String fileName = file.getFileName();
		String path = RealPath.getRealPath(folder);
		File savePath = new File(path);
		if (!savePath.exists()) {
			savePath.mkdir();
		}
		file.write(new File(savePath.getPath().concat(File.separator).concat(fileName)));
		return true;// 事件不冒泡，container中的onUpload不运行
	}

	private String getFileTypes() {
		if (folder.endsWith("images")) {
			return "*.jpg;*.gif;*.bmp;*.png";
		}
		if (folder.endsWith("js")) {
			return "*.js";
		}
		if (folder.endsWith("style")) {
			return "*.css";
		}
		if (folder.endsWith("other")) {
			return "*.html;*.htm;*.jpg;*.gif;*.bmp;*.png;*.js;*.css;*.swf";
		}
		return "*.html;*.htm";
	}

	private String allocateClientId(String id) {
		return renderSupport.allocateClientId(resources.getId().concat("_").concat(id));
	}
}