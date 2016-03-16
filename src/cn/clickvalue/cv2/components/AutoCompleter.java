package cn.clickvalue.cv2.components;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractTextField;

@IncludeJavaScriptLibrary("classpath:cn/clickvalue/cv2/components/loadAutoCompleter.js")
public final class AutoCompleter extends AbstractTextField {

	@Parameter(defaultPrefix = BindingConstants.PROP)
	private List<String> source;

	@Environmental
	private RenderSupport renderSupport;

	@Override
	protected final void writeFieldTag(MarkupWriter writer, String value) {
		writer.element("input",

		"type", "text",

		"name", getControlName(),

		"id", getClientId(),

		"value", value,

		"size", getWidth());
	}

	final void afterRender(MarkupWriter writer) {
		writer.end(); // input
		writeScript();
	}

	final void writeScript() {

		// arr_id = [];
		StringBuffer arr = new StringBuffer();
		String var = "arr_".concat(getClientId());
		arr.append("var ");
		arr.append(var);
		arr.append(" = [\"");
		arr.append(StringUtils.join(source, "\",\""));
		arr.append("\"];");

		// var af = new AutoFieldFromLocal('id',arr_id);
		StringBuffer af = new StringBuffer();
		af.append("var af = new AutoFieldFromLocal('");
		af.append(getClientId());
		af.append("', ");
		af.append(var);
		af.append(");");

		// FIXME addScript中调用了String.format方法，%是String.format(String
		// str,Object[])中要替换的字符的开始符号，所以如果没有要替换的字符，先要把%转义掉。
		// %dddd
		renderSupport.addScript(arr.toString().replace("%", "%%"));
		renderSupport.addScript(af.toString().replace("%", "%%"));
	}
}
