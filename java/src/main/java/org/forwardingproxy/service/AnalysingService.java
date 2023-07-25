package org.forwardingproxy.service;

import java.util.List;

import org.forwardingproxy.dto.AnalysedLog;

public interface AnalysingService {

	List<AnalysedLog> findUnanalyzedLog();

	void update(AnalysedLog analysedLog);
}
