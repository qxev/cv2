package cn.clickvalue.cv2.services.logic.admin;

import java.util.List;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.sv2.Client;

/**
 * @author larry.lang
 * admin审核affiliate、advertiser、campaign、commissionRule、landingPage、banner的业务
 */
public interface AuditingService {
    
    /**
     * @param campaignId 广告活动ID
     * @throws BusinessException
     * 批准广告活动上线
     */
    public Campaign passCampaignOnline(int campaignId) throws BusinessException;
    
    /**
     * @param campaignId 广告活动ID
     * @throws BusinessException
     * 批准广告活动下线
     */
    public void passCampaignOffline(int campaignId) throws BusinessException;

    /**
     * @param commissionRuleId
     * @throws BusinessException
     * 批准佣金规则
     * 
     * 如果已经有审核通过的同类型的佣金规则存在，则把原来的佣金规则的结束和当前佣金规则的开始时间改为当前时间。
     */
    public void passCommissionRule(int commissionRuleId) throws BusinessException;

    /**
     * @param landingPage
     * @throws BusinessException
     * 批准目标地址
     */
    public void passLandingPage(int landingPageId) throws BusinessException;

    /**
     * @param bannerId
     * @throws BusinessException
     * 批准广告
     */
    public void passBanner(int bannerId) throws BusinessException;
   
    /**
     * @param advertiserId 
     * @param semClientId 广告主在sem
     * @throws BusinessException
     * 批准广告主
     */
    public void passAdvertiser(int advertiserId,Client client) throws BusinessException;
    
    /**
     * @param affiliateId
     * @throws BusinessException
     * 批准网站主
     */
    public void passAffiliate(int affiliateId) throws BusinessException;
    
    /**
     * @param siteId
     * @throws BusinessException
     * 批准网站
     */
    public void passSite(int siteId) throws BusinessException;
    
    /**
     * @param campaignId 广告活动ID
     * @throws BusinessException
     * 拒绝广告活动上线
     */
    public void refuseCampaignOnline(int campaignId) throws BusinessException;
    
    /**
     * @param campaignId 广告活动ID
     * @throws BusinessException
     * 拒绝广告活动下线
     */
    public void refuseCampaignOffline(int campaignId) throws BusinessException;

    /**
     * @param commissionRuleId
     * @throws BusinessException
     * 拒绝佣金规则
     */
    public void refuseCommissionRule(int commissionRuleId) throws BusinessException;

    /**
     * @param landingPage
     * @throws BusinessException
     * 拒绝目标地址
     */
    public void refuseLandingPage(int landingPageId) throws BusinessException;

    /**
     * @param bannerId
     * @throws BusinessException
     * 拒绝广告
     */
    public void refuseBanner(int bannerId) throws BusinessException;

    /**
     * @param advertiserId
     * @throws BusinessException
     * 拒绝广告主
     */
    public void refuseAdvertiser(int advertiserId) throws BusinessException;
    
    /**
     * @param affiliateId
     * @throws BusinessException
     * 拒绝网站主
     */
    public void refuseAffiliate(int affiliateId) throws BusinessException;
    
    /**
     * @param siteId
     * @throws BusinessException
     * 拒绝网站
     */
    public void refuseSite(int siteId, String device) throws BusinessException;
    
    /**
     * @param accountId
     * @throws BusinessException
     * 批准银行账号
     */
    public void passAccount(int accountId) throws BusinessException;
    
    /**
     * @param accountId
     * @throws BusinessException
     * 拒绝银行账号
     */
    public void refuseAccount(int accountId, String reason) throws BusinessException; 
    
    /**
     * @return 所有SV系统的Client
     * @throws BusinessException
     * 查找SV系统中所有的client
     */
    public List<Client> getAllSEMClient() throws BusinessException;

    public void deleteClient(int clientId);

    /**
     * @param advertiserId
     * @return
     * @throws BusinessException
     * 当CV广告主在SEM系统中没有对应的client的时候，根据SV广告主在SEM中创建一个新的client
     */
    public Client createSVClientForAdvertiser(User advertiser) throws BusinessException;
    
    /**
     * @param campaignId
     * 
     * 更新campaign的cpa字段为当前佣金规则组
     */
    public void updateCampaignCpa(int campaignId);
}
