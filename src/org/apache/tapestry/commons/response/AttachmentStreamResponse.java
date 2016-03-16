package org.apache.tapestry.commons.response;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class AttachmentStreamResponse implements StreamResponse {
	private InputStream is = null;

	/**
	 * This can be changed to something obscure, so that it is more likely to
	 * trigger a "Save As" dialog, although there is no guarantee.
	 * 
	 * ex: application/x-download
	 * 
	 * See http://www.onjava.com/pub/a/onjava/excerpt/jebp_3/index3.html
	 */
	protected String contentType = "text/plain";

	protected String extension = "txt";

	protected String filename = "default";

	public AttachmentStreamResponse(InputStream is, String filenameIn, String extension) {
		this.is = is;
		if (filenameIn != null) {
			this.filename = filenameIn;
		}
		
		this.extension = extension;
	}

	public AttachmentStreamResponse(InputStream is) {
		this.is = is;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getStream() throws IOException {
		return is;
	}

	public void prepareResponse(Response response) {
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename + ((extension == null) ? "" : ("." + extension)));
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// We can't set the length here because we only have an Input Stream at
		// this point. (Although we'd like to.)
		// We can only get size from an output stream.
		// arg0.setContentLength(.length);
	}

}
