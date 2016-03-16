package cn.clickvalue.cv2.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.DateUtil;

public class DateRangeShortCut {
	
	@Inject
	private Messages messages;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String beginId;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String endId;

	public static final String TODAY = "today";
	public static final String YESTERDAY = "yesterday";
	public static final String THIS_MONTH = "thisMonth";
	public static final String LAST_MONTH = "lastMonth";
	public static final String LAST_SEVEN_DAYS = "lastSevenDays";
	public static final String LAST_MONTH_DAYS = "lastMonthDays";

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "yyyy-MM-dd")
    private String datePattern;

	public static final List<String[]> RANGES = new ArrayList<String[]>();

	private Date current = new Date();

	void setupRender() {
		if (RANGES.size() > 0 && DateUtils.isSameDay(current, DateUtil.stringToDate(RANGES.get(0)[1], datePattern))) {
			return;
		}else{
			RANGES.clear();
			put(TODAY, current, current);
			put(YESTERDAY, DateUtils.addDays(current, -1), DateUtils.addDays(current, -1));
			put(THIS_MONTH, DateUtils.setDays(current, 1), current);
			put(LAST_MONTH, DateUtils.addMonths(DateUtils.setDays(current, 1), -1), DateUtils.addDays(DateUtils.setDays(current, 1), -1));
			put(LAST_SEVEN_DAYS, DateUtils.addDays(current, -7), current);
			put(LAST_MONTH_DAYS, DateUtils.addMonths(current, -1), current);
		}
	}

	void beginRender(MarkupWriter writer) {
		for (String[] range : RANGES) {
			writer.write(" [");
			writer.element("a", "href", String.format("javascript:setRange('%s','%s');", range[1], range[2]));
			writer.write(messages.get(range[0]));
			writer.end();
			writer.write("] ");
		}
		
		StringBuffer script = new StringBuffer("function setRange (dateBegin, dateEnd){ \n");
		script.append("\tvar begin = document.getElementById(\"%s\");\n");
		script.append("\tvar end = document.getElementById(\"%s\");\n");
		script.append("\tbegin.value = dateBegin;\n");
		script.append("\tend.value = dateEnd;\n");
		script.append("}");
		
		writer.element("script", "type", "text/javascript");
		writer.writef(script.toString(), beginId, endId);
		writer.end();
	}

	private void put(String key, Date begin, Date end) {
		RANGES.add(new String[] { key, DateUtil.dateToString(begin, datePattern), DateUtil.dateToString(end, datePattern) });
	}
}
