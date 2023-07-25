package org.forwardingproxy.repo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.forwardingproxy.dto.AnalysedLog;
import org.forwardingproxy.dto.HttpHeaders;

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
public class ProxyLogForAnalyzedEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "analysed")
	private Boolean analysed;

	@Column(name = "request_headers")
	private String requestHeaders;

	@Column(name = "response_headers")
	private String responseHeaders;

	public AnalysedLog toAnalysedLog() {
		AnalysedLog result = AnalysedLog.builder().id(id).analysed(analysed).requestHeaders(HttpHeaders.from(requestHeaders)).responseHeaders(HttpHeaders.from(responseHeaders)).build();
		return result;
	}
}
