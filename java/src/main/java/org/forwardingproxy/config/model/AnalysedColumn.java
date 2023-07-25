package org.forwardingproxy.config.model;

import org.apache.commons.lang3.StringUtils;
import org.forwardingproxy.Constants;
import org.forwardingproxy.dto.AnalystColumnType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnalysedColumn {
	private int index;
	private AnalystColumnType columnType;
	private String columnFieldName;

	public static AnalysedColumn parse(int index, String text) {
		boolean isValidFormat = StringUtils.isNoneBlank(text) && text.contains(Constants.COLON_SYMBOL);
		if (isValidFormat) {
			String[] values = text.split(Constants.COLON_SYMBOL);
			if (values != null && values.length > 1) {
				String columnTypeText = values[0].trim();
				AnalystColumnType columnType = AnalystColumnType.fromName(columnTypeText);
				String columnFieldNameText = values[1].trim();
				boolean hasColumnType = (columnType != null);
				boolean hasColumnFieldName = StringUtils.isNoneBlank(columnFieldNameText);
				if (hasColumnType && hasColumnFieldName) {
					return AnalysedColumn.builder().index(index).columnType(columnType).columnFieldName(columnFieldNameText).build();
				}
			}
		}
		return null;
	}
}
