package cn.clickvalue.cv2.tracking;

import org.apache.tapestry5.ioc.Messages;

/**
 * 广告主获取跟踪代码，与SV2兼容
 * @author jackie
 *
 */
public class Advertiser {
    public static final String IDMARK = "";
    /**
     * 广告主ID
     */
    public int id = 0;
    /**
     * 如果广告主在开始做联盟业务之前，已经在做SEM业务，则需要提供该客户在联盟业务中的ID，存到本变量中
     */
    public int semId = 0;
    /**
     * 广告主获取广告代码时的语言选项
     * 0:中文
     * 1:英文
     */    
    public int LANG = 0;
    /**
     * 广告主绩效跟踪代码
     * @return
     */
    public String getTrackingCode(int forStepId, int forStepType, Messages message) {
        String memo = "";
        StringBuffer acs = new StringBuffer();
        acs.append("<!--Darwin Marketing Tracking System Ver2.0 Begin-->\n");
        acs.append("<script type=\"text/javascript\">\n");
        acs.append("var _rsClientId=\"").append(IDMARK);
        acs.append(this.id);
        acs.append("\";\n");
        acs.append("var _rsStepId=");
        acs.append(forStepId);
        acs.append(";\n");
        acs.append("var _rsStepTp=");
        acs.append(forStepType);
        acs.append(";\n");
        acs.append("var _rsOrderId=\"XXXXXX\";");
        memo = (this.LANG==0)?"//订单号，当要跟踪订单信息时设置":"//Set unique order ID, when you want to track order information";
        acs.append(memo);
        acs.append("\n");
        acs.append("var _rsAmount=\"XXXX.XX\";");
        memo = (this.LANG==0)?"//订单总金额":"//Set order total amount";
        acs.append(memo);
        acs.append("\n");        

        acs.append("var _rsDWLP=location.protocol.indexOf(\"https\")>-1?\"https:\":\"http:\";\n");
        acs.append("var _rsDWDN=\"//cv.dmclick.cn/\";\n");
        acs.append("var _rsDWURL = _rsDWLP+_rsDWDN+\"trk.js\";\n");
        acs.append("document.write(unescape(\"%3Cscript src='\" + _rsDWURL + \"' type='text/javascript'%3E%3C/script%3E\"));\n");
        acs.append("</script>\n");
        acs.append("<!--Darwin Marketing Tracking System Ver2.0 End-->\n");
        String acstr = acs.toString();
        return acstr;
    }
}
