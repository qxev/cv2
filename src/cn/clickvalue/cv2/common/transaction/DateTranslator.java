package cn.clickvalue.cv2.common.transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

public class DateTranslator implements Translator<Date> {

	// @Override
	public Class getType() {
		return Date.class;
	}

	// @Override
	public Date parseClient(String clientValue, Messages messages) throws ValidationException {

		if (InternalUtils.isBlank(clientValue)) {
			return null;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			Date date = format.parse(clientValue);
			return date;
		} catch (ParseException ex) {
			throw new ValidationException(messages.format("number-format-exception", clientValue));
		}
	}

	// @Override
	public String toClient(Date value) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return value == null ? "" : format.format(value);
	}
}
