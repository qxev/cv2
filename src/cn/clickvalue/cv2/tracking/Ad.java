package cn.clickvalue.cv2.tracking;
/**
 * 配置网站主广告信息
 * 在这里只配置广告的宽和高，以在界面打出IFRAME时决定其尺寸
 * 在IFRAME的请求网页内，根据参数加载对应的广告活动的播放配置文件，播放广告
 * @author jackie
 *
 */
public class Ad {
    
    /**
     * 当前广告的AffilliateID
     */
    public int affId = 0;
    /**
     * 当前广告所属的landing和Banner组合ID
     */
    public int aid = 0;
    /**
     * 当前广告Landing的地址
     */
    public String link = "";
    /**
     * 当前广告所属的广告活动ID
     */
    public int campaign_id = 0;
    /**
     * 广告的高度
     */
    public int height = 0;
    /**
     * 广告的宽度
     */
    public int width = 0;
    /**
     * 广告文案类型,指banner.type
     */
    public String type = "0";
    /**
     * 文字连接内容
     */
    public String text = "";
    /**
     * 目标窗口类型
     */
    public int targetWindow = 0;
    /**
     * 图片的地址
     */
    public String pictureUrl = "";
    /**
     * 头文件Url
     */
    public String headUrl = "";
    /**
     * 头文件高度
     */
    public String headHeight = "";
}
