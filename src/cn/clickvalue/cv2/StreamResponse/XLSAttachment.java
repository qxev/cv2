package cn.clickvalue.cv2.StreamResponse;

import java.io.InputStream;

/**
 * wiki.apache.org
 * author:Thobson
 * @author Edited by Tim
 *
 */
public class XLSAttachment extends AttachmentStreamResponse {

    public XLSAttachment(InputStream inputStream, String fileName) {
        super(inputStream, fileName);
        this.contentType = "application/vnd.ms-excel";
        this.extension = "xls";
}

}
