package cn.clickvalue.cv2.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;

import cn.clickvalue.cv2.model.Banner;

public class BannerContent {

	private static final String DEFAULT_WIDTH = "200";
	private static final String DEFAULT_HEIGHT = "120";

	@Parameter(required = true)
	private Banner banner;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private boolean isExact = true;

	@Parameter
	private String width;

	@Parameter
	private String height;

	private String bannerWidth;

	private String bannerHeight;

	void setupRender() {
		if (isExact) {
			bannerWidth = String.valueOf(banner.getWidth());
			bannerHeight = String.valueOf(banner.getHeight());
		} else {
			zoom();
		}
	}

	private void zoom() {
		Double w = NumberUtils.toDouble(DEFAULT_WIDTH);
		Double h = NumberUtils.toDouble(DEFAULT_HEIGHT);

		Double bw = NumberUtils.toDouble(String.valueOf(banner.getWidth()));
		Double bh = NumberUtils.toDouble(String.valueOf(banner.getHeight()));

		if (bw <= w && bh <= h) {
			bannerWidth = String.valueOf(banner.getWidth());
			bannerHeight = String.valueOf(banner.getHeight());
			return;
		}

		Double itemh, itemw;
		itemh = w * bh / bw;
		itemw = h * bw / bh;
		if (itemh > h) {
			bannerHeight = DEFAULT_HEIGHT;
			bannerWidth = itemw.toString();
		} else if (itemw > w) {
			bannerHeight = itemh.toString();
			bannerWidth = DEFAULT_WIDTH;
		}
	}

	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public String getWidth() {
		if (StringUtils.isNotEmpty(width)) {
			return width;
		} else if (banner.getWidth() != null && banner.getWidth() != 0) {
			return banner.getWidth().toString();
		} else {
			return DEFAULT_WIDTH;
		}
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		if (StringUtils.isNotEmpty(height)) {
			return height;
		} else if (banner.getHeight() != null && banner.getHeight() != 0) {
			return banner.getHeight().toString();
		} else {
			return DEFAULT_HEIGHT;
		}
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getBannerWidth() {
		return bannerWidth;
	}

	public void setBannerWidth(String bannerWidth) {
		this.bannerWidth = bannerWidth;
	}

	public String getBannerHeight() {
		return bannerHeight;
	}

	public void setBannerHeight(String bannerHeight) {
		this.bannerHeight = bannerHeight;
	}

	public boolean isExact() {
		return isExact;
	}

	public void setExact(boolean isExact) {
		this.isExact = isExact;
	}
}
