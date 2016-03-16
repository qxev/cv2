package cn.clickvalue.cv2.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class UseLineStripper extends HTMLEditorKit.ParserCallback {

	private Writer out;

	private String lineSeparator;

	private StringBuffer strbuf;

	// 构造器
	public UseLineStripper(StringBuffer strbuf) {
		this.strbuf = strbuf;
		this.lineSeparator = System.getProperty("line.separator", "\r\n");
	}

	/*
	 * public UseLineStripper(Writer out) { this(out,
	 * System.getProperty("line.separator", "\r\n")); }
	 */

	public UseLineStripper(Writer out, String lineSeparator) {
		this.out = out;
		this.lineSeparator = lineSeparator;
	}

	// 文本处理
	public void handleText(char[] text, int position) {
		/*
		 * try { //输出文本 //out.write(text); //out.flush();
		 * //System.out.println("==============================================="
		 * ); } catch (IOException e) { System.err.println(e); }
		 */
		strbuf.append(text);
	}

	// 结束标记处理
	public void handleEndTag(HTML.Tag tag, int position) {
		/*
		 * try {
		 */
		if (tag.isBlock()) {

			strbuf.append(lineSeparator);
		} else if (tag.breaksFlow()) {
			/* out.write(lineSeparator); */
			strbuf.append(lineSeparator);
		}
		/*
		 * }catch (IOException e) { System.err.println(e); }
		 */
	}

	// 简单标记处理
	public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
		/*
		 * try {
		 */
		if (tag.isBlock()) {
			/*
			 * out.write(lineSeparator); out.write(lineSeparator);
			 */
			strbuf.append(lineSeparator);
		} else if (tag.breaksFlow()) {
			/* out.write(lineSeparator); */
			strbuf.append(lineSeparator);
		} else {
			/* out.write(' '); */
			strbuf.append("  ");
		}
		/*
		 * }catch (IOException e) { System.err.println(e); }
		 */
	}

	public static String deleteHtmlTag(String htmlText) throws Exception {
		if (null == htmlText || "".equals(htmlText)) {
			return htmlText;
		}
		StringBuffer sb = new StringBuffer();
		HTMLEditorKit.ParserCallback callback = new UseLineStripper(sb);
		try {
			InputStreamReader rr = new InputStreamReader((InputStream) new java.io.ByteArrayInputStream(htmlText.getBytes()));
			new ParserDelegator().parse(rr, callback, true);
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String deleteHtmlTag(String htmlText, String charsetName) throws Exception {
		if (null == htmlText || "".equals(htmlText)) {
			return htmlText;
		}
		StringBuffer sb = new StringBuffer();
		HTMLEditorKit.ParserCallback callback = new UseLineStripper(sb);
		try {
			InputStreamReader rr = new InputStreamReader((InputStream) new java.io.ByteArrayInputStream(htmlText.getBytes(charsetName)),
					charsetName);
			new ParserDelegator().parse(rr, callback, true);
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String deleteHtmlTag(File file) throws Exception {
		StringBuffer sb = new StringBuffer();
		HTMLEditorKit.ParserCallback callback = new UseLineStripper(sb);
		InputStream in = null;
		ByteArrayOutputStream byteArray = null;
		InputStreamReader rr = null;
		try {
			in = new FileInputStream(file);
			byteArray = new ByteArrayOutputStream();
			int ch = 0;
			while ((ch = in.read()) != -1) {
				byteArray.write(ch);
			}
			String strData = byteArray.toString();
			rr = new InputStreamReader((InputStream) new java.io.ByteArrayInputStream(strData.getBytes()));
			new ParserDelegator().parse(rr, callback, true);
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (rr != null) {
				rr.close();
			}
			if (byteArray != null) {
				byteArray.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	public static String deleteHtmlTag(String url, Object[] obj) throws Exception {
		StringBuffer sb = new StringBuffer();
		HTMLEditorKit.ParserCallback callback = new UseLineStripper(sb);
		InputStream in = null;
		ByteArrayOutputStream byteArray = null;
		InputStreamReader rr = null;
		try {
			URL u = new URL(url);
			in = u.openStream();
			byteArray = new ByteArrayOutputStream();
			int ch = 0;
			while ((ch = in.read()) != -1) {
				byteArray.write(ch);
			}
			String strData = byteArray.toString();
			rr = new InputStreamReader((InputStream) new java.io.ByteArrayInputStream(strData.getBytes()));
			new ParserDelegator().parse(rr, callback, true);
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (rr != null) {
				rr.close();
			}
			if (byteArray != null) {
				byteArray.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	// 主程序
	public static void main(String[] args) throws Exception {
		// StringBuffer sb = new StringBuffer();
		// HTMLEditorKit.ParserCallback callback = new UseLineStripper(sb);
		// try {
		// URL url=new URL("http://www.pchome.net");
		// InputStream in = url.openStream();
		// ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		// int ch = 0;
		// while ((ch = in.read()) != -1) {
		// byteArray.write(ch);
		// }
		// String strData =
		// "<p>亲爱的站长们：<br /><br />达闻新广告——<a href=\"../../../../../../../../../admin/campaign/viewpage/80/admin%252Fcampaign%252FlistPage\">运气视频CPC广告</a>上线了，欢迎大家积极投放，广告详情请查看广告活动介绍。<br /><br /> <br /><br />有问题请联系QQ：907543002 。谢谢大家的合作。<br /><br /> <br /><br />  达闻联盟营销<br /><br />2009.2.13</p> ";
		// String strData = byteArray.toString();
		// InputStreamReader rr = new InputStreamReader((InputStream) new
		// java.io.ByteArrayInputStream(strData.getBytes()));
		// new ParserDelegator().parse(rr, callback, true);
		// System.out.println(sb);
		// System.out.println("=====================================================>");
		// System.out.println(deleteHtmlTag(new File("d:/html.text")));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// String strData =
		// "<p>亲爱的站长们：<br /><br />达闻新广告——<a href=\"../../../../../../../../../admin/campaign/viewpage/80/admin%252Fcampaign%252FlistPage\">运气视频CPC广告</a>上线了，欢迎大家积极投放，广告详情请查看广告活动介绍。<br /><br /> <br /><br />有问题请联系QQ：907543002 。谢谢大家的合作。<br /><br /> <br /><br />  达闻联盟营销<br /><br />2009.2.13</p> ";
		// String afterData = deleteHtmlTag(strData,"utf-8");
		// System.out.println(afterData);

	}
}
