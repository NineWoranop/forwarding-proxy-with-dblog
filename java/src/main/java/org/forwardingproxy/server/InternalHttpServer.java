package org.forwardingproxy.server;

import java.util.List;

import org.apache.catalina.connector.Connector;
import org.forwardingproxy.Constants;
import org.forwardingproxy.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InternalHttpServer {
	@Autowired
	private AppConfig appConfig;

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		List<Integer> addtionalPorts = appConfig.getAdditionalPorts();
		for (Integer port : addtionalPorts) {
			Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
			connector.setScheme(Constants.HTTP);
			connector.setPort(port);
			tomcat.addAdditionalTomcatConnectors(connector);
		}
		return tomcat;
	}
}
