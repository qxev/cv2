package cn.clickvalue.cv2.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.internal.OptionGroupModelImpl;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.util.AbstractSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;

public class IndustryForCampaignSelect {

	public static final String MAIN = "main";

	public static final String SECONDARY = "secondary";

	public static final String BOTH = "both";

	@Inject
	private ComponentResources resources;

	/**
	 * The value to read or update.
	 */
	@Parameter(required = true, principal = true)
	private Object value;

	@Parameter
	private String displayModel = BOTH;
	
	@Parameter(value = "never", defaultPrefix = BindingConstants.LITERAL)
    private BlankOption blankOption;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String blankLabel;
    
    private final List<OptionGroupModel> optgroups = CollectionFactory.newList();

	private final List<OptionModel> options = CollectionFactory.newList();
	
	private String prefix = IndustryForCampaignEnum.class.getSimpleName();

	void beginRender() {
		optgroups.clear();
		options.clear();
		if (BOTH.equals(displayModel)) {
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getParentValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				List<OptionModel> subOptions = CollectionFactory.newList();
				for (IndustryForCampaignEnum subIndustry : industry.getChildren()) {
					String subLabel = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, subIndustry);
					subOptions.add(new OptionModelImpl(subLabel, subIndustry));
				}
				optgroups.add(new OptionGroupModelImpl(label, false, subOptions));
			}
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getOtherValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				options.add(new OptionModelImpl(label, industry));
			}
		} else if (MAIN.equals(displayModel)) {
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getParentValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				options.add(new OptionModelImpl(label, industry));
			}
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getOtherValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				options.add(new OptionModelImpl(label, industry));
			}
		} else {
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getChildValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				options.add(new OptionModelImpl(label, industry));
			}
			for (IndustryForCampaignEnum industry : IndustryForCampaignEnum.getOtherValues()) {
				String label = TapestryInternalUtils.getLabelForEnum(resources.getContainerMessages(), prefix, industry);
				options.add(new OptionModelImpl(label, industry));
			}
		}
	}

	public ValueEncoder<IndustryForCampaignEnum> getEncoder() {
		return new EnumValueEncoder<IndustryForCampaignEnum>(IndustryForCampaignEnum.class);
	}

	public SelectModel getModel() {
		return new AbstractSelectModel() {
			public List<OptionGroupModel> getOptionGroups() {
				return optgroups;
			}

			public List<OptionModel> getOptions() {
				return options;
			}

		};
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public BlankOption getBlankOption() {
		return blankOption;
	}

	public void setBlankOption(BlankOption blankOption) {
		this.blankOption = blankOption;
	}

	public String getBlankLabel() {
		return blankLabel;
	}

	public void setBlankLabel(String blankLabel) {
		this.blankLabel = blankLabel;
	}
}
