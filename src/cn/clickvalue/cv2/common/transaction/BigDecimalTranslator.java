package cn.clickvalue.cv2.common.transaction;

import java.math.BigDecimal;

import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

public class BigDecimalTranslator implements Translator<BigDecimal> {
	// @Override
	public Class getType() {
		return BigDecimal.class;
	}

	// @Override
	public BigDecimal parseClient(String clientValue, Messages messages) throws ValidationException {
		if (InternalUtils.isBlank(clientValue)) {
			return null;
		}
		try {
            BigDecimal number = BigDecimal.valueOf(Double.valueOf(clientValue));
            return number;
        } catch (NumberFormatException e) {
            throw new ValidationException(messages.format("number-format-exception", clientValue));
        }
	}

	// @Override
	public String toClient(BigDecimal value) {
		return value == null ? "" : value.toEngineeringString();
	}
}
