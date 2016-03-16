package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;

import cn.clickvalue.cv2.common.constants.Constants;

/**
 * 审核方式 0:人工  1:自动
 */
public class AffiliateVerifiedFormatBinding extends AbstractBinding {

    public static int MANPOWER = 0;
    public static int AUTOMATISM = 1;
    
	private Binding keyBinding;
	private boolean invariant;
	private Messages messages;

	public AffiliateVerifiedFormatBinding(Location location, boolean invariant, Binding keyBinding, Messages messages) {
		super(location);
		this.invariant = invariant;
		this.keyBinding = keyBinding;
		this.messages = messages;
	}

	// @Override
	public Object get() {
		if (keyBinding.get() == null) {
			return "error Object is Null";
		} else {
			Integer flag = (Integer) keyBinding.get();
			return flag == MANPOWER ? "人工" : "自动"; //TODO 国际化
		}
	}

	@Override
	public boolean isInvariant() {
		return this.invariant;
	}

}
