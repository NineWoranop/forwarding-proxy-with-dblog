package org.forwardingproxy.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.forwardingproxy.client.HttpForwarder;
import org.forwardingproxy.config.AppConfig;
import org.forwardingproxy.config.model.Mock;
import org.forwardingproxy.config.model.ServerConfig;
import org.forwardingproxy.dto.OutgoingResponse;
import org.forwardingproxy.dto.RequestLog;
import org.forwardingproxy.dto.RequestLogBuilder;
import org.forwardingproxy.dto.ResponseLog;
import org.forwardingproxy.service.OverrideService;
import org.forwardingproxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyFilter extends OncePerRequestFilter {

	@Autowired
	private ProxyService proxyService;

	@Autowired
	private OverrideService overrideService;

	@Autowired
	private HttpForwarder httpForwarder;

	@Autowired
	private RequestLogBuilder requestLogBuilder;

	@Autowired
	private AppConfig appConfig;

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		ServerConfig serverConfig = appConfig.getServerConfig(request.getLocalPort());
		if (serverConfig != null) {
			Mock mock = serverConfig.getMock(request);
			boolean usedMock = (mock != null && mock.isEnabled());
			RequestLog requestLog = requestLogBuilder.build(serverConfig, request);
			requestLog.setUsedMock(usedMock);
			ResponseLog responseLog = proxyService.create(requestLog);
			if (usedMock) {
				responseLog.updateFrom(mock);
			} else {
				OutgoingResponse outgoingResponse = httpForwarder.execute(serverConfig, requestLog);
				responseLog.updateFrom(outgoingResponse);
			}
			proxyService.update(responseLog);
			overrideService.overrideResponseLog(serverConfig, requestLog.getRequestHeaders(), responseLog);
			responseLog.returnHttpResponse(response);
		} else {
			ResponseLog.returnInternalServerErrorResponse(response);
		}
	}

}
