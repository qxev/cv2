package cn.clickvalue.cv2.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static String urlEncode(String s) throws UnsupportedEncodingException {
		return URLEncoder.encode(s, "UTF-8");
	}

	public static String urlDecode(String s) throws UnsupportedEncodingException {
		return URLDecoder.decode(s, "UTF-8");
	}

	public static String buildParams(Map<String, String> params) {
		Set<String> keys = params.keySet();
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append("&");
			sb.append(key);
			sb.append("=");
			sb.append(params.get(key));
		}
		if (sb.length() > 0) {
			return sb.substring(1);
		}
		return "";
	}

	public static String appendParams(String url, Map<String, String> params) {
		String strParams = buildParams(params);
		if (strParams.length() > 0) {
			return url.concat(url.indexOf("?") > 0 ? "&" : "?").concat(strParams);
		} else {
			return url;
		}
	}

	public static String sendGet(String str) {
		HttpURLConnection openConnection = null;
		InputStream in = null;
		InputStreamReader reader = null;
		StringBuilder sb = new StringBuilder();
		CharBuffer bos = CharBuffer.allocate(1000);
		try {
			URL url = new URL(str);
			openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setRequestMethod("GET");
			openConnection.setDoOutput(true);
			openConnection.setDoInput(true);
			openConnection.setUseCaches(false);

			in = openConnection.getInputStream();
			reader = new InputStreamReader(in, "UTF-8");

			while (reader.read(bos) != -1) {
				bos.flip();
				sb.append(bos.toString());
			}
			reader.close();
			in.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (openConnection != null) {
				openConnection.disconnect();
			}
		}
		return sb.toString();
	}

	public static String sendPost(String str, String data) {
		HttpURLConnection openConnection = null;
		OutputStream outputStream = null;
		InputStream in = null;
		InputStreamReader reader = null;
		StringBuilder sb = new StringBuilder();
		CharBuffer bos = CharBuffer.allocate(1000);
		try {
			URL url = new URL(str);
			openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setRequestMethod("POST");
			openConnection.setDoOutput(true);
			openConnection.setDoInput(true);
			openConnection.setUseCaches(false);

			if (StringUtils.isNotEmpty(data)) {
				outputStream = openConnection.getOutputStream();
				byte[] bytes = data.getBytes();
				outputStream.write(bytes, 0, bytes.length);
				outputStream.write('\n');
				outputStream.write('\n');
				outputStream.flush();
				outputStream.close();
			}

			in = openConnection.getInputStream();
			reader = new InputStreamReader(in, "UTF-8");

			while (reader.read(bos) != -1) {
				bos.flip();
				sb.append(bos.toString());
			}
			reader.close();
			in.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (openConnection != null) {
				openConnection.disconnect();
			}
		}
		return sb.toString();
	}

	public static String sendGet(String str, String username, String password) {
		Authenticator.setDefault(new MyAuthenticator(username, password));
		return sendGet(str);
	}

	public static String sendPost(String str, String data, String username, String password) {
		Authenticator.setDefault(new MyAuthenticator(username, password));
		return sendPost(str, data);
	}

	static class MyAuthenticator extends Authenticator {
		private String username;
		private String password;

		public MyAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return (new PasswordAuthentication(username, password.toCharArray()));
		}
	}
}
