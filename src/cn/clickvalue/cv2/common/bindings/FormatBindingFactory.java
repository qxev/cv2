package cn.clickvalue.cv2.common.bindings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

public class FormatBindingFactory implements BindingFactory {
	private static final String SEPARATOR = "=";
	private static final String DELIMITER = ",";

	private static final String KEY_PREFIX = BindingConstants.LITERAL;
	private static final String VALUE_PREFIX = BindingConstants.PROP;

	private final BindingSource bindingSource;

	public FormatBindingFactory(BindingSource bindingSource) {
		this.bindingSource = bindingSource;
	}

	public Binding newBinding(String description, ComponentResources container,
			ComponentResources component, String expression, Location location) {
		int separatorIndex = expression.indexOf(SEPARATOR);
		if (-1 == separatorIndex) {
			List<String> keys = Arrays.asList(expression.split(DELIMITER));

			ArrayList<Binding> keyBindings = createBindings(description,
					container, component, KEY_PREFIX, keys, location);

			boolean invariant = isInvariant(keyBindings);
			return new FormatBinding(location, container.getMessages(),
					invariant, keyBindings);
		}

		List<String> keys = Arrays.asList(expression.substring(0,
				separatorIndex).split(DELIMITER));
		
		
		ArrayList<Binding> keyBindings = createBindings(description, container,
				component, KEY_PREFIX, keys, location);

		List<String> values = Arrays.asList(expression.substring(
				separatorIndex + 1).split(DELIMITER));
		
		
		ArrayList<Binding> valueBindings = createBindings(description,
				container, component, VALUE_PREFIX, values, location);

		boolean invariant = isInvariant(keyBindings)
				&& isInvariant(valueBindings);
		
		
		return new FormatBinding(location, container.getMessages(), invariant,
				keyBindings, valueBindings);
	}

	private ArrayList<Binding> createBindings(String description,
			ComponentResources container, ComponentResources component,
			String defaultPrefix, List<String> expressions, Location location) {
		ArrayList<Binding> bindings = new ArrayList<Binding>(expressions.size());

		for (String expression : expressions) {
			bindings.add(bindingSource.newBinding(description, container,
					component, defaultPrefix, expression, location));
		}

		return bindings;
	}

	private boolean isInvariant(ArrayList<Binding> bindings) {
		for (Binding binding : bindings) {
			if (!binding.isInvariant())
				return false;
		}

		return true;
	}

}
