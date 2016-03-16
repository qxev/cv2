package cn.clickvalue.cv2.services.logic;

import java.math.BigDecimal;

import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AdvertiserAccountService extends BaseService<AdvertiserAccount> {

    /**
     * 根据UserId获取AdvertiserAccount
     * 
     * @param userId
     * @return AdvertiserAccount
     */
    public AdvertiserAccount findAdvertiserAccountByUserId(Integer userId) {
        AdvertiserAccount advertiserAccount = this.findUniqueBy("advertiserId",
                userId);
        return advertiserAccount;
    }

    /**
     * @return
     *  创建默认的AdvertiserAccount对象
     */
    public AdvertiserAccount createAdvertiserAccount() {
        AdvertiserAccount advertiserAccount = new AdvertiserAccount();
        advertiserAccount.setTotalexpense(new BigDecimal(0));
        advertiserAccount.setTotalIncome(new BigDecimal(0));
        return advertiserAccount;
    }
    
}
