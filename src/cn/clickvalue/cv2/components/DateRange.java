package cn.clickvalue.cv2.components;

import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;

public class DateRange {

	@Parameter(defaultPrefix = BindingConstants.PROP, required = true)
	private Date dateBegin;

	@Parameter(defaultPrefix = BindingConstants.PROP, required = true)
	private Date dateEnd;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
	private boolean readonly;

	@Parameter
	private boolean showShortcut;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "yyyy-MM-dd")
	private String datePattern;

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isShowShortcut() {
		return showShortcut;
	}

	public void setShowShortcut(boolean showShortcut) {
		this.showShortcut = showShortcut;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
}
