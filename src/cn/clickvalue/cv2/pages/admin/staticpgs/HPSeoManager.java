package cn.clickvalue.cv2.pages.admin.staticpgs;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.grid.CollectionGridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.Enum.HPBlockEnum;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.admin.FileBrowser;
import cn.clickvalue.cv2.services.logic.HPBlockContentService;
import cn.clickvalue.cv2.services.logic.HPBlockService;

public class HPSeoManager extends BasePage {

	@Inject
	private HPBlockService hpBlockService;

	@Inject
	private HPBlockContentService hpBlockContentService;

	@Persist
	private HPBlock hpBlock;

	private List<HPBlockContent> hpBlockContents;

	private HPBlockContent hpBlockContent;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

	@InjectPage
	private FileBrowser fileBrowser;

	@InjectPage
	private MessagePage messagePage;

	void setupRender() {
		hpBlock = hpBlockService.findUniqueBy("name", HPBlockEnum.SEO.name());
		hpBlockContents = hpBlock.getHpBlockContents();
		for (int i = 0; i < hpBlockContents.size(); i++) {
			hpBlockContents.get(i).setSequence(i+1);
		}
	}

	void onPrepareForSubmit() {
		hpBlockContents = hpBlock.getHpBlockContents();
	}

	public BeanModel<HPBlockContent> getBeanModel() {
		BeanModel<HPBlockContent> beanModel = beanModelSource.create(HPBlockContent.class, true, componentResources);
		beanModel.get("sequence").label("序号").sortable(false);
		beanModel.get("cdata").label("内容").sortable(false);
		beanModel.get("url").label("目标地址").sortable(false);
		beanModel.get("image").label("图片").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.include("sequence", "cdata", "url");
		return beanModel;
	}

	public GridDataSource getDataSource() {
		GridDataSource dataSource = new CollectionGridDataSource(hpBlockContents);
		return dataSource;
	}

	Object onSubmit() {
		for (HPBlockContent hpBlockContent : hpBlockContents) {
			hpBlockContentService.save(hpBlockContent);
		}
		messagePage.setMessage("保存成功！");
		messagePage.setNextPage("admin/staticpgs/".concat(this.getClass().getSimpleName().toLowerCase()));
		return messagePage;
	}

	Object onManage() {
		fileBrowser.setBaseFolder(hpBlock.getFolder());
		fileBrowser.setCurrent(null);
		return fileBrowser;
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
