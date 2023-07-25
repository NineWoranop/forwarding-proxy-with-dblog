package org.forwardingproxy.service;

import java.util.List;

import org.forwardingproxy.config.model.OverrideHeader;
import org.forwardingproxy.config.model.ServerConfig;
import org.forwardingproxy.dto.HttpHeaders;
import org.forwardingproxy.dto.ResponseLog;
import org.forwardingproxy.dto.VariableField;
import org.forwardingproxy.dto.VariableFieldType;
import org.springframework.stereotype.Service;

@Service
public class OverrideServiceImpl implements OverrideService {

	@Override
	public void overrideResponseLog(ServerConfig serverConfig, HttpHeaders requestHeaders, ResponseLog responseLog) {
		overrideResponseHeaders(requestHeaders, responseLog.getResponseHeaders(), serverConfig.getOverrideResponseHeaders());
	}

	public void overrideResponseHeaders(HttpHeaders requestHeaders, HttpHeaders responseHeaders, List<OverrideHeader> overrideResponseHeaders) {
		if (overrideResponseHeaders != null) {
			for (OverrideHeader overrideHeader : overrideResponseHeaders) {
				String overrideValue = overrideHeader.getValue();
				if (HttpHeaders.isVariable(overrideValue)) {
					String value = findValue(overrideValue, requestHeaders, responseHeaders);
					if (value != null) {
						responseHeaders.override(overrideHeader.getName(), value);
					}
				} else if (overrideValue != null) {
					responseHeaders.override(overrideHeader.getName(), overrideValue);
				}
			}
		}
	}

	private String findValue(String overrideValue, HttpHeaders requestHeaders, HttpHeaders responseHeaders) {
		if (HttpHeaders.isVariable(overrideValue)) {
			VariableField variableField = HttpHeaders.parseVariable(overrideValue);
			if (variableField != null) {
				if (VariableFieldType.REQUEST_HEADER == variableField.getFieldType()) {
					String requestHeaderName = variableField.getFieldValue();
					String requestHeaderValue = requestHeaders.getHeaderValue(requestHeaderName);
					if (requestHeaderValue != null) {
						return requestHeaderValue;
					}
				} else if (VariableFieldType.RESPONSE_HEADER == variableField.getFieldType()) {
					String responseHeaderName = variableField.getFieldValue();
					String responseHeaderValue = responseHeaders.getHeaderValue(responseHeaderName);
					if (responseHeaderValue != null) {
						return responseHeaderValue;
					}
				}
			}
			return null;
		} else {
			return overrideValue;
		}
	}

}
