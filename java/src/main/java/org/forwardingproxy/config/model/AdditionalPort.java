package org.forwardingproxy.config.model;

import org.apache.commons.lang3.StringUtils;
import org.forwardingproxy.Constants;

import lombok.Data;

@Data
public class AdditionalPort {
	private int port;
	private String endpoint;

	public void chop() {
		if (endpoint.endsWith(Constants.SLASH_SYMBOL)) {
			endpoint = StringUtils.chop(endpoint);
		}
	}
}
