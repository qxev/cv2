package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;

import cn.clickvalue.cv2.common.constants.Constants;

/**
 * 系统常用的状态绑定 未审核,待审核,审核通过,已拒绝
 */
public class GeneralStateBinging extends AbstractBinding {
	private Binding keyBinding;
	private boolean invariant;
	private Messages messages;

	public GeneralStateBinging(Location location, boolean invariant, Binding keyBinding, Messages messages) {
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
			return Constants.getVerifiedState(messages, flag);
		}
	}

	@Override
	public boolean isInvariant() {
		return this.invariant;
	}
}
