package cn.clickvalue.cv2.test;

import java.text.SimpleDateFormat;

public class ParserCommandA extends AbstractCommand {

    @Override
    public SimpleDateFormat getDateFormater() {
        // 2006 November 12
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMMM dd");
        sdf.setLenient(false);
        return sdf;
    }

}
