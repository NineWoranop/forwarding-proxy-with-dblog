package org.forwardingproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VariableField {
	private VariableFieldType fieldType;
	private String fieldValue;

}
