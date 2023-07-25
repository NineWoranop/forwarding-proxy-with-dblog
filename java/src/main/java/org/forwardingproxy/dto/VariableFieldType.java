package org.forwardingproxy.dto;

public enum VariableFieldType {
	REQUEST_HEADER("reqheader"), RESPONSE_HEADER("resheader");

	public final String name;

	private VariableFieldType(String name) {
		this.name = name;
	}

	public static VariableFieldType fromName(String name) {
		if (name != null) {
			VariableFieldType[] values = values();
			for (VariableFieldType fieldType : values) {
				if (fieldType.name.equals(name)) {
					return fieldType;
				}
			}
		}
		return null;
	}
}