package cn.clickvalue.cv2.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class AbstractCommand implements ICommand {
    private final static Logger log = Logger.getLogger(AbstractCommand.class);

        public final static  String DATE_KEY="date";

        public final static String PARSED_DATE_KEY = "ans";

        public abstract SimpleDateFormat getDateFormater();

        public boolean execute(Map<String, Object>context) {
                boolean success = true;
                String dateToBeParsed = (String) context.get(DATE_KEY);
                SimpleDateFormat sdf = getDateFormater();
                try{
                        log.info("Trying to parse " + dateToBeParsed + " with format " +
                                          sdf.toPattern());
                        Date parsedDate = sdf.parse(dateToBeParsed);
            context.put(PARSED_DATE_KEY, parsedDate);

                }
                catch (ParseException pe){
                        log.info("Parser exception :" + pe + "\n pattern=" +
                                        sdf.toPattern());
                        success = false;
                }


                return success;
        }
}
