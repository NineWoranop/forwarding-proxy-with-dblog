package org.forwardingproxy.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class OutgoingResponse {
	private int httpCode;
	private HttpHeaders responseHeaders;
	private HttpBody responseBody;
	private LocalDateTime dateSent;
	private LocalDateTime dateReceived;
	private long responseTime;

	public ResponseLog toResponseLog(Long id) {
		if (id == null) {
			return null;
		}
		ResponseLog.ResponseLogBuilder builder = ResponseLog.builder();
		builder.id(id);
		builder.dateSent(this.dateSent);
		builder.dateReceived(this.dateReceived);
		builder.httpCode(this.httpCode);
		builder.responseHeaders(this.responseHeaders);
		builder.responseBody(this.responseBody);
		builder.responseTime(this.responseTime);
		return builder.build();
	}

	public static OutgoingResponse badRequest() {
		OutgoingResponse result = new OutgoingResponse();
		result.setDateReceived(LocalDateTime.now());
		result.setHttpCode(HttpStatus.BAD_REQUEST.value());
		result.setResponseHeaders(HttpHeaders.plainText());
		result.setResponseBody(HttpBody.badRequest());
		return result;
	}
}
