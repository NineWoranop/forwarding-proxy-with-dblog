package org.forwardingproxy.service;

import org.forwardingproxy.dto.RequestLog;
import org.forwardingproxy.dto.ResponseLog;

public interface ProxyService {
	ResponseLog create(RequestLog requestLog);
	void update(ResponseLog responseLog);
}
