package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.sv2.Client;
import cn.clickvalue.cv2.services.dao.jdbc.SEMClientDao;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class SVClientListPage extends BasePage {
    
    @Persist
    private int advertiserId;
    
    @Property
    private User advertiser;
    
    private List<Client> clients;
    
    @Persist
    private int selectClientId;
    
    private Client client;
    
    @Inject
    private AuditingService auditingService;
    
    @Inject
    private SEMClientDao semClientDao;
    
    @InjectPage
    private AdvertiserVerifiedConfirmPage advertiserVerifiedConfirmPage;
    
    @Inject
    private UserService userService;
    
    @Inject
    private SemClientService semClientService;
    
    private Object redirectPage;
    
    void setupRender() {
        clients = semClientDao.getAllSEMClient();
        
        //如果SV系统中某个client已经跟CV系统用户关联过，则从clients中删掉，在页面列表中不显示改SVclient
        List<Integer> clientIds = semClientService.findAllClientIds();
        for(int i=clients.size();i>0;i--){
        	if(clientIds.contains(clients.get(i-1).getId())){
        		clients.remove(i-1);
        	}
        }
    }
    
    void onPrepare(){
    	advertiser = userService.get(advertiserId);
    }
    
    @OnEvent(value="selected",component="submit")
    void submit() {
        Client svClient = null;
        Map<String,Object> conditions = new HashMap<String,Object>();
    	conditions.put("id", selectClientId);
    	List<Client> clients = semClientDao.findClientByCondition(conditions);
    	if(clients != null && clients.size()>0){
    		svClient = clients.get(0);
    	}else{
    		svClient = auditingService.createSVClientForAdvertiser(advertiser);
    	}
        advertiserVerifiedConfirmPage.setAdvertiser(advertiser);
        advertiserVerifiedConfirmPage.setClient(svClient);
        setRedirectPage(advertiserVerifiedConfirmPage);
    }
    
    @OnEvent(value="selected",component="findNone")
    void findNone(){
    	Client svClient = auditingService.createSVClientForAdvertiser(advertiser);
    	advertiserVerifiedConfirmPage.setAdvertiser(advertiser);
        advertiserVerifiedConfirmPage.setClient(svClient);
        setRedirectPage(advertiserVerifiedConfirmPage);
    }
    
    @OnEvent(value="selected",component="cancel")
    void cancel() {
        setRedirectPage("admin/usermanage/clientlistpage");
    }
    
    Object onSubmit() {
        return redirectPage;
    }
    
    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }


    public AuditingService getAuditingService() {
        return auditingService;
    }


    public void setAuditingService(AuditingService auditingService) {
        this.auditingService = auditingService;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getSelectClientId() {
        return selectClientId;
    }

    public void setSelectClientId(int selectClientId) {
        this.selectClientId = selectClientId;
    }
    
    public Object getRedirectPage() {
        return redirectPage;
    }

    public void setRedirectPage(Object redirectPage) {
        this.redirectPage = redirectPage;
    }
    
}