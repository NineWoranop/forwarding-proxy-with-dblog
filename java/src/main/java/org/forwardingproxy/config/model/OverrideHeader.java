package org.forwardingproxy.config.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OverrideHeader {
	private String name;
	private String value;

}
