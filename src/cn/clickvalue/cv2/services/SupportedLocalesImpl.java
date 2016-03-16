package cn.clickvalue.cv2.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class SupportedLocalesImpl implements SupportedLocales {

	private String supportedLocales;

	public SupportedLocalesImpl(@Inject @Symbol("tapestry.supported-locales") String locales) {
		supportedLocales = locales;
	}

	public String getSupportedLocales() {
		return supportedLocales;
	}
}