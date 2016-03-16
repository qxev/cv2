package cn.clickvalue.cv2.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.util.DefaultPrimaryKeyEncoder;

import cn.clickvalue.cv2.model.mts.Paragraph;

public class TestTest {
	
	@Persist
	private List<Paragraph> paragraphs = new ArrayList<Paragraph>();
	
	@Property
	@Persist
	private DefaultPrimaryKeyEncoder<Serializable, Paragraph> encoder = new DefaultPrimaryKeyEncoder<Serializable, Paragraph>();
	
	@Property
	private Paragraph paragraph;

//	public PrimaryKeyEncoder<Long, Paragraph> getPhoneEncoder() {
//		return new PrimaryKeyEncoder<Long, Paragraph>() {
//			public Long toKey(Paragraph value) {
//				return Long.parseLong(value.getId());
//			}
//
//			public void prepareForKeys(List<Long> keys) {
//			}
//
//			public Paragraph toValue(Long key) {
//				return (Paragraph) map.get(key);
//			}
//		};
//	}

	@PageLoaded
	public void pageLoaded() {
		for (int i = 0; i < 10; i++) {
			Paragraph paragraph = new Paragraph();
			paragraph.setContent("abc" + i);
			paragraphs.add(paragraph);
			encoder.add(paragraph.getId(), paragraph);
		}
	}

	Object onAddRowFromParagraphs() {
		Paragraph paragraph = new Paragraph();
		paragraph.setContent("asdlfhalskdjf");
		paragraphs.add(paragraph);
		encoder.add(paragraph.getId(), paragraph);
		return paragraph;
	}

	void onRemoveRowFromParagraphs(Paragraph paragraph) {
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}
}
