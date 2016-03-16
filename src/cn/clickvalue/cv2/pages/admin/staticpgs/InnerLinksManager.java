package cn.clickvalue.cv2.pages.admin.staticpgs;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.DefaultPrimaryKeyEncoder;

import cn.clickvalue.cv2.common.Enum.HPBlockEnum;
import cn.clickvalue.cv2.common.util.UUIDUtil;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.HPBlockContentService;
import cn.clickvalue.cv2.services.logic.HPBlockService;
import cn.clickvalue.cv2.services.logic.StaticPageService;

public class InnerLinksManager {

	@Inject
	private HPBlockService hpBlockService;
	
	@Inject
	private StaticPageService staticPageService;

	@Inject
	private HPBlockContentService hpBlockContentService;

	@Persist
	private HPBlock hpBlock;

	private HPBlockContent hpBlockContent;

	@Property
	@Persist
	private DefaultPrimaryKeyEncoder<String, HPBlockContent> hpBlockContentEncoder;

	@Inject
	private ComponentResources componentResources;

	@InjectPage
	private MessagePage messagePage;

	void setupRender() {
		hpBlockContentEncoder = new DefaultPrimaryKeyEncoder<String, HPBlockContent>();
		hpBlock = hpBlockService.findUniqueBy("name", HPBlockEnum.INNER_LINKS.name());
		List<HPBlockContent> hpBlockContents = hpBlock.getHpBlockContents();
		for (int i = 0; i < hpBlockContents.size(); i++) {
			HPBlockContent hpbc = hpBlockContents.get(i);
			hpbc.setSequence(i + 1);
			hpBlockContentEncoder.add(UUIDUtil.getUUID(), hpbc);
		}
	}

	Object onAddRow() {
		HPBlockContent h = new HPBlockContent();
		h.setSequence(hpBlockContentEncoder.getValues().size() + 1);
		h.setHpBlock(hpBlock);
		hpBlockContentService.save(h);
		hpBlockContentEncoder.add(UUIDUtil.getUUID(), h);
		return h;
	}

	void onRemoveRow(HPBlockContent hpBlockContent) {
		hpBlockContentEncoder.setDeleted(true);
	}

	Object onSubmit() {
		for (HPBlockContent hpBlockContent : hpBlockContentEncoder.getAllValues()) {
			hpBlockContentEncoder.toKey(hpBlockContent);
			if (!hpBlockContentEncoder.isDeleted()) {
				hpBlockContentService.save(hpBlockContent);
			} else {
				if (hpBlockContent.getId() != null) {
					hpBlockContentService.delete(hpBlockContent);
				}
			}
		}
		
		staticPageService.buildLinks();
		messagePage.setMessage("保存成功！");
		messagePage.setNextPage("admin/staticpgs/".concat(this.getClass().getSimpleName().toLowerCase()));
		return messagePage;
	}

	public HPBlock getHpBlock() {
		return hpBlock;
	}

	public void setHpBlock(HPBlock hpBlock) {
		this.hpBlock = hpBlock;
	}

	public HPBlockContent getHpBlockContent() {
		return hpBlockContent;
	}

	public void setHpBlockContent(HPBlockContent hpBlockContent) {
		this.hpBlockContent = hpBlockContent;
	}

	public String getManageLink() {
		return componentResources.createActionLink("manage", false, new Object[] {}).toURI();
	}

	public boolean isManageAble() {
		return (hpBlock.getPermission() & 1) > 0;
	}
}
