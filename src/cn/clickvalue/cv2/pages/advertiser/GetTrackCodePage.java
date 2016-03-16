package cn.clickvalue.cv2.pages.advertiser;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.tracking.Tracker;

public class GetTrackCodePage extends BasePage {

	private SemClient semClient;

	private static final String ERROR_MESSAGE = "您还未通过审核！";
	
	@Property
	private String cpcCode = ERROR_MESSAGE;

	@Property
	private String cplCode = ERROR_MESSAGE;

	@Property
	private String cpsCode = ERROR_MESSAGE;

	@Inject
	private SemClientService semClientService;

	@SetupRender
	public void setupRender() {
		semClient = semClientService.findUniqueBy("advertiser.id", this
				.getClientSession().getId());
		if (semClient != null) {
			int trackUserId = semClient.getClientId();
			Tracker track = new Tracker(this.getClientSession().getLanguage(),
					trackUserId, 0);
			cpcCode = track.getAdvertiserTrackingCodes(0, Tracker.TRACK_FOR_CPC,getMessages());
			cplCode = track.getAdvertiserTrackingCodes(1, Tracker.TRACK_FOR_CPL,getMessages());
			cpsCode = track.getAdvertiserTrackingCodes(2, Tracker.TRACK_FOR_CPS,getMessages());
			
			//这个为landingPage代码，在landingPage页面中不能再记一次_rsStepTp=0，这样会重复，所以替换为零
			cpcCode = cpcCode.replace("var _rsStepTp=100;", "var _rsStepTp=0;");
		}
	}
}