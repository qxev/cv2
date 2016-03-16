package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;

import cn.clickvalue.cv2.common.constants.Constants;

/**
 * 审核状态 0.未申请 1.待审核 2.已批准 3.已拒绝 4.申请下线
 */
public class StatusFormatBinding extends AbstractBinding {

	private Binding keyBinding;
	private boolean invariant;
	private Messages messages;

	public StatusFormatBinding(Location location, boolean invariant, Binding keyBinding, Messages messages) {
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
			return Constants.getCampignVerified(messages, flag);
		}
	}

	@Override
	public boolean isInvariant() {
		return this.invariant;
	}

}
