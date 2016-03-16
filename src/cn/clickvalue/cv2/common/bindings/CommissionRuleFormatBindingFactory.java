package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

public class CommissionRuleFormatBindingFactory implements BindingFactory {
	private final BindingSource bindingSource;
	

	public CommissionRuleFormatBindingFactory(BindingSource source) {
		this.bindingSource = source;
	}

	public Binding newBinding(String description, ComponentResources container,
			ComponentResources component, String expression, Location location) {

		int separatorIndex = expression.indexOf("=");
		String value;
		String key;
		if (separatorIndex == -1) {
			value = "(none)";
			key = expression;
		} else {
			value = expression.substring(separatorIndex + 1);
			key = expression.substring(0, separatorIndex);
		}

		Binding b_key = createBindings(description, container, component,
				BindingConstants.PROP, key, location);
		Binding b_value = createBindings(description, container, component,
				BindingConstants.LITERAL, value, location);
		boolean invariant = isInvariant(b_key) && isInvariant(b_value);
		return new CommissionRuleFormatBinding(location, invariant, b_key, b_value);
	}

	private boolean isInvariant(Binding binding) {
		if (!binding.isInvariant())
			return false;
		return true;
	}

	private Binding createBindings(String description,
			ComponentResources container, ComponentResources component,
			String defaultPrefix, String expression, Location location) {
		return bindingSource.newBinding(description, container, component,
				defaultPrefix, expression, location);
	}
}
