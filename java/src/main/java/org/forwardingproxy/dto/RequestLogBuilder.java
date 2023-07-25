package org.forwardingproxy.dto;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.forwardingproxy.Constants;
import org.forwardingproxy.config.model.ServerConfig;
import org.springframework.stereotype.Component;

@Component
public class RequestLogBuilder {

	private String getPath(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		// Reconstruct original requesting URL
		StringBuilder path = new StringBuilder();
		path.append(request.getContextPath()).append(request.getServletPath());
		if (pathInfo != null) {
			path.append(pathInfo);
		}
		if (queryString != null) {
			path.append(Constants.QUESTION_MARK_SYMBOL).append(queryString);
		}
		return path.toString();
	}

	public RequestLog build(ServerConfig serverConfig, HttpServletRequest request) {
		RequestLog.RequestLogBuilder builder = RequestLog.builder();
		String path = getPath(request);
		builder.listeningPort(request.getLocalPort());
		builder.httpMethod(request.getMethod());
		builder.uri(serverConfig.getEndPointUrl(path));
		builder.shouldLogRequestBody(serverConfig.shouldLogRequestBody(path));
		builder.shouldLogResponseBody(serverConfig.shouldLogResponseBody(path));
		HttpHeaders requestHeaders = HttpHeaders.from(request); 
		requestHeaders = serverConfig.overrideRequestHeaders(requestHeaders);
		builder.requestHeaders(requestHeaders);
		try {
			builder.requestBody(HttpBody.from(request.getInputStream(), request.getContentType()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		builder.dateSent(LocalDateTime.now());
		RequestLog result = builder.build();
		return result;
	}
}
