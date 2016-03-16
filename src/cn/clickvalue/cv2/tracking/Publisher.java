package cn.clickvalue.cv2.tracking;
import java.util.ArrayList;

import cn.clickvalue.cv2.common.constants.Constants;
/**
 * 网站主信息配置
 * @author jackie
 *
 */
public class Publisher {
    /**
     * 网站主获取广告代码时的语言选项
     * 0:中文
     * 1:英文
     */
    public int LANG = 0;
    /**
     * 网站主的账户ID
     */
    public int id = 0;
    /**
     * 网站主的网站集合
     */
    public ArrayList<Website> sites = new ArrayList<Website>();
    
    /**
     * 产生网站主获取的代码清单
     * @param adver 目标广告主
     * @return
     */
    public String getTrackingCode(Advertiser adver) {
        StringBuffer codes = new StringBuffer();
        StringBuffer memo = new StringBuffer();
        if(LANG==0) {
            codes.append("<b>使用说明：</b>拷贝方框内的代码，放到对应网站网页的HTML代码里即可，请在放置时确认代码的位置是想要展示广告的位置。您还可以根据代码说明调整对应参数值。");
            memo.append("<b>代码说明：</b><br/>");
        }else {
            codes.append("<b>Usage：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.");
            memo.append("<b>Notes：</b>");
        }
        codes.append("<br/><br/>");
        memo.append("<I>");
        if(LANG==0) {
            memo.append("<b>_cvOPM</b>：是否在新窗口打开广告页，值为“yes”时在新窗口打开，值为“no”时在当前窗口打开。");
            memo.append("<br/><b>_cvSUP</b>: 下线代号，由您自己决定，若有，请设置一个不多于12位的编号值（允许字母范围：“0”到“9”的数字，“a”到“z”的大小写英文字母），系统根据这个编号做相关明细统计报告");
            memo.append("<br/><b>_cvAAM</b>: 广告发布自动托管设置，值为“<b>0</b>”时代表关闭，值为“<b>1</b>”时代表启用（当所选广告活动过期或停止时，自动播放符合尺寸的其它活动的广告），值为“<b>2</b>”时代表立即强制为自动托管（此时会自动播放符合尺寸的其它广告）");
            memo.append("<br/><b>_cvPLS</b>: 设置广告显示的宽和高，可以手工调整");
            memo.append("<br/><b>_cvStyle</b>: 可选参数，使用CSS语法设置文字广告链接样式（字体、字号大小、颜色、是否加下划线），二维数组，数组内有4个数组元素，依次设置a:link、a:visited、a:hover和a:active");
        }else {
            memo.append("<b>_cvOPM</b>：Open URL at new window or not，value: <b>yes</b> or <b>no</b>。");
            memo.append("<br/><b>_cvSUP</b>: This is your sub-publisher unique ID, please set a value which length is no more than 12(Allowed chars:from 0 to 9, from a to z, or from A to Z), it use to generate detail report.");
            memo.append("<br/><b>_cvAAM</b>: Control the advertisement, value <b>0</b> is to close auto match, value <b>1</b> is to open auto match ads when current is out of date or stopped, value <b>2</b> is to auto match immediately.");
            memo.append("<br/><b>_cvPLS</b>: Set width and height.");
            memo.append("<br/><b>_cvStyle</b>: Optional，set text link style by using CSS(font family,size,color,underline),it have four element, to set a:link、a:visited、a:hover and a:active by turn.");
        } 
        memo.append("</I><br/><br/>");
        String memostr = memo.toString();
        Website ws = null;
        String codestr = null;
        codes.append("<table>\n");
        String ncode = null;
        StringBuffer index = new StringBuffer("<table>");
        index.append("<th align='left'>");
        if(LANG==0) {
            index.append("<a id='IDX'>广告代码索引</a>");
        }else {
            index.append("Code Index");
        }
        index.append("</th>");
        index.append("<tr><td>");
        for(int i=0;i<sites.size();i++) {
            ws = sites.get(i);
            codestr = ws.getTrackingCodes(this,adver);
            index.append("<a href='#S");
            index.append(i+1);
            index.append("'>");
            if(LANG==0) {
                index.append("网站 ");
            }else {
                index.append("Site ");
            }
            index.append(i+1);
            index.append("：");
            index.append(ws.site_name);
            index.append("(");
            index.append(ws.site_url);
            index.append(")");
            index.append("</a><br/>");
            codes.append("<tr><td><a id='S");
            codes.append(i+1);
            codes.append("' ");
            codes.append("href=\'");
            codes.append(ws.site_url);
            codes.append("' target='_blank'>");
            if(LANG==0) {
                codes.append("网站 ");
            }else codes.append("Site ");
            codes.append(i+1);
            codes.append(": ");
            codes.append(ws.site_name);
            codes.append("</a>");
            codes.append("&nbsp;&nbsp;&nbsp;&nbsp;<a href='#IDX'>Back to Top</a>");
            codes.append("</td></tr>\n");
            codes.append("<tr><td>\n");
            codes.append("<textarea cols='110' rows='18' readonly onmouseover='this.select();'>");
            ncode = codestr.replaceAll("<", "&lt;");
            ncode = ncode.replaceAll(">", "&gt;");
            codes.append(ncode);
            codes.append("</textarea>");
            codes.append("\n</td></tr>\n");
            codes.append("\n<tr><td>");
            codes.append(memostr);
            codes.append("\n</td></tr>");
        }
        index.append("</td></tr></table><br/><br/>");
        codes.append("</table>\n");
        String pstr = codes.toString();
        index.append(pstr);
        pstr = index.toString();
        return pstr;
    }
    
    /**
     * 产生网站主获取的广告托管代码清单
     * @param adver 目标广告主
     * @return
     */
    public String getTrackingCode() {
        StringBuffer codes = new StringBuffer();
        StringBuffer memo = new StringBuffer();
        if(LANG==0) {
            codes.append("<b>使用说明：</b>拷贝方框内的代码，放到对应网站网页的HTML代码里即可，请在放置时确认代码的位置是想要展示广告的位置。您还可以根据代码说明调整对应参数值。");
            memo.append("<b>代码说明：</b><br/>");
        }else {
            codes.append("<b>Usage：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.");
            memo.append("<b>Notes：</b>");
        }
        codes.append("<br/><br/>");
        memo.append("<I>");
        if(LANG==0) {
            memo.append("<b>_cvOPM</b>：是否在新窗口打开广告页，值为“yes”时在新窗口打开，值为“no”时在当前窗口打开。");
            memo.append("<br/><b>_cvSUP</b>: 下线代号，由您自己决定，若有，请设置一个不多于12位的编号值（允许字母范围：“0”到“9”的数字，“a”到“z”的大小写英文字母），系统根据这个编号做相关明细统计报告");
            memo.append("<br/><b>_cvPLS</b>: 设置广告显示的宽和高，可以手工调整");
            memo.append("<br/><b>_cvStyle</b>: 可选参数，使用CSS语法设置文字广告链接样式（字体、字号大小、颜色、是否加下划线），二维数组，数组内有4个数组元素，依次设置a:link、a:visited、a:hover和a:active");
        }else {
            memo.append("<b>_cvOPM</b>：Open URL at new window or not，value: <b>yes</b> or <b>no</b>。");
            memo.append("<br/><b>_cvSUP</b>: This is your sub-publisher unique ID, please set a value which length is no more than 12(Allowed chars:from 0 to 9, from a to z, or from A to Z), it use to generate detail report.");
            memo.append("<br/><b>_cvPLS</b>: Set width and height.");
            memo.append("<br/><b>_cvStyle</b>: Optional，set text link style by using CSS(font family,size,color,underline),it have four element, to set a:link、a:visited、a:hover and a:active by turn.");
        } 
        memo.append("</I><br/><br/>");
        String memostr = memo.toString();
        Website ws = null;
        String codestr = null;
        codes.append("<table>\n");
        String ncode = null;
//        StringBuffer index = new StringBuffer("<table>");
//        index.append("<th align='left'>");
//        if(LANG==0) {
//            index.append("<a id='IDX'>广告代码索引</a>");
//        }else {
//            index.append("Code Index");
//        }
//        index.append("</th>");
//        index.append("<tr><td>");
        for(int i=0;i<sites.size();i++) {
            ws = sites.get(i);
            codestr = ws.getTrackingCodes(this);
//            index.append("<a href='#S");
//            index.append(i+1);
//            index.append("'>");
//            if(LANG==0) {
//                index.append("网站 ");
//            }else {
//                index.append("Site ");
//            }
//            index.append(i+1);
//            index.append("：");
//            index.append(ws.site_name);
//            index.append("(");
//            index.append(ws.site_url);
//            index.append(")");
//            index.append("</a><br/>");
            codes.append("<tr><td><a id='S");
            codes.append(i+1);
            codes.append("' ");
            codes.append("href=\'");
            codes.append(ws.site_url);
            codes.append("' target='_blank'>");
            if(LANG==0) {
                codes.append("网站 ");
            }else codes.append("Site ");
            codes.append(i+1);
            codes.append(": ");
            codes.append(ws.site_name);
            codes.append("</a>");
//            codes.append("&nbsp;&nbsp;&nbsp;&nbsp;<a href='#IDX'>Back to Top</a>");
            codes.append("</td></tr>\n");
            codes.append("<tr><td>\n");
            codes.append("<textarea cols='110' rows='18' readonly onmouseover='this.select();'>");
            ncode = codestr.replaceAll("<", "&lt;");
            ncode = ncode.replaceAll(">", "&gt;");
            codes.append(ncode);
            codes.append("</textarea>");
            codes.append("\n</td></tr>\n");
            codes.append("\n<tr><td>");
            codes.append(memostr);
            codes.append("\n</td></tr>");
        }
//        index.append("</td></tr></table><br/><br/>");
        codes.append("</table>\n");
        String pstr = codes.toString();
//        index.append(pstr);
//        pstr = index.toString();
        return pstr;
    }
    
    
    /**
     * 网站主获得广告的链接
     * @param adver 目标广告主
     * @return
     */
    public String getTrackingLink(Advertiser adver) {
        StringBuffer codes = new StringBuffer();
        if(LANG==0) {
            codes.append("<b>使用说明：</b>将方框内的网页地址设置为链接即可");
        }else {
            codes.append("<b>Usage：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.");
        }
        codes.append("<br/><br/>");
        Website ws = null;
        String codestr = null;
        codes.append("<table>\n");
        String ncode = null;
        StringBuffer index = new StringBuffer("<table>");
        index.append("<th align='left'>");
        if(LANG==0) {
            index.append("<a id='IDX'>广告代码索引</a>");
        }else {
            index.append("Code Index");
        }
        index.append("</th>");
        index.append("<tr><td>");
        for(int i=0;i<sites.size();i++) {
            ws = sites.get(i);
            codestr = ws.getTrackingLink(this,adver);
            index.append("<a href='#S");
            index.append(i+1);
            index.append("'>");
            if(LANG==0) {
                index.append("网站 ");
            }else {
                index.append("Site ");
            }
            index.append(i+1);
            index.append("：");
            index.append(ws.site_name);
            index.append("(");
            index.append(ws.site_url);
            index.append(")");
            index.append("</a><br/>");
            codes.append("<tr><td><a id='S");
            codes.append(i+1);
            codes.append("' ");
            codes.append("href=\'");
            codes.append(ws.site_url);
            codes.append("' target='_blank'>");
            if(LANG==0) {
                codes.append("网站 ");
            }else codes.append("Site ");
            codes.append(i+1);
            codes.append(": ");
            codes.append(ws.site_name);
            codes.append("</a>");
            codes.append("&nbsp;&nbsp;&nbsp;&nbsp;<a href='#IDX'>Back to Top</a>");
            codes.append("</td></tr>\n");
            codes.append("<tr><td>\n");
            codes.append("<textarea cols='110' rows='2' readonly onmouseover='this.select();'>");
            ncode = codestr.replaceAll("<", "&lt;");
            ncode = ncode.replaceAll(">", "&gt;");
            codes.append(ncode);
            codes.append("</textarea>");
            codes.append("\n</td></tr>\n");
            codes.append("\n<tr><td>");
            codes.append("\n</td></tr>");
        }
        index.append("</td></tr></table><br/><br/>");
        codes.append("</table>\n");
        String pstr = codes.toString();
        index.append(pstr);
        pstr = index.toString();
        return pstr;
    }
    
    /**
     * 网站主获得自定义广告链接
     * @param adver 目标广告主
     * @return
     */
    public String getCustomLink(Advertiser adver) {
        StringBuffer codes = new StringBuffer();
        Website ws = null;
        String codestr = null;
        codes.append("<table>\n");
        codes.append("<th align='left'>");
        if(LANG==0) {
        	codes.append("<a id='IDX'>广告代码索引</a>");
        }else {
        	codes.append("Code Index");
        }
        codes.append("</th>");
        if(LANG==0) {
            codes.append("<tr><td><b>使用说明：</b>拷贝方框内的代码，放到对应网站网页的HTML代码里即可，请在放置时确认代码的位置是想要展示广告的位置。</td></tr>");
        }else {
            codes.append("<tr><td><b>Usage：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.</td></tr>");
        }
        
        String ncode = null;
        StringBuffer index = new StringBuffer();
        for(int i=0;i<sites.size();i++) {
        	ws = sites.get(i);
            codes.append("<tr><td>");
        	if(LANG==0) {
                codes.append("网站 ");
            } else {
        		codes.append("Site ");
            }
            codes.append(i+1);
            codes.append(":<br/>");
            if(LANG==0) {
                codes.append("<b>预览：</b>");
            }else {
                codes.append("<b>Preview：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.");
            }
            codes.append("<br/><tr align='center'><td>");
            codes.append("<a href='");
            codes.append(ws.getTrackingLinkNoescape(this,adver,"1"));
            codes.append("' ");
            if (ws.ad.targetWindow==0){
            	codes.append("target=\"_blank\"");
            }
            codes.append(">");
            if (("1".equals(ws.ad.type))||("5".equals(ws.ad.type))) {
            	codes.append("<IMG SRC='");
            	codes.append(ws.ad.pictureUrl);
            	codes.append("' border='0'>");
            } else if ("2".equals(ws.ad.type)){
            	codes.append(ws.ad.text);
            }
            codes.append("</a></tr></td>");
            codes.append("</td></tr>\n");
            codes.append("<tr><td>\n");
            if(LANG==0) {
            	codes.append("原搞文本：");
            } else {
            	codes.append("Text Code:");
            }
            codes.append("<textarea cols='110' rows='4' readonly onmouseover='this.select();'><a href='");
            codestr = ws.getTrackingLinkNoescape(this,adver,"0");
            ncode = codestr.replaceAll("<", "&lt;");
            ncode = ncode.replaceAll(">", "&gt;");
            codes.append(ncode);
            codes.append("' ");
            if (ws.ad.targetWindow==0){
            	codes.append("target=\"_blank\"");
            }
            codes.append(">");
            if ("1".equals(ws.ad.type)) {
            	codes.append("<IMG SRC='");
            	codes.append(ws.ad.pictureUrl);
            	codes.append("' border='0'>");
            } else if ("2".equals(ws.ad.type)){
            	codes.append(ws.ad.text);
            }
            codes.append("</a></textarea>");
            codes.append("\n</td></tr>\n");
            codes.append("\n<tr><td>");
            codes.append("\n</td></tr>");
        }
        codes.append("</table>\n");
        String pstr = codes.toString();
        index.append(pstr);
        pstr = index.toString();
        return pstr;
    }
    
    
    /**
     * 网站主获得Iframe广告链接
     * @param adver 目标广告主
     * @return
     */
    public String getIframeLink(Advertiser adver) {
        StringBuffer codes = new StringBuffer();
        Website ws = null;
        String codestr = null;
        codes.append("<table>\n");
        codes.append("<th align='left'>");
        if(LANG==0) {
        	codes.append("<a id='IDX'>广告代码索引</a>");
        }else {
        	codes.append("Code Index");
        }
        codes.append("</th>");
        if(LANG==0) {
            codes.append("<tr><td><b>使用说明：</b>拷贝方框内的代码，放到对应网站网页的HTML代码里即可，请在放置时确认代码的位置是想要展示广告的位置。</td></tr>");
        }else {
            codes.append("<tr><td><b>Usage：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.</td></tr>");
        }
        
        String ncode = null;
        StringBuffer index = new StringBuffer();
        for(int i=0;i<sites.size();i++) {
        	ws = sites.get(i);
            codes.append("<tr><td>");
        	if(LANG==0) {
                codes.append("网站 ");
            } else {
        		codes.append("Site ");
            }
            codes.append(i+1);
            codes.append(":<br/>");
            if(LANG==0) {
                codes.append("<b>预览：</b>");
            }else {
                codes.append("<b>Preview：</b>Copy the codes，and put it into your website page source codes，please make sure that it is the right place that you want to show the advertisement。you also can change the parameter value by notes.");
            }
            codes.append("<br/><tr align='center'><td>");
            codes.append("<a href='");
            codes.append(ws.site_url);
            codes.append("?head=");
            codes.append(ws.ad.headUrl);
            codes.append("&height=");
            codes.append(ws.ad.headHeight);
            codes.append("&content=");
            codes.append(ws.getTrackingLinkNoescape(this,adver,"1"));
            codes.append("' ");
            if (ws.ad.targetWindow==0){
            	codes.append("target=\"_blank\"");
            }
            codes.append(">");
            if ("0".equals(ws.ad.type)) {
            	codes.append("<IMG SRC='");
            	codes.append(Constants.WEBSITE);
            	codes.append(ws.ad.pictureUrl);
            	codes.append("' border='0'>");
            } else if ("1".equals(ws.ad.type)){
            	codes.append(ws.ad.text);
            }
            codes.append("</a></tr></td>");
            codes.append("</td></tr>\n");
            codes.append("<tr><td>\n");
            if(LANG==0) {
            	codes.append("原搞文本：");
            } else {
            	codes.append("Text Code:");
            }
            codes.append("<textarea cols='110' rows='4' readonly onmouseover='this.select();'><a href='");
            codes.append(ws.site_url);
            codes.append("?head=");
            codes.append(ws.ad.headUrl);
            codes.append("&height=");
            codes.append(ws.ad.headHeight);
            codes.append("&content=");
            codes.append(ws.getTrackingLinkNoescape(this,adver,"1"));
//            ncode = codestr.replaceAll("<", "&lt;");
 //           ncode = ncode.replaceAll(">", "&gt;");
  //          codes.append(ncode);
            codes.append("' ");
            if (ws.ad.targetWindow==0){
            	codes.append("target=\"_blank\"");
            }
            codes.append(">");
            if ("0".equals(ws.ad.type)) {
            	codes.append("<IMG SRC='");
            	codes.append(Constants.WEBSITE);
            	codes.append(ws.ad.pictureUrl);
            	codes.append("' border='0'>");
            } else if ("1".equals(ws.ad.type)){
            	codes.append(ws.ad.text);
            }
            codes.append("</a></textarea>");
            codes.append("\n</td></tr>\n");
            codes.append("\n<tr><td>");
            codes.append("\n</td></tr>");
        }
        codes.append("</table>\n");
        String pstr = codes.toString();
        index.append(pstr);
        pstr = index.toString();
        return pstr;
    }
}
