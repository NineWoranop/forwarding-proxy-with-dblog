package org.forwardingproxy.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DtoTest {

	@Test
	@DisplayName("RequestLog Positive")
	void testSchoolPositive() {
		// When
		RequestLog actualResult = RequestLog.builder()
				.dateSent(LocalDateTime.MIN)
				.httpMethod("GET")
				.listeningPort(8080)
				.requestBody(HttpBody.badRequest())
				.requestHeaders(HttpHeaders.plainText())
				.shouldLogRequestBody(true)
				.shouldLogResponseBody(true)
				.uri("Http://localhost")
				.build();

		// Then
		assertThat(actualResult).hasNoNullFieldsOrProperties();
	}

}
