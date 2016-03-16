package cn.clickvalue.cv2.common.util;

import java.math.BigDecimal;

public abstract class BigDecimalUtils {

	public static float toFloat(BigDecimal obj, float nullValue) {
		if (obj == null) {
			return nullValue;
		} else {
			return obj.floatValue();
		}
	}

	public static float toFloat(BigDecimal obj) {
		return toFloat(obj, 0.00f);
	}

}
