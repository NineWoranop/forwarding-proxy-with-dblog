package org.forwardingproxy.dto;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.forwardingproxy.config.model.Mock;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseLog {
	private Long id;
	private int httpCode;
	private HttpHeaders responseHeaders;
	private HttpBody responseBody;
	private LocalDateTime dateSent;
	private LocalDateTime dateReceived;
	private long responseTime;
	private boolean shouldLogResponseBody;

	public void returnHttpResponse(HttpServletResponse response) {
		response.setStatus(this.httpCode);
		HttpHeaders responseHeader = this.responseHeaders;
		if (responseHeader != null) {
			responseHeader.putInto(response);
		}
		HttpBody responseBody = this.getResponseBody();
		if (responseBody != null) {
			responseBody.putInto(response);
		}
	}

	public static void returnInternalServerErrorResponse(HttpServletResponse response) {
		HttpBody.internalServerError().putInto(response);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public ResponseLog updateFrom(Mock mock) {
		ResponseLog mockResponseLog = mock.getResponseLog();
		this.httpCode = mockResponseLog.getHttpCode();
		this.responseHeaders = mockResponseLog.getResponseHeaders();
		this.responseBody = mockResponseLog.getResponseBody();
		LocalDateTime now = LocalDateTime.now();
		this.dateSent = now;
		this.dateReceived = now;
		this.responseTime = 0;
		return this;
	}

	public ResponseLog updateFrom(OutgoingResponse outgoingResponse) {
		this.dateSent = outgoingResponse.getDateSent();
		this.dateReceived = outgoingResponse.getDateReceived();
		this.httpCode = outgoingResponse.getHttpCode();
		this.responseHeaders = outgoingResponse.getResponseHeaders();
		this.responseBody = outgoingResponse.getResponseBody();
		this.responseTime = outgoingResponse.getResponseTime();
		return this;
	}
}
