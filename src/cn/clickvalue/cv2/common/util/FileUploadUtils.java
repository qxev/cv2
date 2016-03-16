package cn.clickvalue.cv2.common.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.upload.services.UploadedFile;

public class FileUploadUtils {

	public static String upload(UploadedFile file, String path, String[] types, HttpServletRequest request) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			validateUpLoadFile(file, 1048576000, types);
			sb.append(RealPath.getUploadPath(path));
			RealPath.mkDirs(sb.toString());
			sb.append(UUIDUtil.getUUID());
			sb.append(FileUtils.getFileSuffix(file.getFileName()));
			file.write(new File(sb.toString()));
			return sb.toString();
		} catch (RuntimeException e) {
			throw new Exception("上传文件类型不符");
		}
	}

	public static String upload(UploadedFile file, String path, String[] types) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			validateUpLoadFile(file, 1048576000, types);
			sb.append(RealPath.getUploadPath(path));
			RealPath.mkDirs(sb.toString());
			sb.append(UUIDUtil.getUUID());
			sb.append(FileUtils.getFileSuffix(file.getFileName()));
			file.write(new File(sb.toString()));
			return sb.toString();
		} catch (RuntimeException e) {
			throw new Exception("上传文件类型不符");
		}
	}

	/**
	 * 验证文件上传
	 * 
	 * @param file
	 * @param maxSize
	 * @param types
	 * @throws Exception
	 */
	public static void validateUpLoadFile(UploadedFile file, int maxSize, String[] types) throws Exception {
		String fileName = file.getFileName();
		if (file.getSize() <= 0) {
			throw new Exception("未读取到要上传的文件!");
		}
		String suff = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		long fileSize = file.getSize();
		List<String> list = Arrays.asList(types);
		if (!list.contains(suff.toLowerCase())) {
			throw new Exception("上传文件类型不正确");
		} else if (fileSize > maxSize) {
			throw new Exception("上传文件太大了!");
		}

	}

}
