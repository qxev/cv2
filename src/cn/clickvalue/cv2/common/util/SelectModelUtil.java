package cn.clickvalue.cv2.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.Bank;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.City;
import cn.clickvalue.cv2.model.MatchTask;
import cn.clickvalue.cv2.model.Province;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.logic.BankService;
import cn.clickvalue.cv2.services.logic.CityService;
import cn.clickvalue.cv2.services.logic.ProvinceService;

/**
 * spring管理到的单例 类 所以不需要显示的去static
 */
public class SelectModelUtil {

	private CityService cityService;

	private BankService bankService;

	private ProvinceService provinceService;

	public static List<OptionModel> fatherSelectModel = CollectionFactory.newList();

	public static List<SelectModel> childSelectModels = CollectionFactory.newList();

	public static List<OptionModel> fatherSelectNameModel = CollectionFactory.newList();

	public static List<SelectModel> childSelectNameModels = CollectionFactory.newList();

	public static List<OptionModel> bankNamesModel = CollectionFactory.newList();

	/**
	 * 匹配
	 * 
	 * @param list
	 * @return
	 */
	public SelectModel getMatchTasksModel(List<MatchTask> list) {
		List<OptionModel> matchTasks = CollectionFactory.newList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				MatchTask matchTask = list.get(i);
				matchTasks.add(new OptionModelImpl(matchTask.getTaskName(), matchTask.getId()));
			}
		}
		return new SelectModelImpl(null, matchTasks);
	}

	public SelectModel getCampaignModel(List<Campaign> campaigns) {
		List<OptionModel> campaignList = CollectionFactory.newList();

		if (campaigns != null && campaigns.size() > 0) {
			for (int i = 0; i < campaigns.size(); i++) {
				Campaign campaign = campaigns.get(i);
				campaignList.add(new OptionModelImpl(campaign.getName(), campaign.getId()));
			}
		}
		return new SelectModelImpl(null, campaignList);
	}

	public SelectModel getAffCategoryModel(List<AffiliateCategory> affiliateCategoryList) {
		List<OptionModel> affCategorySelectModel = CollectionFactory.newList();
		if (affiliateCategoryList != null && affiliateCategoryList.size() > 0) {
			for (int i = 0; i < affiliateCategoryList.size(); i++) {
				AffiliateCategory affiliateCategory = affiliateCategoryList.get(i);
				affCategorySelectModel.add(new OptionModelImpl(affiliateCategory.getName(), affiliateCategory.getId()));
			}
		}
		return new SelectModelImpl(null, affCategorySelectModel);
	}

	public SelectModel getSiteModel(List<Site> siteList) {
		List<OptionModel> siteSelectModel = CollectionFactory.newList();

		if (siteList != null && siteList.size() > 0) {
			for (int i = 0; i < siteList.size(); i++) {
				Site site = siteList.get(i);
				siteSelectModel.add(new OptionModelImpl(site.getName(), site.getId()));
			}
		}
		return new SelectModelImpl(null, siteSelectModel);
	}

	/**
	 * 银行的名称model
	 * 
	 * @return SelectModel
	 */
	public SelectModel getBankNamesModel() {
		if (bankNamesModel.size() <= 0) {
			List<Bank> banks = bankService.findAll();
			if (banks != null && banks.size() > 0) {
				for (int i = 0; i < banks.size(); i++) {
					Bank bank = banks.get(i);
					bankNamesModel.add(new OptionModelImpl(bank.getName(), bank.getName()));
				}
			}
		}
		return new SelectModelImpl(null, bankNamesModel);
	}

	/**
	 * 获取省名称
	 * 
	 * @return SelectModel
	 */
	public SelectModel getProvinceNameSelectModel() {
		if (fatherSelectNameModel.size() <= 0) {
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);
					fatherSelectNameModel.add(new OptionModelImpl(province.getProvince(), province.getProvince()));
				}
			}
		}
		return new SelectModelImpl(null, fatherSelectNameModel);
	}

	/**
	 * 获取城市 Name
	 * 
	 * @return
	 */
	public List<SelectModel> getCityNameSelectModels() {
		if (childSelectNameModels.size() <= 0) {
			childSelectNameModels = new ArrayList<SelectModel>();
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);

					List<City> cities = cityService.getCityByProvinceId(province.getProvinceId());
					if (cities != null && cities.size() > 0) {
						childSelectNameModels.add(getSelectCityNameModle(cities));
					}
				}
			}
		}
		return childSelectNameModels;
	}

	/**
	 * 银行账户下拉操作框
	 * 
	 * @return SelectModel
	 */
	public SelectModel getAccountOperateModel(Account account, Messages messages) {
		List<OptionModel> operateModel = CollectionFactory.newList();

		operateModel.add(new OptionModelImpl(messages.get("view"), "/affiliate/AffiliateBankAccontViewPage/" + account.getId()));

		operateModel.add(new OptionModelImpl(messages.get("delete"), "/affiliate/AffiliateBankAccontViewPage/" + account.getId()));
		if (account.getVerified() == 0) {
			operateModel.add(new OptionModelImpl(messages.get("apply"), "/affiliate/AffiliateBankAccontViewPage/" + account.getId()));
			operateModel.add(new OptionModelImpl(messages.get("edit"), "/affiliate/BankAccountEditPage/" + account.getId()));
		}
		return new SelectModelImpl(null, operateModel);
	}

	/**
	 * 支付宝下拉操作框
	 * 
	 * @return SelectModel
	 */
	public SelectModel getAlipayOperateModel(Account account, Messages messages) {
		List<OptionModel> operateModel = CollectionFactory.newList();
		operateModel.add(new OptionModelImpl(messages.get("delete"), "/affiliate/AffiliateBankAccontViewPage/" + account.getId()));
		if (account.getVerified() == 0) {
			operateModel.add(new OptionModelImpl(messages.get("apply"), "/affiliate/AffiliateBankAccontViewPage/" + account.getId()));
			operateModel.add(new OptionModelImpl(messages.get("edit"), "/affiliate/alipayaccounteditpage/" + account.getId()));
		}
		return new SelectModelImpl(null, operateModel);
	}

	/**
	 * 下拉操作框 Campaign
	 * 
	 * @return SelectModel
	 */
	public SelectModel getOperateModel(Campaign campaign, Messages messages) {
		List<OptionModel> operateModel = CollectionFactory.newList();
		operateModel.add(new OptionModelImpl(messages.get("view"), "/affiliate/AffiliateCampaignViewPage/" + campaign.getId()));
		// 去除 免费申请的站点
		if (campaign.getAffiliateVerified() != null && campaign.getAffiliateVerified() != 1) {
			operateModel.add(new OptionModelImpl(messages.get("apply"), "/affiliate/applysitelistpage/" + campaign.getId()));
		}
		return new SelectModelImpl(null, operateModel);
	}

	/**
	 * 下拉操作框 Site
	 * 
	 * @return SelectModel
	 */
	public SelectModel getSiteOperateModel(Site site, Messages messages) {
		List<OptionModel> operateModel = CollectionFactory.newList();
		operateModel.add(new OptionModelImpl(messages.get("view"), "/affiliate/SiteViewPage/" + site.getId()));

		// 小于1的时候有 提交,删除功能
		if (site.getVerified() < 1) {
			operateModel.add(new OptionModelImpl(messages.get("apply"), "/affiliate/SiteViewPage/" + site.getId()));
			operateModel.add(new OptionModelImpl(messages.get("edit"), "/affiliate/SiteEditPage/" + site.getId()));
			operateModel.add(new OptionModelImpl(messages.get("delete"), "/affiliate/SiteViewPage/" + site.getId()));
		}
		return new SelectModelImpl(null, operateModel);
	}

	/**
	 * 获取省
	 * 
	 * @return SelectModel
	 */
	public SelectModel getProvinceSelectModel() {
		if (fatherSelectModel.size() <= 0) {
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);
					fatherSelectModel.add(new OptionModelImpl(province.getProvince(), String.valueOf(province.getProvinceId())));
				}
			}
		}
		return new SelectModelImpl(null, fatherSelectModel);
	}

	/**
	 * 获取城市
	 * 
	 * @return
	 */
	public List<SelectModel> getCitySelectModels() {
		if (childSelectModels.size() <= 0) {
			childSelectModels = new ArrayList<SelectModel>();
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);

					List<City> cities = cityService.getCityByProvinceId(province.getProvinceId());
					if (cities != null && cities.size() > 0) {
						childSelectModels.add(getSelectCityModle(cities));
					}
				}
			}
		}
		return childSelectModels;
	}

	/**
	 * 下拉操作框 0=未申请,1=待审核,2=审核通过,3=已拒绝
	 * 
	 * @return SelectModel
	 */
	public static SelectModel getStateModel(Messages messages) {
		List<OptionModel> stateModel = CollectionFactory.newList();
		stateModel = CollectionFactory.newList();
		stateModel.add(new OptionModelImpl(messages.get("not_submitted"), "0"));
		stateModel.add(new OptionModelImpl(messages.get("pending_approval"), "1"));
		stateModel.add(new OptionModelImpl(messages.get("approved"), "2"));
		stateModel.add(new OptionModelImpl(messages.get("refused"), "3"));
		return new SelectModelImpl(null, stateModel);
	}

	/**
	 * 城市
	 * 
	 * @param cityies
	 * @return SelectModel
	 */
	private SelectModel getSelectCityModle(List<City> cityies) {
		return new SelectModelImpl(null, getOptionCityModels(cityies));
	}

	/**
	 * 城市名称
	 * 
	 * @param cityies
	 * @return SelectModel
	 */
	private SelectModel getSelectCityNameModle(List<City> cityies) {
		return new SelectModelImpl(null, getOptionCityNameModels(cityies));
	}

	/**
	 * 城市id
	 * 
	 * @param cityies
	 * @return List
	 */
	private List<OptionModel> getOptionCityModels(List<City> cityies) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		for (Iterator<City> it = cityies.iterator(); it.hasNext();) {
			City city = it.next();
			optionModels.add(new OptionModelImpl(city.getCity(), String.valueOf(city.getCityId())));
		}
		return optionModels;
	}

	/**
	 * 城市名称
	 * 
	 * @param cityies
	 * @return List
	 */
	private List<OptionModel> getOptionCityNameModels(List<City> cityies) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		for (Iterator<City> it = cityies.iterator(); it.hasNext();) {
			City city = it.next();
			optionModels.add(new OptionModelImpl(city.getCity(), city.getCity()));
		}
		return optionModels;
	}

	public CityService getCityService() {
		return cityService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public ProvinceService getProvinceService() {
		return provinceService;
	}

	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
	}

	public BankService getBankService() {
		return bankService;
	}

	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}
}
