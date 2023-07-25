package org.forwardingproxy.client;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.forwardingproxy.dto.HttpBody;
import org.forwardingproxy.dto.HttpHeaders;
import org.forwardingproxy.dto.OutgoingResponse;

public class HttpResponseHandler extends AbstractHttpClientResponseHandler<OutgoingResponse> {

	@Override
	public OutgoingResponse handleEntity(final HttpEntity httpEntity) throws IOException {
		throw new ClientProtocolException("Not support yet");
	}

	@Override
	public OutgoingResponse handleResponse(ClassicHttpResponse response) {
		OutgoingResponse result = new OutgoingResponse();
		result.setHttpCode(response.getCode());
		result.setDateReceived(LocalDateTime.now());
		result.setResponseHeaders(HttpHeaders.from(response.getHeaders()));
		result.setResponseBody(HttpBody.from(response.getEntity()));
		return result;
	}
}