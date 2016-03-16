package cn.clickvalue.cv2.pages.admin.staticpgs;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.grid.CollectionGridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.services.logic.HPBlockContentService;
import cn.clickvalue.cv2.services.logic.HPBlockService;

public class HomePageManager extends BasePage {

	@Inject
	private HPBlockService hpBlockService;

	@Inject
	private HPBlockContentService hpBlockContentService;

	private List<HPBlock> hpBlocks;

	private HPBlock hpBlock;

	private List<HPBlockContent> hpBlockContents;

	private HPBlockContent hpBlockContent;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

	void setupRender() {
		hpBlocks = hpBlockService.findAll();
	}

	void onPrepareForSubmit() {
		hpBlockContents = new ArrayList<HPBlockContent>();
	}

	public BeanModel<HPBlockContent> getBeanModel() {
		BeanModel<HPBlockContent> beanModel = beanModelSource.create(HPBlockContent.class, true, componentResources);
		beanModel.get("cdata").label("内容").sortable(false);
		beanModel.get("url").label("目标地址").sortable(false);
		beanModel.get("image").label("图片").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.include("cdata", "url", "image", "description");
		return beanModel;
	}

	public GridDataSource getDataSource() {
		GridDataSource dataSource = new CollectionGridDataSource(hpBlock.getHpBlockContents());
		return dataSource;
	}

	Object onSubmit() {
		for (HPBlockContent hpBlockContent : hpBlockContents) {
			hpBlockContentService.save(hpBlockContent);
		}
		return this;
	}

	public List<HPBlock> getHpBlocks() {
		return hpBlocks;
	}

	public void setHpBlocks(List<HPBlock> hpBlocks) {
		this.hpBlocks = hpBlocks;
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
		if (hpBlockContents != null) {
			hpBlockContents.add(hpBlockContent);
		}
	}
}
