package cn.clickvalue.cv2.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.exceptions.BusinessException;

public class ImageUtils {

	public static String upload(UploadedFile file, String path) {
		StringBuffer sb = new StringBuffer();
		try {
			validateUpLoadFile(file, 1048576, new String[]{".jpg", ".gif", ".png", ".jpeg"});
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

	public static BufferedImage getImage(String path) throws IOException {
		File file = new File(path);
		BufferedImage bi = ImageIO.read(file);
		return bi;
	}

	public static int[] getImageSize(String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			BufferedImage bi = ImageIO.read(file);
			int width = bi.getWidth();
			int height = bi.getHeight();
			int[] wh = new int[]{width, height};
			return wh;
		} else {
			int[] wh = new int[]{0, 0};
			return wh;
		}
	}

	public static void main(String[] args) throws IOException {
		getImageSize("sadsa");
	}
}
