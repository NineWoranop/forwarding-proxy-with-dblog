package org.forwardingproxy.repo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.forwardingproxy.dto.ResponseLog;

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
public class ResponseProxyLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "http_code")
	private Integer httpCode;

	@Column(name = "response_headers")
	private String responseHeaders;

	@Lob
	@Column(name = "response_body")
	private byte[] responseBody;

	@Column(name = "date_sent")
	private LocalDateTime dateSent;

	@Column(name = "date_received")
	private LocalDateTime dateReceived;

	@Column(name = "response_time")
	private Long responseTime;

	public static ResponseProxyLogEntity from(ResponseLog responseLog) {
		ResponseProxyLogEntity result = new ResponseProxyLogEntity();
		result.dateReceived = responseLog.getDateReceived();
		result.dateSent = responseLog.getDateSent();
		result.httpCode = responseLog.getHttpCode();
		result.id = responseLog.getId();
		if (responseLog.getResponseHeaders() != null) {
			result.responseHeaders = responseLog.getResponseHeaders().toString();
		}
		if (responseLog.isShouldLogResponseBody() && responseLog.getResponseBody() != null) {
			result.responseBody = responseLog.getResponseBody().getBytes();
		}
		result.responseTime = responseLog.getResponseTime();
		return result;
	}
}
