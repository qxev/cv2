package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.City;
import cn.clickvalue.cv2.model.Province;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserInfo;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.CityService;
import cn.clickvalue.cv2.services.logic.ProvinceService;
import cn.clickvalue.cv2.services.logic.UserService;

public class UserEditPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Component
	private Form userForm;

	@Inject
	@Service(value = "userService")
	private UserService userService;

	@Inject
	@Service(value = "provinceService")
	private ProvinceService provinceService;

	@Inject
	@Service(value = "cityService")
	private CityService cityService;

	@Persist("flash")
	private User user1;

	@ApplicationState
	private User user;

	private List<SelectModel> childSelectModels;

	private List<OptionModel> fatherSelectModel;

	void onPrepare() {

		// 对数据进行加工
		if (user1.getUserInfo().getCountry() == null || user1.getUserInfo().getCountry().length() == 0) {
			user1.getUserInfo().setCountry("110000");
		}

		if (user1.getUserInfo().getCity() == null || user1.getUserInfo().getCity().length() == 0) {
			user1.getUserInfo().setCity("110100");
		}
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void setupRender() {
		this.user1 = userService.get(user.getId());
		if (user1.getUserInfo() == null) {
			user1.setUserInfo(new UserInfo());
		}
	}

	void cleanupRender() {
		userForm.clearErrors();
	}

	Object onSuccess() {
		try {
			user1.setHasContact(1);
			userService.save(user1);
			user = user1;
			messagePage.setMessage(getMessages().get("action_success"));
			messagePage.setNextPage("advertiser/homepage");
			return messagePage;
		} catch (RuntimeException e) {
			this.userForm.recordError(getMessages().get("action_fail"));
			return this;
		}
	}

	Object onClicked() {
		return SiteListPage.class;
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (StringUtils.isBlank(user1.getUserInfo().getContact())) {
			userForm.recordError(getMessages().get("the_contact_person_name_cannot_be_spatial"));
		} else if (StringUtils.isBlank(user1.getUserInfo().getCompany())) {
			userForm.recordError(getMessages().get("the_corporate_name_cannot_be_spatial"));
		} else if (StringUtils.isBlank(user1.getNickName())) {
			userForm.recordError(getMessages().get("the_nickname_cannot_be_spatial"));
		} else if (user1.getNickName().length() >= 30) {
			userForm.recordError("昵称不能超过30个字符");
		} else if (userService.vaildateNickNameUnique(user1)) {
			userForm.recordError(getMessages().get("the_user's_nickname_cannot_duplicate"));
		} else if (user1.getUserInfo().getPostcode() != null) {
			if (!ValidateUtils.isZip(String.valueOf(user1.getUserInfo().getPostcode()))) {
				userForm.recordError(getMessages().get("postal_code_format_is_not_correct"));
			}
		} else if (!StringUtils.isBlank(user1.getUserInfo().getMobile())) {
			if (!ValidateUtils.isMobile(user1.getUserInfo().getMobile().trim())) {
				userForm.recordError(getMessages().get("cellphone_number_form_not_correct"));
			}
		}
	}

	/**
	 * 获取父类
	 * 
	 * @return SelectModel
	 */
	public SelectModel getFatherSelectModel() {
		if (fatherSelectModel == null) {
			fatherSelectModel = CollectionFactory.newList();
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);
					if ("day".equals(getMessages().get("day"))){
						fatherSelectModel.add(new OptionModelImpl(province.getProvinceEnglish(), String.valueOf(province
							.getProvinceId())));
					} else {
						fatherSelectModel.add(new OptionModelImpl(province.getProvince(), String.valueOf(province
								.getProvinceId())));
					}
				}
			}
		}
		return new SelectModelImpl(null, fatherSelectModel);
	}

	/**
	 * 获取子项
	 * 
	 * @return List
	 */
	public List<SelectModel> getChildSelectModels() {
		if (childSelectModels == null) {
			childSelectModels = new ArrayList<SelectModel>();
			List<Province> provinces = provinceService.findAll();
			if (provinces != null && provinces.size() > 0) {
				for (int i = 0; i < provinces.size(); i++) {
					Province province = provinces.get(i);

					List<City> cities = cityService.getCityByProvinceId(province.getProvinceId());

					if (cities != null && cities.size() > 0)
						childSelectModels.add(getSelectCityModle(cities));
				}
			}
		}
		return childSelectModels;
	}

	/**
	 * 城市
	 * 
	 * @param cityies
	 * @return SelectModel
	 */
	protected SelectModel getSelectCityModle(List<City> cityies) {
		return new SelectModelImpl(null, getOptionCityModels(cityies));
	}

	/**
	 * 城市
	 * 
	 * @param cityies
	 * @return List
	 */
	private List<OptionModel> getOptionCityModels(List<City> cityies) {
		List<OptionModel> optionModels = new ArrayList<OptionModel>();
		for (Iterator<City> it = cityies.iterator(); it.hasNext();) {
			City city = it.next();
			if ("day".equals(getMessages().get("day"))){
				optionModels.add(new OptionModelImpl(city.getCityEnglish(), String.valueOf(city.getCityId())));
			} else {
				optionModels.add(new OptionModelImpl(city.getCity(), String.valueOf(city.getCityId())));
			}
		}
		return optionModels;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}