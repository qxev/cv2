package cn.clickvalue.cv2.components;

import java.util.Locale;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

public class Border {
    @Inject
    private PersistentLocale persistentLocale;

    @Inject
    private Locale currentLocale;

    @Persist
    private String localeLabel;

    /**
     * we have defined that the default prefix for this parameter is
     * literal,otherwiseit "literal",otherwiseit otherwise it will be "prop".
     * 
     */
    @Parameter(defaultPrefix = "literal")
    private String pageTitle = " Tapestry5 example ";

    @Inject
    private ComponentResources resources;

    public String getPageTitle() {
        return pageTitle;
    }

    public String getLocaleLabel() {
        if (localeLabel == null) {
            if (currentLocale.equals(Locale.GERMAN)) {
                localeLabel = new Locale("en").getDisplayName(Locale.ENGLISH);
            } else {
                localeLabel = new Locale("de").getDisplayName(Locale.GERMAN);
            }
        }
        return localeLabel;
    }

    @OnEvent(component = "switchlocale")
    void changeLocale() {
        localeLabel = currentLocale.getDisplayName(currentLocale);
        if (currentLocale.equals(Locale.GERMAN)) {
            persistentLocale.set(Locale.ENGLISH);
        } else {
            persistentLocale.set(Locale.GERMAN);
        }
    }

//    private String getPageName() {
////        Component page = resources.getContainer();
////        return page.getClass().getName();
//    }

//    public boolean isNotStart() {
//        return !getPageName().equals(Start.class.getName());
//    }

//    public boolean isRegistration() {
////        return getPageName().equals(Registration.class.getName());
//    }
//
//    public boolean isShowAll() {
//        return getPageName().equals(ShowAll.class.getName());
//    }
//
//    public boolean isAddCelebrity() {
//        return getPageName().equals(AddCelebrity.class.getName());
//    }

}