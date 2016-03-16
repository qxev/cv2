package cn.clickvalue.cv2.components;

import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

import cn.clickvalue.cv2.services.SupportedLocales;

public class LocalSwitch {

	@Inject
	private PersistentLocale persistentLocale;

//	@Inject
//	private SupportedLocales supportedLocales;
//
//	public void setLocale(Locale selectedLocale) {
//		persistentLocale.set(selectedLocale);
//	}

	void onActionFromChinese() {
		persistentLocale.set(Locale.CHINESE);
	}

	void onActionFromEnglish() {
		persistentLocale.set(Locale.ENGLISH);
	}

}