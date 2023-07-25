package org.forwardingproxy.config.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.forwardingproxy.Constants;
import org.forwardingproxy.dto.HttpHeaders;

import lombok.Data;

@Data
public class ServerConfig {
	private int port;
	private boolean enabled;
	private String endpoint;
	private List<OverrideHeader> overrideRequestHeaders;
	private List<OverrideHeader> overrideResponseHeaders;
	private String ignoredUrls;
	private String ignoredRequestBodyUrls;
	private String ignoredResponseBodyUrls;
	private int connectionRequestTimeout;
	private int responseTimeout;

	private List<String> ignoredUrlsList;
	private List<String> ignoredRequestBodyUrlsList;
	private List<String> ignoredResponseBodyUrlsList;
	private Proxy proxy;
	private List<Mock> mocks;

	public String getEndPointUrl(String path) {
		return endpoint + path;
	}

	private List<String> getIgnoredUrlsList() {
		if (ignoredUrlsList == null) {
			ignoredUrlsList = split(ignoredUrls);
		}
		return ignoredUrlsList;
	}

	private List<String> getIgnoredRequestBodyUrlsList() {
		if (ignoredRequestBodyUrlsList == null) {
			ignoredRequestBodyUrlsList = split(ignoredRequestBodyUrls);
		}
		return ignoredRequestBodyUrlsList;
	}

	private List<String> getignoredResponseBodyUrlsList() {
		if (ignoredResponseBodyUrlsList == null) {
			ignoredResponseBodyUrlsList = split(ignoredResponseBodyUrls);
		}
		return ignoredResponseBodyUrlsList;
	}

	private static List<String> split(String urls) {
		if (urls != null) {
			List<String> list = Stream.of(urls.split(Constants.COMMA_SYMBOL)).map(String::trim).collect(Collectors.toList());
			list.removeAll(Arrays.asList("", null));
			return list;
		}
		return Arrays.asList();
	}

	public boolean shouldLogRequest(String path) {
		List<String> list = getIgnoredUrlsList();
		final String upperCasePath = path.toUpperCase();
		boolean result = list.stream().noneMatch(url -> url.toUpperCase().startsWith(upperCasePath));
		return result;
	}

	public boolean shouldLogRequestBody(String path) {
		List<String> list = getIgnoredRequestBodyUrlsList();
		final String upperCasePath = path.toUpperCase();
		boolean result = list.stream().noneMatch(url -> url.toUpperCase().startsWith(upperCasePath));
		return result;
	}

	public boolean shouldLogResponseBody(String path) {
		List<String> list = getignoredResponseBodyUrlsList();
		final String upperCasePath = path.toUpperCase();
		boolean result = list.stream().noneMatch(url -> url.toUpperCase().startsWith(upperCasePath));
		return result;
	}

	public RequestConfig buildRequestConfig() {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(Timeout.ofMilliseconds(this.connectionRequestTimeout))
				.setResponseTimeout(Timeout.ofMilliseconds(this.responseTimeout)).build();
		return config;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public Mock getMock(HttpServletRequest request) {
		if (mocks != null) {
			String upperCasePath = request.getServletPath().toUpperCase();
			String method = request.getMethod();
			for (Mock mock : mocks) {
				boolean matched = upperCasePath.startsWith(mock.getPath().toUpperCase());
				matched &= method.equalsIgnoreCase(mock.getMethod().name());
				if (matched) {
					return mock;
				}
			}
		}
		return null;
	}

	public HttpHeaders overrideRequestHeaders(HttpHeaders requestHeaders) {
		if (requestHeaders != null && overrideRequestHeaders != null) {
			overrideRequestHeaders.forEach(header -> {
				requestHeaders.override(header.getName(), header.getValue());
			});
		}
		return requestHeaders;
	}
}
