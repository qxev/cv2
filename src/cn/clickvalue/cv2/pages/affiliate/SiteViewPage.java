package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SiteViewPage extends BasePage {

	@Persist
	@Property
	private Site site;

	@Property
	private Integer id;

	@Component
	private Form form;

	@Inject
	private SiteService siteService;

	@InjectPage
	private MessagePage messagePage;

	@SetupRender
	public void setupRender() {
		site = siteService.get(id);
	}

	void onActivate(Integer id) {
		this.id = id;
	}

	Integer onPassivate() {
		return id;
	}
	
	public String getAffiliateCategory(){
    	StringBuilder sb = new StringBuilder();
    	List<AffiliateCategorySite> affiliateCategorySites = site.getAffiliateCategorySites();
    	for(AffiliateCategorySite affiliateCategorySite : affiliateCategorySites){
    		sb.append(affiliateCategorySite.getAffiliateCategory().getName());
    		sb.append(" ");
    	}
    	return sb.toString();
    }

	Object onClicked() {
		return SiteListPage.class;
	}

	Object onAudit() {
		site.setVerified(Integer.valueOf(1));
		siteService.save(site);
		messagePage.setNextPage("affiliate/SiteListPage");
		messagePage.setMessage(getMessages().get("The_application_is_successful"));
		return messagePage;
	}

	Object onSuccess() {
		site.setDeleted(Integer.valueOf(1));
		siteService.save(site);
		messagePage.setNextPage("affiliate/SiteListPage");
		messagePage.setMessage(getMessages().get("Deletes_successful"));
		return messagePage;
	}

	public boolean isDelete() {
		return site.getVerified() == 0 && site.getDeleted() == 0 ? true : false;
	}

	public boolean isAudit() {
		return site.getVerified() == 0 && site.getDeleted() == 0 ? true : false;
	}
}