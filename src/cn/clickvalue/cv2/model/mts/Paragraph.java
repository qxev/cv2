package cn.clickvalue.cv2.model.mts;

import org.apache.commons.lang.builder.EqualsBuilder;

import cn.clickvalue.cv2.common.util.UUIDUtil;

public class Paragraph implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subject;

	private String content;

	private String id;
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Paragraph() {
		id = UUIDUtil.getUUID();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject == null ? "" : subject;
	}

	public String getContent() {
		return content;
	}
	
	public String getHtmlContent(){
		String hContent = content.replaceAll("\n\r", "<br />");
		hContent = hContent.replaceAll("\r\n", "<br />");
		hContent = hContent.replaceAll("\n", "<br />");
		hContent = hContent.replaceAll("\r", "<br />");
		return hContent;
	}

	public void setContent(String content) {
		this.content = content == null ? "" : content;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof Paragraph == false) {
			return false;
		}
		Paragraph o = (Paragraph) obj;
		return new EqualsBuilder().append(this.id, o.id).isEquals();
	}

}
