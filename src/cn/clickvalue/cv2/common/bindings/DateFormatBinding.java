package cn.clickvalue.cv2.common.bindings;

import java.util.Date;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;

import cn.clickvalue.cv2.common.util.DateUtil;

public class DateFormatBinding extends AbstractBinding {

	private Binding keyBinding;
	private Binding format;
	private boolean invariant;

	public DateFormatBinding(Location location, boolean invariant, Binding keyBinding, Binding format) {
		super(location);
		this.invariant = invariant;
		this.keyBinding = keyBinding;
		this.format = format;
	}

	@SuppressWarnings("deprecation")
	// @Override
	public Object get() {
		Date date = (Date) keyBinding.get();
		return DateUtil.dateToString(date, String.valueOf(format.get()));
	}

	public boolean isInvariant() {
		return this.invariant;
	}

	@SuppressWarnings("unchecked")
	public Class getBindingType() {
		return String.class;
	}
}
