package cn.clickvalue.cv2.model.sv2;

import java.io.Serializable;

/**
 * @author larry.lang
 *
 *  SV2的client
 */
public class Client implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private String name;
    
    private String site;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
