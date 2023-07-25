package org.forwardingproxy.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HttpHeader {
	private String name;
	private String value;
}
