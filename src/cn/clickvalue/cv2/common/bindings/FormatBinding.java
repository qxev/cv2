package cn.clickvalue.cv2.common.bindings;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;

public class FormatBinding extends AbstractBinding {
	private final Messages messages;
	private final boolean invariant;
	private final List<Binding> keyBindings;
	private final List<Binding> valueBindings;

	public FormatBinding(Location location, Messages messages,
			boolean invariant, ArrayList<Binding> keyBindings,
			List<Binding> valueBindings) {
		super(location);

		this.messages = messages;
		this.invariant = invariant;
		this.keyBindings = keyBindings;
		this.valueBindings = valueBindings;
	}

	public FormatBinding(Location location, Messages messages,
			boolean invariant, ArrayList<Binding> keyBindings) {
		super(location);

		this.messages = messages;
		this.invariant = invariant;
		this.keyBindings = keyBindings;
		this.valueBindings = null;
	}

	public Object get() {
		StringBuilder key = new StringBuilder();
		for (Binding keyBinding : keyBindings) {
			key.append(keyBinding.get());
		}

		if (null == valueBindings)
			return messages.get(key.toString());

		List<Object> values = new ArrayList<Object>(valueBindings.size());
		for (Binding valueBinding : valueBindings) {
			values.add(valueBinding.get());
		}

		return messages.format(key.toString(), values.toArray());
	}

	public boolean isInvariant() {
		return this.invariant;
	}

	@SuppressWarnings("unchecked")
	public Class getBindingType() {
		return String.class;
	}

}
