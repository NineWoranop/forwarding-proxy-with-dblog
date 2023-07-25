package org.forwardingproxy.repo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.forwardingproxy.dto.RequestLog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "proxy_log")
@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class RequestProxyLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "listening_port")
	private Integer listeningPort;

	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "uri")
	private String uri;

	@Column(name = "http_code")
	private Integer httpCode;

	@Column(name = "request_headers")
	private String requestHeaders;

	@Column(name = "request_body")
	private byte[] requestBody;

	@Column(name = "date_sent")
	private LocalDateTime dateSent;

	@Column(name = "mock")
	private Boolean mock;

	public static RequestProxyLogEntity from(RequestLog requestLog) {
		RequestProxyLogEntity result = new RequestProxyLogEntity();
		result.dateSent = requestLog.getDateSent();
		result.httpMethod = requestLog.getHttpMethod();
		result.listeningPort = requestLog.getListeningPort();
		if (requestLog.getRequestHeaders() != null) {
			result.requestHeaders = requestLog.getRequestHeaders().toString();
		}
		if (requestLog.isShouldLogRequestBody() && requestLog.getRequestBody() != null) {
			result.requestBody = requestLog.getRequestBody().getBytes();
		}
		result.uri = requestLog.getUri();
		result.mock = requestLog.isUsedMock();
		return result;
	}
}
