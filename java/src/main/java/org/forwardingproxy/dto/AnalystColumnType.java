package org.forwardingproxy.dto;

public enum AnalystColumnType {
	REQUEST_HEADER("reqheader"), RESPONSE_HEADER("resheader");

	public final String name;

	private AnalystColumnType(String name) {
		this.name = name;
	}

	public static AnalystColumnType fromName(String name) {
		if (name != null) {
			AnalystColumnType[] values = values();
			for (AnalystColumnType columnType : values) {
				if (columnType.name.equals(name)) {
					return columnType;
				}
			}
		}
		return null;
	}
}
