package cn.clickvalue.cv2.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.exceptions.BusinessException;

public class FileUtils {

	public static String upload(UploadedFile file, String path, String[] types) {
		StringBuffer sb = new StringBuffer();
		try {
			validateUpLoadFile(file, 1048576000, types);
			sb.append(RealPath.getUploadPath(path));
			RealPath.mkDirs(sb.toString());
			sb.append(UUIDUtil.getUUID());
			sb.append(FileUtils.getFileSuffix(file.getFileName()));
			file.write(new File(sb.toString()));
			return sb.toString().substring(sb.toString().indexOf("public") - 1);
		} catch (RuntimeException e) {
			throw new BusinessException("上传文件类型不符");
		}
	}

	
	public static File createExcelFile(String uniqueFolderName, String excelName) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(RealPath.getUploadPath(uniqueFolderName));
        RealPath.mkDirs(sb.toString());
        
        sb.append(excelName);
        sb.append(".xls");
        
        File file = new File(sb.toString());
        return file;
	}
	
	public static String uploadImage(UploadedFile file, String path) {
		return upload(file, path, new String[]{".jpg", ".gif", ".jpeg", ".bmp", ".png"});
	}

	public static String uploadFlash(UploadedFile file, String path) {
		return upload(file, path, new String[]{".swf",".fla"});
	}

	/**
	 * 验证文件上传
	 * 
	 * @param file
	 * @param maxSize
	 * @param types
	 */
	public static void validateUpLoadFile(UploadedFile file, int maxSize, String[] types) {
		BusinessException exceptions = new BusinessException();

		String fileName = file.getFileName();
		if (file.getSize() <= 0) {
			exceptions.add(new Exception("未读取到要上传的文件!"));
			throw exceptions;
		}
		String suff = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		long fileSize = file.getSize();
		List<String> list = Arrays.asList(types);
		if (!list.contains(suff.toLowerCase())) {
			exceptions.add(new Exception("上传文件类型不正确"));
			throw exceptions;
		} else if (fileSize > maxSize) {
			exceptions.add(new Exception("上传文件太大了!"));
			throw exceptions;
		}

	}

	public static boolean isExisted(String pathName) {
		File file = new File(pathName);
		return file.exists();
	}

	public static boolean remove(String pathName) {
		File file = new File(pathName);
		return file.delete();
	}

	public static boolean rename(String oldPathName, String newPathName) {
		File file = new File(oldPathName);
		return file.renameTo(new File(newPathName));
	}

	public static String getFileSuffix(String fileName) {
		String filesuffix = "s";
		// String filesuffix = null;
		// StringTokenizer fx = new StringTokenizer(fileName, ".");
		// while (fx.hasMoreTokens()) {
		// filesuffix = fx.nextToken();
		// }
		return "." + filesuffix;
	}

	public static String getHtmlSuffix() {
		String filesuffix = "html";
		return "." + filesuffix;
	}

	public static void createHtmlFile(String path, String data, String fileName) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(RealPath.getUploadPath(path));
		RealPath.mkDirs(sb.toString());
		sb.append(fileName);
		sb.append(getHtmlSuffix());
		File file = new File(sb.toString());
		org.apache.commons.io.FileUtils.writeStringToFile(file, data, "utf-8");
	}

	public static void main(String[] args) {
//		System.out.println(getFileSuffix("c://abc/add/add.txt"));
	}
}
