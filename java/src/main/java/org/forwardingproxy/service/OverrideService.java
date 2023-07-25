package org.forwardingproxy.service;

import org.forwardingproxy.config.model.ServerConfig;
import org.forwardingproxy.dto.HttpHeaders;
import org.forwardingproxy.dto.ResponseLog;

public interface OverrideService {

	void overrideResponseLog(ServerConfig serverConfig, HttpHeaders requestHeaders, ResponseLog responseLog);

}
