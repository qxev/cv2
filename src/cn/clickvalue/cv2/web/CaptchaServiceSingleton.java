package cn.clickvalue.cv2.web;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * captcha的引擎实现
 */
public class CaptchaServiceSingleton extends ListImageCaptchaEngine {
	private static Logger log = Logger.getLogger(CaptchaServiceSingleton.class);

	private static final CaptchaServiceSingleton[] instances = { new CaptchaServiceSingleton(),
			new CaptchaServiceSingleton("imageCaptcha1") };

	ImageCaptcha imageCaptcha = null;

	private String sessionKey;

	private CaptchaServiceSingleton() {
		this.sessionKey = "imageCaptcha";
	}

	private CaptchaServiceSingleton(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public static CaptchaServiceSingleton getInstance() {
		return instances[0];
	}

	public static CaptchaServiceSingleton getInstance(int index) {
		if (index > instances.length - 1) {
			index = instances.length - 1;
		}
		return instances[index];
	}

	@Override
	protected void buildInitialFactories() {
		WordGenerator wgen = new RandomWordGenerator("abcdefghjkmnpquvwxyz23456789");
		RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(new int[] { 0, 50 }, new int[] { 0, 50 }, new int[] { 0, 50 });
		// 文字显示4个数
		TextPaster textPaster = new RandomTextPaster(Integer.valueOf(4), Integer.valueOf(4), cgen, true);
		// 图片的大小
		BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(Integer.valueOf(110), Integer.valueOf(35));

		Font[] fontsList = new Font[] { new Font("Arial", 0, 20) };

		FontGenerator fontGenerator = new RandomFontGenerator(Integer.valueOf(23), Integer.valueOf(23), fontsList);

		WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
		this.addFactory(new GimpyFactory(wgen, wordToImage));

	}

	/**
	 * Write the captcha image of current user to the servlet response
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void writeCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		imageCaptcha = getNextImageCaptcha();
		HttpSession session = request.getSession();
		session.setAttribute(sessionKey, imageCaptcha);
		BufferedImage image = (BufferedImage) imageCaptcha.getChallenge();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			// render the captcha challenge as a JPEG image in the response
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
			encoder.encode(image);
			outputStream.flush();
			outputStream.close();
			outputStream = null;// no close twice
		} catch (IOException ex) {
			log.error("产生图片异常 ＝＝ " + ex);
			throw ex;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
			imageCaptcha.disposeChallenge();
		}
	}

	public boolean validateCaptchaResponse(String validateCode, HttpSession session) {
		boolean flag = true;
		try {
			imageCaptcha = (ImageCaptcha) session.getAttribute(sessionKey);
			if (imageCaptcha == null) {
				flag = false;
			} else {
				flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();
			}
			session.removeAttribute(sessionKey);
			return flag;
		} catch (Exception ex) {
			return false;
		}
	}

}
