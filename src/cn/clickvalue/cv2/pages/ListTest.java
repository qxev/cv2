package cn.clickvalue.cv2.pages;
import java.util.List;
import java.util.Hashtable;
import java.util.ArrayList;
public class ListTest {
    private Hashtable<String,Object> ht1 = null;
    private List<Hashtable<String,Object>> data = new ArrayList<Hashtable<String,Object>>();
    public ListTest() {
        Hashtable<String,Object> ht2 = new Hashtable<String,Object>();
        ht2.put("name", "Jackie");
        ht2.put("company", "Darwin");
        ht2.put("money", 1000000000);
        data.add(ht2);
        
        ht2 = new Hashtable<String,Object>();
        ht2.put("name", "David");
        ht2.put("company", "Darwin");
        ht2.put("money", 1000000000);
        data.add(ht2);
    }
    
    public List<Hashtable<String, Object>> getData() {
        return data;
    }
    
    public void setData(List<Hashtable<String, Object>> data) {
        this.data = data;
    }

    public Hashtable<String, Object> getHt1() {
        return ht1;
    }

    public void setHt1(Hashtable<String, Object> ht1) {
        this.ht1 = ht1;
    }
}
