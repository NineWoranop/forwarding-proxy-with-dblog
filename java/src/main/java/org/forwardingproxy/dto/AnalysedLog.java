package org.forwardingproxy.dto;

import org.forwardingproxy.Constants;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnalysedLog {
	private Long id;
	private Boolean analysed;
	private HttpHeaders requestHeaders;
	private HttpHeaders responseHeaders;
	private String referenceId_01;
	private String referenceId_02;
	private String referenceId_03;
	private String referenceId_04;
	private String referenceId_05;
	private String referenceId_06;
	private String referenceId_07;
	private String referenceId_08;
	private String referenceId_09;
	private String referenceId_10;

	public void updateReferenceId(int index, String requestHeaderValue) {
		if (requestHeaderValue != null) {
			int size = Math.min(requestHeaderValue.length(), Constants.REFERENCE_ID_FIELD_SIZE);
			String value = requestHeaderValue.substring(0, size);
			switch (index) {
			case 0:
				referenceId_01 = value;
				break;
			case 1:
				referenceId_02 = value;
				break;
			case 2:
				referenceId_03 = value;
				break;
			case 3:
				referenceId_04 = value;
				break;
			case 4:
				referenceId_05 = value;
				break;
			case 5:
				referenceId_06 = value;
				break;
			case 6:
				referenceId_07 = value;
				break;
			case 7:
				referenceId_08 = value;
				break;
			case 8:
				referenceId_09 = value;
				break;
			case 9:
				referenceId_10 = value;
				break;
			default:
				break;
			}
		}
	}
}