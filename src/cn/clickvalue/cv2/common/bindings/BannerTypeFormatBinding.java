package cn.clickvalue.cv2.common.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;

/**
 * 广告类型 0.图片 1.文本 2.flash 3.html
 */
public class BannerTypeFormatBinding extends AbstractBinding {
    
    public static final int BANNER_TYPE_IMAGE = 0;
    public static final int BANNER_TYPE_TEXT = 1;
    public static final int BANNER_TYPE_FLASH = 2;
    public static final int BANNER_TYPE_HTML = 3;
    public static final int BANNER_TYPE_IFRAME = 4;

	private Binding keyBinding;
	private boolean invariant;
	private Messages messages;

	public BannerTypeFormatBinding(Location location, boolean invariant, Binding keyBinding, Messages messages) {
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
			Integer flag = Integer.parseInt((String)keyBinding.get());
			return formatBannerType(flag,messages);
		}
	}

	@Override
	public boolean isInvariant() {
		return this.invariant;
	}
	
	public String formatBannerType(Integer bannerType, Messages message) {
        String str = "";
        switch (bannerType) {
        case BANNER_TYPE_IMAGE:
            str = message.get("image_ad");
            break;
        case BANNER_TYPE_TEXT:
            str = message.get("text_ad");
            break;
        case BANNER_TYPE_FLASH:
            str = message.get("flash_ad");
            break;
        case BANNER_TYPE_HTML:
            str = message.get("html_ad");
            break;
	    case BANNER_TYPE_IFRAME:
	        str = message.get("iframe_ad");
	        break;
	    }
        return str;
    }

}
