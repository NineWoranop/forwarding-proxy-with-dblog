package org.forwardingproxy.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.forwardingproxy.config.model.ServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties("app")
@Getter
public class AppConfig {
	private List<ServerConfig> servers = new ArrayList<>();

	public ServerConfig getServerConfig(int localPort) {
		for (ServerConfig serverConfig : servers) {
			if (localPort == serverConfig.getPort()) {
				return serverConfig;
			}
		}
		return null;
	}

	public List<Integer> getAdditionalPorts() {
		List<Integer> additionalPorts = servers.stream().skip(1).filter(item -> item.isEnabled()).map(item -> item.getPort()).collect(Collectors.toList());
		return additionalPorts;
	}
}
