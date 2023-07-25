package org.forwardingproxy.config.model;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.forwardingproxy.Constants;
import org.forwardingproxy.client.MyConnectionSocketFactory;
import org.forwardingproxy.client.MyPlainConnectionSocketFactory;

import lombok.Data;

@Data
public class Proxy {
	private ProxyType proxyType;
	private String proxyHost;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;

	public boolean hasValue() {
		return proxyType != null && StringUtils.isNoneBlank(proxyHost) && proxyPort != 0;
	}

	public boolean isHttpProxy() {
		return proxyType == ProxyType.HTTP;
	}

	public DefaultProxyRoutePlanner getProxyRoutePlanner() {
		if (hasValue() && proxyType == ProxyType.HTTP) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			return routePlanner;
		}
		return null;
	}

	public PoolingHttpClientConnectionManager getConnectionManager(SSLContext sslContext) {
		if (hasValue() && proxyType == ProxyType.SOCK) {
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register(Constants.HTTP, MyPlainConnectionSocketFactory.INSTANCE)
					.register(Constants.HTTPS, new MyConnectionSocketFactory(sslContext)).build();
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
			return connectionManager;
		}
		return null;
	}

	public HttpClientContext getHttpClientContext() {
		InetSocketAddress socksaddr = new InetSocketAddress(proxyHost, proxyPort);
		HttpClientContext context = HttpClientContext.create();
		context.setAttribute(Constants.SOCKS_ADDRESS, socksaddr);
		return context;
	}

	public boolean hasCredential() {
		return StringUtils.isNoneBlank(proxyUser);
	}

	public CredentialsProvider getCredentialsProvider() {
		BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
		AuthScope authScope = new AuthScope(proxyHost, proxyPort);
		Credentials creds = new UsernamePasswordCredentials(proxyUser, proxyPassword.toCharArray());
		credsProvider.setCredentials(authScope, creds);
		return credsProvider;
	}
}
