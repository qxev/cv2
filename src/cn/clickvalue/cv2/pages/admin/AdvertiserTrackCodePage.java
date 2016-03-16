package cn.clickvalue.cv2.pages.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.tracking.Tracker;

public class AdvertiserTrackCodePage extends BasePage {

    private SemClient semClient;

    private LabelValueModel code;

    @Inject
    private SemClientService semClientService;

    @Inject
    private UserService userService;

    /**
     * 0:CPC 1:CPL 2:CPS
     */
    private List<LabelValueModel> codes = new ArrayList<LabelValueModel>();

    private Map<Integer, String> ruleTypes = new HashMap<Integer, String>();
    private Map<Integer, Integer> stepTypes = new HashMap<Integer, Integer>();

    void pageLoaded() {
        ruleTypes.put(0, "CPC");
        ruleTypes.put(1, "CPL");
        ruleTypes.put(2, "CPS");
        
        stepTypes.put(0, Tracker.TRACK_FOR_CPC);
        stepTypes.put(1, Tracker.TRACK_FOR_CPL);
        stepTypes.put(2, Tracker.TRACK_FOR_CPS);
    }
    
    void afterRender() {
        ruleTypes.clear();
        stepTypes.clear();
        codes.clear();
    }

    void onActivate(int advertiserId) {
        
        User user = userService.get(advertiserId);
        semClient = semClientService.findUniqueBy("advertiser.id", advertiserId);
        if (semClient != null) {
            int trackUserId = semClient.getClientId();
            Tracker track = new Tracker(user.getLanguage(), trackUserId, 0);
            for (int i = 0; i < ruleTypes.size(); i++) {
                String str = track.getAdvertiserTrackingCodes(i,stepTypes.get(i),getMessages());
                LabelValueModel trackCode = new LabelValueModel();
                trackCode.setLabel(ruleTypes.get(i));
                trackCode.setValue(str);
                codes.add(trackCode);
            }
        }
    }

    public LabelValueModel getCode() {
        return code;
    }

    public void setCode(LabelValueModel code) {
        this.code = code;
    }

    public List<LabelValueModel> getCodes() {
        return codes;
    }

    public void setCodes(List<LabelValueModel> codes) {
        this.codes = codes;
    }
}
