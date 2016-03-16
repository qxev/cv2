package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

public class StatusFormatBindingFactory implements BindingFactory {

	private final BindingSource bindingSource;

	public StatusFormatBindingFactory(BindingSource source) {
		this.bindingSource = source;
	}

	// @Override
	public Binding newBinding(String description, ComponentResources container, ComponentResources component,
			String expression, Location location) {
		Binding keyBinding = bindingSource.newBinding(description, container, component, BindingConstants.PROP,
				expression, location);
		return new StatusFormatBinding(location, false, keyBinding, container.getMessages());
	}

}
