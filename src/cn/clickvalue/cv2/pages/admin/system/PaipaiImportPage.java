package cn.clickvalue.cv2.pages.admin.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.FileUploadUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.PartnerId;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.PartnerIdService;

public class PaipaiImportPage extends BasePage {

	@Property
	private UploadedFile file;

	@Component(id = "importForm")
	private Form form;

	@Inject
	private PartnerIdService partnerIdService;

	@Persist
	@Property
	private Campaign formCampaign;

	@Property
	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Campaign> campaigns = new ArrayList<Campaign>();

	@Inject
	private CampaignService campaignService;

	void setupRender() {
		campaigns = campaignService.findHavingPartnerId();
	}

	void onValidateForm() {
		if (formCampaign == null) {
			addError(form, "请选择广告活动", false);
		} else if (formCampaign.getPartnerType() == null || formCampaign.getPartnerType() == 0) {
			addError(form, "广告活动不支持导入自定义subid", false);
		}
		if (file == null) {
			addError(form, "请选择上传的excel", false);
		}
	}

	Object onSubmit() {
		if (form.isValid()) {
			try {
				String[] types = { ".xls" };
				String fileName = FileUploadUtils.upload(file, "proxy", types);
				List<PartnerId> partnerIds = ExcelUtils.readExl(PartnerId.class, "excel/partnerId.properties", fileName);
				List<String> tempOurIds = new ArrayList<String>();
				List<String> tempPartnerIds = new ArrayList<String>();
				List<String> existedOurIds = partnerIdService.findOurIdsByType(formCampaign.getId());
				List<String> existedPartnerIds = partnerIdService.findPartnerIdsByType(formCampaign.getId());

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < partnerIds.size(); i++) {
					PartnerId partnerId = partnerIds.get(i);
					partnerId.setType(formCampaign.getId());
					int indexOfOurId = tempOurIds.indexOf(partnerId.getOurId());
					int indexOfPartnerId = tempPartnerIds.indexOf(partnerId.getPartnerId());
					tempOurIds.add(partnerId.getOurId());
					tempPartnerIds.add(partnerId.getPartnerId());
					if (StringUtils.isEmpty(partnerId.getOurId())) {
						sb.append(String.format("第%d行:ourId为空", i + 1));
					} else if (!StringUtils.isNumeric(partnerId.getOurId())) {
						sb.append(String.format("第%d行:ourId(%s)，不是一个数字<br />", i + 1, partnerId.getOurId()));
					} else if (indexOfOurId > -1) {
						sb.append(String.format("第%d行与第%d行ourId重复，不能重复关联<br />", i + 1, indexOfOurId + 2));
					} else if (indexOfPartnerId > -1) {
						sb.append(String.format("第%d行与第%d行partnerId重复，不能重复关联<br />", i + 1, indexOfPartnerId + 2));
					} else if (existedOurIds.indexOf(partnerId.getOurId()) > -1) {
						sb.append(String.format("第%d行:已为此ourId(%s)关联过partnerId，不能重复关联<br />", i + 1, partnerId.getOurId()));
					} else if (existedPartnerIds.indexOf(partnerId.getPartnerId()) > -1) {
						sb.append(String.format("第%d行:已为此partnerId(%s)关联过ourId，不能重复关联<br />", i + 1, partnerId.getPartnerId()));
					}
				}
				if (StringUtils.isNotEmpty(sb.toString())) {
					addError(sb.toString(), false);
				} else {
					partnerIdService.batchSave(partnerIds);
					addInfo("导入成功", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				addError("导入失败....", false);
			}
		}
		return this;
	}

}
