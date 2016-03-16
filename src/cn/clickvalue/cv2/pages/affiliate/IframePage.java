package cn.clickvalue.cv2.pages.affiliate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.tapestry.commons.response.AttachmentStreamResponse;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.tracking.TrackerModel;

import com.darwinmarketing.configs.ConfigReader;

public class IframePage extends BasePage {

	@Persist
	private List<TrackerModel> tracks;
	
	@Persist
	private Campaign campaign;

	@Persist
	private boolean isCheck;
	
	@Persist
	private String text;

	@Persist
	private String bannerUrl;

	@Persist
	private String iframeStyle;

	@Property
	private String headUrl;
	
	@Property
	private String siteUrl;

	@Property
	private String headHeight;
	
	@Component(id = "form")
	private Form form;
	
	@InjectPage
	private IframeCodePage iframeCodePage;
	
	void onPrepare() {
    }

    void cleanupRender() {
    }

	/**
	 * 下载IFrame
	 * @return
	 * @throws IOException 
	 */
	public StreamResponse onDownload() throws IOException {
		FileInputStream is = new FileInputStream(ConfigReader.appRootDir+"public/iframe.html");
		return new AttachmentStreamResponse(is,"iframe","html");
	}
    
    /**
     * 表单的验证
     */
    void onValidateForm() {
    	if (headUrl == null || headUrl.length() == 0) {
			form.recordError("请选择头文件");
		} else if (siteUrl == null || siteUrl.length() == 0) {
			form.recordError("请选择IFrame目标地址");
		}
    }

    Object onSuccessFrom() {
    	iframeCodePage.setTracks(tracks);
    	iframeCodePage.setCampaign(campaign);
    	iframeCodePage.setText(text);
    	iframeCodePage.setIframeStyle(iframeStyle);
    	iframeCodePage.setHeadUrl(headUrl);
    	iframeCodePage.setSiteUrl(siteUrl);
    	iframeCodePage.setHeadHeight(headHeight);
    	iframeCodePage.setBannerUrl(bannerUrl);
		return iframeCodePage;
    }

	public List<TrackerModel> getTracks() {
		return tracks;
	}

	public void setTracks(List<TrackerModel> tracks) {
		this.tracks = tracks;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIframeStyle() {
		return iframeStyle;
	}

	public void setIframeStyle(String iframeStyle) {
		this.iframeStyle = iframeStyle;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

}