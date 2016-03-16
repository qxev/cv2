package cn.clickvalue.cv2.services.util.selector;

import java.util.Map;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.TapestryInternalUtils;

public final class GeneralOptionModel implements OptionModel {
    private final String _label;

    private final boolean _disabled;

    private final Object _value;

    private final Map<String, String> _attributes;

    public GeneralOptionModel(String label, boolean disabled, Object value,
            String... keysAndValues) {
        this(label, disabled, value,
                keysAndValues.length > 0 ? TapestryInternalUtils
                        .mapFromKeysAndValues(keysAndValues) : null);
    }

    public GeneralOptionModel(String label, boolean disabled, Object value,
            Map<String, String> attributes) {
        _label = label;
        _disabled = disabled;
        _value = value;
        _attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return _attributes;
    }

    public String getLabel() {
        return _label;
    }

    public Object getValue() {
        return _value;
    }

    public boolean isDisabled() {
        return _disabled;
    }

    @Override
    public String toString() {
        return String.format("OptionModel[%s %s]", _label, _value);
    }
}
