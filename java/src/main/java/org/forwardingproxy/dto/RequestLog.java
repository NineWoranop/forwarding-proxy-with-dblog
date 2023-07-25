package org.forwardingproxy.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLog {
	private Integer listeningPort;
	private String httpMethod;
	private String uri;
	private HttpHeaders requestHeaders;
	private HttpBody requestBody;
	private LocalDateTime dateSent;
	boolean shouldLogRequestBody;
	boolean shouldLogResponseBody;
	boolean usedMock;
}
