package cn.clickvalue.cv2.services.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.util.EnumValueEncoder;

/**
 * This class is used as a workaround to
 * https://issues.apache.org/jira/browse/TAPESTRY-2090.<br>
 * todo: remove this class when TAPESTRY-2090 has been resolved.
 */
public class ContextFixer {
    static private final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public String encodeBoolean(Boolean b) {
        return b == null ? "null" : b.toString();
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public Boolean unencodeBoolean(String s) {

        if (s == null || s.equals("") || s.equals("null")) {
            return null;
        } else {
            return Boolean.valueOf(s);
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public String encodeDate(Date date) {
        return date == null ? "null" : ISO_DATE_FORMAT.format(date);
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public Date unencodeDate(String s) {

        if (s == null || s.equals("") || s.equals("null")) {
            return null;
        } else {
            try {
                Date d = ISO_DATE_FORMAT.parse(s);
                return d;
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    @SuppressWarnings("unchecked")
    static public String encodeEnum(Enum e) {
        if (e == null) {
            return "null";
        } else {
            EnumValueEncoder encoder = new EnumValueEncoder(e.getClass());
            return encoder.toClient(e);
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    @SuppressWarnings("unchecked")
    static public Enum unencodeEnum(String s, Class enumClass) {
        if (s == null || s.equals("") || s.equals("null")) {
            return null;
        } else {
            EnumValueEncoder encoder = new EnumValueEncoder(enumClass);
            return encoder.toValue(s);
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public String encodeLong(Long l) {
        return l == null ? "null" : l.toString();
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public Long unencodeLong(String s) {

        if (s == null || s.equals("") || s.equals("null")) {
            return null;
        } else {
            return Long.valueOf(s);
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public String encodeString(String str) {
        if (str == null) {
            return "null";
        } else if (str.startsWith("null")) {
            return "null" + str;
        } else if (str.trim().equals("")) {
            return "blank";
        } else if (str.startsWith("blank")) {
            return "blank" + str;
        } else {
            return str;
        }
    }

    /**
     * This is a workaround to
     * https://issues.apache.org/jira/browse/TAPESTRY-2090.
     */
    static public String unencodeString(String s) {

        if (s == null) {
            return null;
        } else if (s.equals("null")) {
            return null;
        } else if (s.startsWith("null")) {
            return s.substring(4);
        } else if (s.equals("blank")) {
            return "";
        } else if (s.startsWith("blank")) {
            return s.substring(5);
        } else {
            return s;
        }
    }

}
