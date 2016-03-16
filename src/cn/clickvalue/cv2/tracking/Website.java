package cn.clickvalue.cv2.tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Cached;
import org.hibernate.annotations.Cache;

/**
 * 网站信息配置
 * 
 * @author jackie
 * 
 */
public class Website {
	/**
	 * 网站主网站ID
	 */
	public int id = 0;
	/**
	 * 网站名称
	 */
	public String site_name = "";
	/**
	 * 网站网址，用以提示网站主如何将代码与网站对应起来
	 */
	public String site_url = "";
	/**
	 * 是否启用自动托管
	 */
	public boolean enableAutoAd = false;
	/**
	 * 当前要播放的广告
	 */
	public Ad ad = new Ad();
	/**
	 * 用以更新affilliateAdvertise表中的track_code字段
	 */
	private String trackCodeId = "";

	/**
	 * 参数
	 */
	public String parameters = "";

	/**
	 * campaign类型
	 * 
	 * campaignType =
	 * 1时，说明广告主要求下线ID由广告主定，所以我们传给广告主的下线ID为partnerId表中type=campaign.id and
	 * ourId=site.id时对应的partnerId的值
	 * 
	 */
	public int campaignType = 0;

	/**
	 * 合作伙伴的ID
	 */
	public String partnerId = "";

	Pattern PARAM_PATTERN = Pattern.compile("\\{(.*?)\\}");

	public int[] categoryIds;

	public String getTrackCodeId() {
		return trackCodeId;
	}

	/**
	 * 网站主获得广告的链接
	 * 
	 * @param cpub
	 * @param adver
	 * @return
	 */
	public String getTrackingLink(Publisher cpub, Advertiser adver) {
		StringBuffer tcs = new StringBuffer();
		tcs.append("http://cc.dmclick.cn/cgi-bin/tc?_cv_code=");
		int start = tcs.length();
		int csid = 0;
		if (adver.semId == 0) {
			// 先做联盟业务，未开始做SEM业务
			tcs.append(Advertiser.IDMARK);
			tcs.append(adver.id);
			csid = adver.id;
		} else {
			// 已经做了SEM业务，广告主ID应依照SEM系统中的ID
			tcs.append(adver.semId);
			csid = adver.semId;
		}
		tcs.append("-");
		tcs.append(ad.campaign_id);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(ad.aid);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(cpub.id);
		tcs.append("-");
		tcs.append(this.id);
		tcs.append("-");
		tcs.append(ad.affId);// 当拿的代码版本是托管时，值为0
		int end = tcs.length();
		trackCodeId = tcs.substring(start, end);

		tcs.append("&trk_cid=");
		tcs.append(csid);
		tcs.append("&_to_page=");
		String url = handleParameter();
		// 判断有没有锚点，锚点要放在url的最后
		int dumy = url.indexOf("#");
		if (dumy > -1) {
			String link = url.substring(0, dumy);
			String anchor = url.substring(dumy);
			String s0 = (link.indexOf("?") > -1) ? link.concat("&_cv_code=").concat(trackCodeId) : link.concat("?_cv_code=").concat(
					trackCodeId);
			url = s0.concat(anchor);
		} else {
			url = (url.indexOf("?") > -1) ? url.concat("&_cv_code=").concat(trackCodeId) : url.concat("?_cv_code=").concat(trackCodeId);
		}
		// /**
		// * 特殊接口特殊处理！
		// */
		// if (ad.campaign_id == 171) { // 热血三国
		// url = url.replaceAll("\\{_cv_code.5\\}", String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 137) { // 推游戏
		// url =
		// url.concat("-&gameid=hslj&uid=dwlm&webid=").concat(String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 41) { // vancl
		// url = url.concat("-&SourceSunInfo=").concat(trackCodeId);
		// } else if (ad.campaign_id == 126) { // 罗莱家纺
		// url =
		// url.concat("-&sid=0&webid=1177&uid=").concat(String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 85) { // 当当
		// url = url.concat("-&from=P-265942").concat(String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 86) { // 卓越
		// url =
		// url.concat("-&source=clickvalue_").concat(String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 100) { // 7天连锁
		// url =
		// url.concat("-&rid=1005&sid=6969&crid=").concat(String.valueOf(ad.affId));
		// } else if (ad.campaign_id == 24) { // DHC
		// url = url.concat("-");
		// } else if (ad.campaign_id == 174) {
		// url = url.replaceAll("\\{_cv_code\\}", trackCodeId);
		// } else if (parameters != null && (!"".equals(parameters))) {
		// String par = parameters.replaceAll("\\|", "=");
		// par = par.replaceAll(",", "&");
		// par = par.replaceAll("\\{_cv_code.5\\}", String.valueOf(ad.affId));
		// par = par.replaceAll("\\{_cv_code\\}", trackCodeId);
		// url = url.concat("&").concat(par);
		// }
		url = Coder.escape(url);
		url = Coder.escape(url);
		tcs.append(url);
		return tcs.toString();
	}

	// 这个方法是iframe广告专用的，先不改了，还要建表，麻烦。
	// 而且，还有是否需要编码之分，还没有理解为什么有这样的需求。
	public String getTrackingLinkNoescape(Publisher cpub, Advertiser adver, String noescape) {
		StringBuffer tcs = new StringBuffer();
		tcs.append("http://cc.dmclick.cn/cgi-bin/tc?_cv_code=");
		int start = tcs.length();
		int csid = 0;
		if (adver.semId == 0) {
			// 先做联盟业务，未开始做SEM业务
			tcs.append(Advertiser.IDMARK);
			tcs.append(adver.id);
			csid = adver.id;
		} else {
			// 已经做了SEM业务，广告主ID应依照SEM系统中的ID
			tcs.append(adver.semId);
			csid = adver.semId;
		}
		tcs.append("-");
		tcs.append(ad.campaign_id);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(ad.aid);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(cpub.id);
		tcs.append("-");
		tcs.append(this.id);
		tcs.append("-");
		tcs.append(ad.affId);// 当拿的代码版本是托管时，值为0
		int end = tcs.length();
		trackCodeId = tcs.substring(start, end);

		tcs.append("&trk_cid=");
		tcs.append(csid);
		tcs.append("&_to_page=");
		String url = handleParameter();
		// 判断有没有锚点，锚点要放在url的最后
		int dumy = ad.link.indexOf("#");
		if (dumy > -1) {
			String link = ad.link.substring(0, dumy);
			String anchor = ad.link.substring(dumy);
			String s0 = (link.indexOf("?") > -1) ? link.concat("&_cv_code=").concat(trackCodeId) : link.concat("?_cv_code=").concat(
					trackCodeId);
			url = s0.concat(anchor);
		} else {
			url = (ad.link.indexOf("?") > -1) ? ad.link.concat("&_cv_code=").concat(trackCodeId) : ad.link.concat("?_cv_code=").concat(
					trackCodeId);
		}
		/**
		 * 特殊接口特殊处理！
		 */
		if (ad.campaign_id == 137) { // 推游戏
			url = url.concat("-%2526gameid=hslj%2526uid=dwlm%2526webid=").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 41) { // vancl
			url = "http://www.vancl.com/WebSource/WebSource.aspx?Url=" + url;
			url = url.concat("-%2526Source=darwin%2526SourceSunInfo=").concat(trackCodeId);
		} else if (ad.campaign_id == 126) { // 罗莱家纺
			url = "http://www.lovo.cn/jieshou.asp?linkurl=" + url;
			url = url.concat("-%2526sid=0%2526webid=1177%2526uid=").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 85) { // 当当
			url = "http://union.dangdang.com/transfer/transfer.aspx?backurl=" + url;
			url = url.concat("-%2526from%253DP-265942").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 86) { // 卓越
			url = url.concat("-%2526source=clickvalue_").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 100) { // 7天连锁
			url = url.concat("-%2526rid=1005%2526sid=6969%2526crid=").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 24) { // DHC
			url = url.concat("-%2526un_cps=%2526ad_cps=L0000009");
		} else if (ad.campaign_id == 151) { // 乐淘族
			url = url.concat("-%2526source=dawen%2526subid=").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 150) { // 乐陶
			url = url.concat("-%2526source=dawen%2526subid=").concat(String.valueOf(ad.affId));
		} else if (ad.campaign_id == 171) { // 热血三过
			url = url.replaceAll("\\{_cv_code.5\\}", String.valueOf(ad.affId));
		} else if (ad.campaign_id == 174) {
			url = url.replaceAll("\\{_cv_code\\}", trackCodeId);
		} else {
			if (parameters != null && (!"".equals(parameters))) {
				String par = parameters.replaceAll("\\|", "=");
				par = par.replaceAll(",", "%2526");
				par = par.replaceAll("\\{_cv_code.5\\}", String.valueOf(ad.affId));
				par = par.replaceAll("\\{_cv_code\\}", trackCodeId);
				url = url.concat("%2526").concat(par);
			}
			if ("0".equals(noescape)) {
				url = Coder.escape(url);
				url = Coder.escape(url);
			}
		}
		tcs.append(url);
		String tcstr = tcs.toString();
		return tcstr;
	}

	private String handleParameter() {
		String url = ad.link;
		// campaign级别的参数加到url后面
		if (StringUtils.isNotEmpty(parameters)) {
			String par = parameters.replaceAll("\\|=?", "=");
			par = par.replaceAll(",", "&");
			url = url.concat(url.indexOf("?") > -1 ? "&" : "?").concat(par);
		}

		// 收集可以用参数资源，_cv_code：核心跟踪标识，tp：广告主要自定义下线ID，对应着我们实际的一个站点ID
		Map<String, String> map = new HashMap<String, String>();
		String[] trackCodes = trackCodeId.split("-");
		map.put("_cv_code", trackCodeId);
		for (int i = 0; i < trackCodes.length; i++) {
			map.put("_cv_code.".concat(String.valueOf(i)), trackCodes[i]);
		}
		map.put("tp", partnerId);

		// 把url中的需要替换的参数值替换成资源中对应的值
		Matcher matcher = PARAM_PATTERN.matcher(url);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String key = matcher.group(1);
			String m = map.get(key);
			if (m != null) {
				matcher.appendReplacement(sb, m);
			}
		}
		url = matcher.appendTail(sb).toString();
		return url;
	}

	/**
	 * 生成网站要放置的广告代码
	 * 
	 * @return 广告代码
	 */
	public String getTrackingCodes(Publisher cpub, Advertiser adver) {
		StringBuffer tcs = new StringBuffer();
		tcs.append("<!--Darwin Marketing Tracking System Ver2.0 Begin-->\n");
		tcs.append("<script type=\"text/javascript\">\n");
		// 设置相关变量
		tcs.append("var _cvUID=\"");
		int start = tcs.length();
		if (adver.semId == 0) {
			// 先做联盟业务，未开始做SEM业务
			tcs.append(Advertiser.IDMARK);
			tcs.append(adver.id);
		} else {
			// 已经做了SEM业务，广告主ID应依照SEM系统中的ID
			tcs.append(adver.semId);
		}
		tcs.append("-");
		tcs.append(ad.campaign_id);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(ad.aid);// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(cpub.id);
		tcs.append("-");
		tcs.append(this.id);
		tcs.append("-");
		tcs.append(ad.affId);// 当拿的代码版本是托管时，值为0
		int end = tcs.length();
		trackCodeId = tcs.substring(start, end);
		tcs.append("\";//ACTPSV\n");
		if (campaignType == 1) {
			tcs.append("var _cvPID=\"");
			tcs.append(partnerId);
			tcs.append("\";\n");
		}
		tcs.append("var _cvSUP=\"\";\n");// 当与SEM业务整合时，需要将这个值同_cvUID的值作为_cv_code传到Landing
		// Page地址上。
		// 是否启用自动托管，当广告过期后，立即自动切换到托管模式，否则播放默认的公益广告
		tcs.append("var _cvOPM=\"yes\";\n");
		tcs.append("var _cvAAM=");
		int aam = enableAutoAd ? 1 : 0;
		tcs.append(aam);// 广告托管设置，0：禁止托管；1：启用自动托管；2：强制立即托管
		tcs.append(";\n");
		// 设置广告展示的宽和高
		tcs.append("var _cvPLS=[");
		tcs.append(ad.width);
		tcs.append(",");
		tcs.append(ad.height);
		tcs.append("];\n");
		// 样式定义
		if (ad.type.equals("1")) {
			tcs.append("var _cvStyle=[['Arial,宋体',12,'blue','N'],['',12,'gray',''],['',12,'red','Y'],['',12,'red','Y']];");
			if (cpub.LANG == 0) {
				tcs.append("//使用CSS语法设置文字广告链接样式\n");
			} else {
				tcs.append("//Set text link style by using CSS\n");
			}
		}
		// 引用主控JS
		tcs.append("var _cvDWLP=location.protocol.indexOf(\"https\")>-1?\"https:\":\"http:\";\n");
		tcs.append("var _cvDWDN=\"//cs.dmclick.cn/\";\n");
		tcs.append("var _cvDWURL = _cvDWLP+_cvDWDN+\"dmm.js\";\n");
		tcs.append("if(typeof _DW_C_P == \"undefined\" || _DW_C_P == null || _DW_C_P == \"undefined\"){");
		tcs.append("var _DW_C_P = new Object();");
		tcs.append("document.write(unescape(\"%3Cscript src='\" + _cvDWURL + \"' type='text/javascript'%3E%3C/script%3E\"));");
		tcs.append("}\n");
		tcs.append("try{_DW_C_PLAY();}catch(err){}\n");
		tcs.append("</script>\n");
		tcs.append("<!--Darwin Marketing Tracking System Ver2.0 End-->\n");
		String tcstr = tcs.toString();

		return tcstr;
	}

	/**
	 * 生成网站要放置的托管广告代码
	 * 
	 * @return 广告代码
	 */
	public String getTrackingCodes(Publisher cpub) {
		StringBuffer tcs = new StringBuffer();
		tcs.append("<!--Darwin Marketing Tracking System Ver2.0 Begin-->\n");
		tcs.append("<script type=\"text/javascript\">\n");
		// 设置相关变量
		tcs.append("var _cvUID=\"");
		int start = tcs.length();
		tcs.append("0");// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append("0");// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append("0");// 当拿的代码版本是托管时，值为0
		tcs.append("-");
		tcs.append(cpub.id);
		tcs.append("-");
		tcs.append(this.id);
		tcs.append("-");
		tcs.append("0");// 当拿的代码版本是托管时，值为0
		int end = tcs.length();
		trackCodeId = tcs.substring(start, end);
		tcs.append("\";\n");
		tcs.append("var _cvSUP=\"\";\n");// 当与SEM业务整合时，需要将这个值同_cvUID的值作为_cv_code传到Landing
		// Page地址上。
		// 是否启用自动托管，当广告过期后，立即自动切换到托管模式，否则播放默认的公益广告
		tcs.append("var _cvOPM=\"yes\";\n");
		tcs.append("var _cvAAM=2;\n");
		if (this.categoryIds != null && this.categoryIds.length > 0) {
			tcs.append("var _cvSC=\"");
			for (int i = 0; i < this.categoryIds.length; i++) {
				if (i != 0) {
					tcs.append("-");
				}
				tcs.append(this.categoryIds[i]);
			}
			tcs.append("\";\r\n");
		}
		// 设置广告展示的宽和高
		tcs.append("var _cvPLS=[");
		tcs.append(ad.width);
		tcs.append(",");
		tcs.append(ad.height);
		tcs.append("];\n");
		// 样式定义
		// if (ad.type.equals("1")) {
		// tcs.append("var _cvStyle=[['Arial,宋体',12,'blue','N'],['',12,'gray',''],['',12,'red','Y'],['',12,'red','Y']];");
		// if (cpub.LANG == 0) {
		// tcs.append("//使用CSS语法设置文字广告链接样式\n");
		// } else {
		// tcs.append("//Set text link style by using CSS\n");
		// }
		// }
		// 引用主控JS
		tcs.append("var _cvDWLP=location.protocol.indexOf(\"https\")>-1?\"https:\":\"http:\";\n");
		tcs.append("var _cvDWDN=\"//cs.dmclick.cn/\";\n");
		tcs.append("var _cvDWURL = _cvDWLP+_cvDWDN+\"dmm.js\";\n");
		tcs.append("if(typeof _DW_C_P == \"undefined\" || _DW_C_P == null || _DW_C_P == \"undefined\"){");
		tcs.append("var _DW_C_P = new Object();");
		tcs.append("document.write(unescape(\"%3Cscript src='\" + _cvDWURL + \"' type='text/javascript'%3E%3C/script%3E\"));");
		tcs.append("}\n");
		tcs.append("try{_DW_C_PLAY();}catch(err){}\n");
		tcs.append("</script>\n");
		tcs.append("<!--Darwin Marketing Tracking System Ver2.0 End-->\n");
		String tcstr = tcs.toString();

		return tcstr;
	}
}
