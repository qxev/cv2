package cn.clickvalue.cv2.components;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;

/**
 * 我的第一次的组件,我是仿照 appfuse做的.以后继续扩展.
 */
public class MessageBanner {
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";

	@Property
	@Parameter(required = true)
	private String message;

	@Property
	private String showMes;

	@Property
	private String hidMes;

	@Property
	private boolean hidden = false;

	@Property
	@Parameter(required = true)
	private String type;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	private Block error;

	@Inject
	private Block success;

	@Inject
	private ComponentResources resources;

	Binding defaultMessage() {
		return defaultProvider.defaultBinding("message", resources);
	}

	public Object getActiveBlock() {
		if (type == null) {
			return null;
		}
		if (!SUCCESS.equals(type) && !ERROR.equals(type)) {
			throw new RuntimeException("Invalid Message type: only 'error' or 'success' are supported");
		}
		return SUCCESS.equals(type) ? success : error;
	}

	Object beginRender() {
		if (message == null) {
			return false;
		}
		String mes = message.replaceAll("\r\n", "<br />");
		mes = mes.replaceAll("\n\r", "<br />");
		mes = mes.replaceAll("\r", "<br />");
		mes = mes.replaceAll("\n", "<br />");
		String[] messages = message.split("<br />", 11);
		if (messages.length > 10) {
			showMes = StringUtils.join(messages, "<br />", 0, 10);
			hidMes = mes.substring(showMes.length());
			hidden = true;
		} else {
			showMes = mes;
			hidden = false;
		}
		return null;
	}
	// var reRenderContent = function(id){
	// var div = document.getElementById(id);
	// console.log(div);
	// var content = div.innerHTML;
	// console.log(content);
	// var cs = content.split(/<br\/?>/,10);
	// console.log(cs.length);
	// if(cs.length = 10){
	// var str = cs.join("<br>");
	// var hidstr = content.substring(str.length,content.length);
	// str = str.concat('<span style="display: none;"');
	// str = str.concat(hidstr);
	// str = str.concat('</span>');
	// str = str.concat('<br><a href="#" onclick="showOrHidden(\'');
	// str = str.concat(id);
	// str = str.concat('\');">show</a>');
	// div.innerHTML = str;
	// }
	// }
}
