package org.forwardingproxy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.forwardingproxy.config.AnalystConfig;
import org.forwardingproxy.config.model.AnalysedColumn;
import org.forwardingproxy.dto.AnalysedLog;
import org.forwardingproxy.dto.AnalystColumnType;
import org.forwardingproxy.dto.HttpHeader;
import org.forwardingproxy.repo.AnalysedProxyLogRepository;
import org.forwardingproxy.repo.ProxyLogForAnalyzedRepository;
import org.forwardingproxy.repo.entity.AnalysedProxyLogEntity;
import org.forwardingproxy.repo.entity.ProxyLogForAnalyzedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysingServiceImpl implements AnalysingService {

	@Autowired
	private AnalystConfig analystConfig;

	@Autowired
	private AnalysedProxyLogRepository analysedRepo;

	@Autowired
	private ProxyLogForAnalyzedRepository proxyLogRepo;

	@Override
	public void update(AnalysedLog analysedLog) {
		analyse(analysedLog);
		AnalysedProxyLogEntity entity = AnalysedProxyLogEntity.from(analysedLog);
		analysedRepo.save(entity);
	}

	@Override
	public List<AnalysedLog> findUnanalyzedLog() {
		List<ProxyLogForAnalyzedEntity> entities = proxyLogRepo.findByAnalysedFalse();
		List<AnalysedLog> result = entities.stream().map(item -> item.toAnalysedLog()).collect(Collectors.toList());
		return result;
	}

	private void analyse(AnalysedLog analysedLog) {
		List<AnalysedColumn> analystColumns = analystConfig.getAnalystColumns();
		for (AnalysedColumn analysedColumn : analystColumns) {
			if (analysedColumn.getColumnType() == AnalystColumnType.REQUEST_HEADER) {
				String requestHeaderName = analysedColumn.getColumnFieldName();
				HttpHeader requestHttpHeader = analysedLog.getRequestHeaders().contains(requestHeaderName);
				if (requestHttpHeader != null) {
					analysedLog.updateReferenceId(analysedColumn.getIndex(), requestHttpHeader.getValue());
				}
			} else if (analysedColumn.getColumnType() == AnalystColumnType.RESPONSE_HEADER) {
				String responseHeaderName = analysedColumn.getColumnFieldName();
				HttpHeader responseHttpHeader = analysedLog.getResponseHeaders().contains(responseHeaderName);
				if (responseHttpHeader != null) {
					analysedLog.updateReferenceId(analysedColumn.getIndex(), responseHttpHeader.getValue());
				}
			}
		}
		analysedLog.setAnalysed(true);
	}

}
