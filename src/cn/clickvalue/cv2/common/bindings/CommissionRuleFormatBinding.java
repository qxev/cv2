package cn.clickvalue.cv2.common.bindings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.model.CommissionRule;

public class CommissionRuleFormatBinding extends AbstractBinding {

	private Binding keyBinding;
	private Binding format;
	private boolean invariant;

	public CommissionRuleFormatBinding(Location location, boolean invariant, Binding keyBinding, Binding format) {
		super(location);
		this.invariant = invariant;
		this.keyBinding = keyBinding;
		this.format = format;
	}

	@SuppressWarnings("deprecation")
	// @Override
	public Object get() {
		CommissionRule commissionRule = (CommissionRule) keyBinding.get();
		return getFormatCommissionRule(commissionRule, format.get().toString());
	}

	public boolean isInvariant() {
		return this.invariant;
	}

	@SuppressWarnings("unchecked")
	public Class getBindingType() {
		return String.class;
	}

	private Object getFormatCommissionRule(CommissionRule commissionRule, String format) {
		String formatCommissionRule = format;
		if (!"(none)".equals(formatCommissionRule)) {
			Pattern pv = Pattern.compile("%v");
			Pattern pt = Pattern.compile("%t");
			Pattern pd = Pattern.compile("%d");
			Pattern pc = Pattern.compile("%c");
			Matcher mv = pv.matcher(format);
			if (mv.find()) {
				formatCommissionRule = mv.replaceAll(Constants.formatCommissionValue(commissionRule));
			}
			Matcher mt = pt.matcher(formatCommissionRule);
			if (mt.find()) {
				formatCommissionRule = mt.replaceAll(Constants.formatCommissionType(commissionRule));
			}
			Matcher md = pd.matcher(formatCommissionRule);
			if (md.find()) {
				formatCommissionRule = md.replaceAll(Constants.formatDarwinCommissionValue(commissionRule));
			}
			Matcher mc = pc.matcher(formatCommissionRule);
			if (mc.find()) {
				formatCommissionRule = Constants.formatCommissionRule(commissionRule);
			}
		} else {
			formatCommissionRule = Constants.formatCommissionRule(commissionRule);
		}
		return formatCommissionRule;
	}
}
