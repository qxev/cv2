package cn.clickvalue.cv2.test;

import java.text.SimpleDateFormat;

public class ParserCommandB extends AbstractCommand {

    @Override
    public SimpleDateFormat getDateFormater() {
        // 13 Jan 2007
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        sdf.setLenient(false);
        return sdf;
    }

}
