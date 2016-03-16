package cn.clickvalue.cv2.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.MyPrimaryKeyEncoder;

import cn.clickvalue.cv2.common.util.UUIDUtil;

public class TestAjaxBlock {

	@Property
	@Persist
	private List<List<String>> sups;

	@Inject
	@Property
	private Block p;

	@Inject
	@Property
	private Block table;

	private List<String> sup;

	public List<String> getSup() {
		return sup;
	}

	public void setSup(List<String> sup) {
		if (isSubmit){
			sup.clear();
			setSupForSubmit(sup);
		}
		this.sup = sup;
	}

	private void setSupForSubmit(final List<String> sup) {
		sups.add(sup);
		listEncode.add(UUIDUtil.getUUID(), sup);
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		if (isSubmit)
			setSForSubmit(s);
		this.s = s;
	}

	private void setSForSubmit(String s) {
		sup.add(s);
		sEncode.add(UUIDUtil.getUUID(), s);
	}

	private String s;

	@Property
	@Persist
	private MyPrimaryKeyEncoder<String, List<String>> listEncode;

	@Property
	@Persist
	private MyPrimaryKeyEncoder<String, String> sEncode;

	private boolean isSubmit = false;

	@SetupRender
	void setupRender() {
		if (sEncode == null) {
			sEncode = new MyPrimaryKeyEncoder<String, String>();
		}

		if (listEncode == null) {
			listEncode = new MyPrimaryKeyEncoder<String, List<String>>();
		}

		if (sups == null) {
			sups = new ArrayList<List<String>>();
			for (int i = 0; i < 3; i++) {
				List<String> sup = new ArrayList<String>();
				for (int j = 0; j < 3; j++) {
					String str = String.valueOf(i).concat("|").concat(
							String.valueOf(j));
					sup.add(str);
					sEncode.add(UUIDUtil.getUUID(), str);
				}
				sups.add(sup);
				listEncode.add(UUIDUtil.getUUID(), sup);
			}
		}

	}

	void onPrepareForSubmit() {
		sups.clear();
		isSubmit = true;
	}

	void onSubmit() {
		listEncode.clear();
		sEncode.clear();
		
		for(List<String> sup : sups){
			listEncode.add(UUIDUtil.getUUID(), sup);
			
			for(String str : sup){
				sEncode.add(UUIDUtil.getUUID(), str);
			}
		}
	}

	Object onAddRowFromFa() {
		List<String> sup = new ArrayList<String>();
		for (int j = 0; j < 3; j++) {
			String str = String.valueOf(j);
			sup.add(str);
			sEncode.add(UUIDUtil.getUUID(), str);
		}
		sups.add(sup);
		listEncode.add(UUIDUtil.getUUID(), sup);
		return sup;
	}

	Object onAddRowFromFs() {
		String str = String.valueOf(UUIDUtil.getUUID());
		sEncode.add(UUIDUtil.getUUID(), str);
		return str;
	}
}
