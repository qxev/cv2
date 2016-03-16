package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DEMO")
public class Demo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name = null;
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;


	public Demo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
