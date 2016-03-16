package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.model.AffiliateTrackData;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class AffiliateTrackDataService extends BaseService<AffiliateTrackData> {

    /**
     * @param trackCode
     * @param trackIP
     * @return List<AffiliateTrackData>
     * 
     * 传入条件为null或者空字符串时，不再以该条件限定查询
     */
    public List<AffiliateTrackData> findByTrackDataAndTrackID(String trackCode,
            String trackIP,String ruleType) {
        CritQueryObject c = new CritQueryObject();
        
        Map<String, Object> conditions = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(trackCode)) {
            conditions.put("trackCode", trackCode);
        }
        if (StringUtils.isNotBlank(trackIP)) {
            conditions.put("trackIp", trackIP);
        }
        if(StringUtils.isNotBlank(ruleType)) {
            conditions.put("ruleType", Integer.parseInt(ruleType));
        }
        
        c.addOrder(Order.desc("trackTime"));
        c.setMaxResults(50);
        c.setCondition(conditions);
        List<AffiliateTrackData> affiliateTrackDatas = find(c);
        return affiliateTrackDatas;
    }
}
