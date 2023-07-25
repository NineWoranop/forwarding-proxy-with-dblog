package org.forwardingproxy.config.model;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class SockProxy {
	private String sockHost;
	private int sockPort;

	public boolean hasValue() {
		return StringUtils.isNoneBlank(sockHost) && sockPort != 0;
	}
}
