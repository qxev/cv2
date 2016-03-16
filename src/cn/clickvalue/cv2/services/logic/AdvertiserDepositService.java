package cn.clickvalue.cv2.services.logic;

import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.AdvertiserDeposit;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AdvertiserDepositService extends BaseService<AdvertiserDeposit> {
	
	private AdvertiserAccountService advertiserAccountService;
	
	public void deposit(AdvertiserDeposit advertiserDeposit,AdvertiserAccount advertiserAccount){
		advertiserAccountService.merge(advertiserAccount);
		this.merge(advertiserDeposit);
	}

	public void setAdvertiserAccountService(AdvertiserAccountService advertiserAccountService) {
		this.advertiserAccountService = advertiserAccountService;
	}

}
