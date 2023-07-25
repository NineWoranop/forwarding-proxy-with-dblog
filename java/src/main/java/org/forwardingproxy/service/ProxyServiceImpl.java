package org.forwardingproxy.service;

import org.forwardingproxy.dto.RequestLog;
import org.forwardingproxy.dto.ResponseLog;
import org.forwardingproxy.repo.RequestProxyLogRepository;
import org.forwardingproxy.repo.ResponseProxyLogRepository;
import org.forwardingproxy.repo.entity.RequestProxyLogEntity;
import org.forwardingproxy.repo.entity.ResponseProxyLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProxyServiceImpl implements ProxyService {

	@Autowired
	private RequestProxyLogRepository crudRepo;
	@Autowired
	private ResponseProxyLogRepository updateRepo;

	@Override
	public ResponseLog create(RequestLog requestLog) {
		if (requestLog == null) {
			return null;
		}
		RequestProxyLogEntity entity = RequestProxyLogEntity.from(requestLog);
		entity = crudRepo.save(entity);
		ResponseLog result = ResponseLog.builder().id(entity.getId()).shouldLogResponseBody(requestLog.isShouldLogResponseBody()).build();
		return result;
	}

	@Override
	public void update(ResponseLog responseLog) {
		if (responseLog != null) {
			ResponseProxyLogEntity entity = ResponseProxyLogEntity.from(responseLog);
			updateRepo.save(entity);
		}
	}
}
