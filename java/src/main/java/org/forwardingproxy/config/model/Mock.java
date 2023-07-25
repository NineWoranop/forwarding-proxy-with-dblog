package org.forwardingproxy.config.model;

import java.io.IOException;

import org.forwardingproxy.dto.HttpBody;
import org.forwardingproxy.dto.HttpHeaders;
import org.forwardingproxy.dto.ResponseLog;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mock {
	private boolean enabled;
	private HttpMethod method;
	private String path;
	private String file;
	private ResponseLog responseLog;

	public ResponseLog getResponseLog() {
		if (responseLog != null) {
			return responseLog;
		}
		ResponseLog.ResponseLogBuilder builder = ResponseLog.builder();
		try (MockReader reader = new MockReader(this.file)) {
			Integer httpCode = reader.readHttpCode();
			HttpHeaders httpHeaders = reader.readHttpHeaders();
			String contentType = httpHeaders.getContentType();
			HttpBody httpBody = reader.readHttpBody(contentType);
			builder.httpCode(httpCode.intValue());
			builder.responseHeaders(httpHeaders);
			builder.responseBody(httpBody);
		} catch(IOException e) {
			e.printStackTrace();
			builder.httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			builder.responseHeaders(HttpHeaders.plainText());
			builder.responseBody(HttpBody.internalServerError());
		}
		responseLog = builder.build();
		return responseLog;
	}
}
