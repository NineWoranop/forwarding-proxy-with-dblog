package org.forwardingproxy.dto;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.Header;
import org.forwardingproxy.Constants;

import lombok.Getter;

@Getter
public class HttpHeaders {

	private Map<String, String> headers;

	public HttpHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public static HttpHeaders from(HttpServletRequest request) {
		Map<String, String> headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		return new HttpHeaders(headers);
	}

	public void putInto(HttpUriRequestBase httpRequest) {
		Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			String headerName = entry.getKey();
			if (headerName != null) {
				boolean ignoreHeaders = Constants.IGNORE_HTTP_HEADERS.contains(headerName.toLowerCase());
				if (!ignoreHeaders) {
					httpRequest.addHeader(headerName, entry.getValue());
				}
			}
		}
	}

	public void putInto(HttpServletResponse response) {
		Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			response.setHeader(entry.getKey(), entry.getValue());
		}
	}

	public static HttpHeaders plainText() {
		Map<String, String> headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		return new HttpHeaders(headers);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
		if (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			builder.append(entry.getKey()).append(':').append(entry.getValue());
			while (iterator.hasNext()) {
				entry = iterator.next();
				builder.append(Constants.NEW_LINE);
				builder.append(entry.getKey()).append(':').append(entry.getValue());
			}
		}
		return builder.toString();
	}

	public static HttpHeaders from(Header[] sourceHeaders) {
		if (sourceHeaders != null) {
			Map<String, String> headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
			for (Header header : sourceHeaders) {
				headers.put(header.getName(), header.getValue());
			}
			return new HttpHeaders(headers);
		}
		return null;
	}

	public static HttpHeaders from(String headersText) {
		Map<String, String> headersMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		if (headersText != null) {
			String[] values = headersText.split(Constants.NEW_LINE);
			for (int index = 0; index < values.length; index++) {
				String line = values[index];
				if (StringUtils.isNotBlank(line)) {
					String[] texts = line.split(Constants.COLON_SYMBOL, 2);
					if (texts.length == 2) {
						String headerName = texts[0];
						String headerValue = texts[1];
						headersMap.put(headerName, headerValue);
					} else {
						System.out.println("Invalid Text:" + line);
					}
				}
			}
		}
		return new HttpHeaders(headersMap);
	}

	public HttpHeader contains(String headerName) {
		String headerValue = headers.get(headerName);
		if (headerValue == null) {
			return null;
		}
		return HttpHeader.builder().name(headerName).value(headerValue).build();
	}

	public static boolean isVariable(String value) {
		if (value.startsWith(Constants.OPENING_CURLY_BRACKET_SYMBOL) && value.endsWith(Constants.CLOSING_CURLY_BRACKET_SYMBOL)) {
			return true;
		}
		return false;
	}

	public void override(String headerName, String value) {
		headers.put(headerName, value);
	}

	public String getHeaderValue(String HeaderName) {
		return headers.get(HeaderName);
	}

	public static VariableField parseVariable(String value) {
		String expression = value.substring(1, value.length() - 1);
		String[] values = expression.split(Constants.COLON_SYMBOL, 2);
		if (values.length == 2) {
			VariableFieldType variableFieldType = VariableFieldType.fromName(values[0].trim());
			String fieldName = values[1].trim();
			if (variableFieldType != null && StringUtils.isNoneBlank(fieldName)) {
				VariableField result = new VariableField(variableFieldType, fieldName);
				return result;
			}
		}
		return null;
	}

	public String getContentType() {
		String result = headers.get(Constants.CONTENT_TYPE);
		return result;
	}
}
