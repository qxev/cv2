package cn.clickvalue.cv2.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.Enum.HPBlockEnum;
import cn.clickvalue.cv2.common.util.StringUtils;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.services.logic.HPBlockService;

public class HPBlockList {

	@Property
	private List<HPBlock> hpBlocks;

	@Property
	private HPBlock hpBlock;

	@Inject
	private HPBlockService hpBlockService;

	@Inject
	private ComponentResources resources;

	void setupRender() {
		hpBlocks = new ArrayList<HPBlock>();
		List<HPBlock> result = hpBlockService.findAll();
		for (HPBlock hpBlock : result) {
			try {
				HPBlockEnum.valueOf(hpBlock.getName());
				hpBlocks.add(hpBlock);
			} catch (IllegalArgumentException e) {
			}
		}
	}

	public String getHref() {
		String name = StringUtils.toHumpcase("HP_".concat(hpBlock.getName()));
		String pageName = "admin/staticpgs/".concat(name).concat("Manager");
		return resources.createPageLink(pageName, false, new Object[] {}).toURI();

	}
}
