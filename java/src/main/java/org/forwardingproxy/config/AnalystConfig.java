package org.forwardingproxy.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.forwardingproxy.config.model.AnalysedColumn;
import org.forwardingproxy.dto.AnalystColumnType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalystConfig {

	@Value("${anlayse.referenceIds}")
	private List<String> referenceIds;

	private List<AnalysedColumn> analysedColumns = new ArrayList<>();
	private List<String> requestHeaders = new ArrayList<>();
	private List<String> responseHeaders = new ArrayList<>();

	@PostConstruct
	private void initialize() {
		int length = this.referenceIds.size();
		for (int index = 0; index < length; index++) {
			String referenceId = this.referenceIds.get(index);
			AnalysedColumn analysedColumn = AnalysedColumn.parse(index, referenceId);
			analysedColumns.add(analysedColumn);
			if (analysedColumn != null) {
				if (analysedColumn.getColumnType() == AnalystColumnType.REQUEST_HEADER) {
					requestHeaders.add(analysedColumn.getColumnFieldName());
				} else if (analysedColumn.getColumnType() == AnalystColumnType.RESPONSE_HEADER) {
					responseHeaders.add(analysedColumn.getColumnFieldName());
				}
			}
		}
	}

	public List<AnalysedColumn> getAnalystColumns() {
		return analysedColumns;
	}

	public boolean containsRequestHeader(String headerName) {
		return requestHeaders.contains(headerName);
	}

	public boolean containsResponseHeader(String headerName) {
		return responseHeaders.contains(headerName);
	}
}
