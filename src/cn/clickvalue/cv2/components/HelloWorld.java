package cn.clickvalue.cv2.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;


public class HelloWorld {
	@BeginRender
	void renderMessage(MarkupWriter writer) {
		writer.write("Larry Pig");
	}
}