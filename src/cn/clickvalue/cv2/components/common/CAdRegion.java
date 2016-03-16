package cn.clickvalue.cv2.components.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CAdRegion {

	@Inject
	private Messages messages;

	@Property
	private int index;

	@Persist
	private String region;

	private final String[] regions = { "北京市", "上海市", "天津市", "重庆市", "广东省", "福建省", "浙江省", "江苏省", "山东省", "辽宁省", "江西省", "四川省", "陕西省", "湖北省",
			"河南省", "河北省", "山西省", "内蒙古", "吉林省", "黑龙江", "安徽省", "湖南省", "广西区", "海南省", "云南省", "贵州省", "西藏区", "甘肃省", "宁夏区", "青海省", "新疆区", "香港区",
			"澳门区", "台湾省", "所有地区" };

	private final String[] regions1 = { "Beijing", "Shanghai", "Tianjin", "Chongqing", "Guangdong", "Fujian", "Zhejiang", "Jiangsu",
			"Shandong", "Liaoning", "Jiangxi", "Sichuan", "Shanxi", "Hubei", "Henan", "Hebei", "Shaanxi", "Inner Mongolia", "Jilin",
			"Heilongjiang", "Anhui", "Hunan", "Guangxi", "Hainan", "Yunnan", "Guizhou", "Tibet", "Gansu", "Ningxia", "Qinghai", "Xinjiang",
			"Hongkong", "Macao", "Taiwan", "All Region" };

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private ComponentResources resources;

	@Persist
	private Set<String> selectedRegions;

	@SetupRender
	public void setupRender() {
		if (selectedRegions == null) {
			selectedRegions = new HashSet<String>();
		}
	}

	public boolean getBreakLine() {
		return (this.index + 1) % 5 == 0 ? true : false;
	}

	public String getRegion() {
		return region;
	}

	public String[] getRegions() {
		if ("day".equals(messages.get("day"))) {
			return regions1;
		} else {
			return regions;
		}
	}

	public Set<String> getSelectedRegions() {
		return selectedRegions;
	}

	public boolean isSelected() {
		return selectedRegions.contains(regions[index]);
	}

	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Add region to the selected set if selected. #
	 */
	public void setSelected(boolean selected) {

		// 因为提交回来的region可能是中文也可能是英文，会出问题，所以这里用index只去中文regions里面拿值，无论页面展示是中文还是英文，提交回来都是中文
		String region = regions[index];
		if (selected) {
			if (region != null)
				selectedRegions.add(region);
		} else {
			selectedRegions.remove(region);
		}
	}

	public void setSelectedRegions(Set<String> selectedRegions) {
		this.selectedRegions = selectedRegions;
	}

	/**
	 * @return
	 * 
	 *         把选中的regions转成以";"分割的字符串返回，如果是全选则返回"所有地区"；
	 */
	public String getStrSelectedRegions() {
		if (selectedRegions.size() == 34) {
			return "所有地区";
		} else {
			return StringUtils.join(selectedRegions, ";");
		}
	}

	/**
	 * @param strSelectedRegions
	 * 
	 *            把传入的字符串转成set，然后设置给selectedRegions
	 */
	public void setStrSelectedRegions(String strSelectedRegions) {
		Set<String> sets = new HashSet<String>();
		if (strSelectedRegions.equals("所有地区")) {
			sets.addAll(Arrays.asList(regions));
		} else {
			for (String region : strSelectedRegions.split(";")) {
				sets.add(region);
			}
		}
		setSelectedRegions(sets);
	}

	public String[] getRegions1() {
		return regions1;
	}

	public String getCheckboxId() {
		return renderSupport.allocateClientId(resources.getId());
	}
}
