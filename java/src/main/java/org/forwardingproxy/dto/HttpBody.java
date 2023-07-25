package org.forwardingproxy.dto;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.forwardingproxy.Constants;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpBody {
	private String contentType;
	private byte[] bytes;

	public HttpBody(byte[] bytes, String contentType) {
		this.bytes = bytes;
		this.contentType = contentType;
	}

	public static HttpBody from(InputStream inputStream, String contentType) {
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HttpBody(bytes, contentType);
	}

	public static HttpBody badRequest() {
		return new HttpBody(Constants.BAD_REQUEST_BODY_BYTES, Constants.CONTENT_TYPE_PLAIN_TEXT);
	}

	public static HttpBody internalServerError() {
		return new HttpBody(Constants.INTERNAL_SERVER_ERROR_BODY_BYTES, Constants.CONTENT_TYPE_PLAIN_TEXT);
	}

	public static HttpBody internalMockError() {
		return new HttpBody(Constants.INTERNAL_MOCK_ERROR_BODY_BYTES, Constants.CONTENT_TYPE_PLAIN_TEXT);
	}

	public void putInto(HttpServletResponse response) {
		try {
			ServletOutputStream stream = response.getOutputStream();
			stream.write(bytes);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HttpEntity toHttpEntity() {
		if (contentType != null) {
			return new ByteArrayEntity(this.bytes, ContentType.create(contentType));
		} else {
			return new ByteArrayEntity(this.bytes, null);
		}
	}

	public static HttpBody from(HttpEntity responseBodyEntity) {
		if (responseBodyEntity != null) {
			InputStream responseBodyStream;
			try {
				responseBodyStream = responseBodyEntity.getContent();
				HttpBody responseBody = HttpBody.from(responseBodyStream, responseBodyEntity.getContentType());
				return responseBody;
			} catch (UnsupportedOperationException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
