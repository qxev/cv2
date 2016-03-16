package cn.clickvalue.cv2.test;

import org.apache.tapestry5.upload.services.UploadedFile;

public class FileValue {


private UploadedFile filevalue;

private Integer id;

public UploadedFile getFilevalue() {
return filevalue;
}

public Integer getId() {
return id;
}

public void setFilevalue(UploadedFile filevalue) {
this.filevalue = filevalue;
}

public void setId(Integer id) {
this.id = id;
}

} 