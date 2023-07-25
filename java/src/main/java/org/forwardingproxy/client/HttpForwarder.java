package org.forwardingproxy.client;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.ssl.SSLContexts;
import org.forwardingproxy.Constants;
import org.forwardingproxy.config.model.Proxy;
import org.forwardingproxy.config.model.ServerConfig;
import org.forwardingproxy.dto.OutgoingResponse;
import org.forwardingproxy.dto.RequestLog;
import org.springframework.stereotype.Component;

@Component
public class HttpForwarder {

	public OutgoingResponse execute(ServerConfig serverConfig, RequestLog requestLog) {
		OutgoingResponse result = null;
		LocalDateTime startProcessTime = null;
		LocalDateTime endProcessTime = null;
		HttpClientConnectionManager connectionManager = null;

		try {
			HttpUriRequestBase httpRequest = new HttpUriRequestBase(requestLog.getHttpMethod(), new URI(requestLog.getUri()));
			if (requestLog.getRequestHeaders() != null) {
				requestLog.getRequestHeaders().putInto(httpRequest);
			}
			if (requestLog.getRequestBody() != null) {
				httpRequest.setEntity(requestLog.getRequestBody().toHttpEntity());
			}

			HttpClientResponseHandler<OutgoingResponse> responseHandler = new HttpResponseHandler();

			HttpClientBuilder httpClientBuilder = HttpClients.custom();
			HttpClientContext httpClientContext = null;

			SSLContext sslContext = acceptAllSSLCertificates();
			// Set Proxy
			Proxy proxy = serverConfig.getProxy();
			if (proxy != null && proxy.hasValue()) {
				if (proxy.isHttpProxy()) {
					// HttpProxy setting
					DefaultProxyRoutePlanner proxyRoutePlanner = proxy.getProxyRoutePlanner();
					httpClientBuilder.setRoutePlanner(proxyRoutePlanner);
					if(proxy.hasCredential()) {
						httpClientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
					}
				} else {
					// SOCKProxy setting
					connectionManager = proxy.getConnectionManager(sslContext);
					// Set SOCKProxy
					httpClientBuilder.setConnectionManager(connectionManager);
					httpClientContext = proxy.getHttpClientContext();
				}
			}
			// Set Proxy --- END ---
			if(connectionManager == null) {
				httpClientBuilder.setConnectionManager(getConnectionManager(sslContext));
			}

			try (CloseableHttpClient httpclient = httpClientBuilder.setDefaultRequestConfig(serverConfig.buildRequestConfig()).build();) {
				startProcessTime = LocalDateTime.now();
				if (httpClientContext != null) {
					result = httpclient.execute(httpRequest, httpClientContext, responseHandler);
				} else {
					result = httpclient.execute(httpRequest, responseHandler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (startProcessTime == null) {
				startProcessTime = LocalDateTime.now();
			}
			if (result == null) {
				result = OutgoingResponse.badRequest();
				endProcessTime = LocalDateTime.now();
			} else if (result.getDateReceived() != null) {
				endProcessTime = result.getDateReceived();
			}
			result.setDateSent(startProcessTime);
			long responseTime = Math.min(ChronoUnit.MILLIS.between(startProcessTime, endProcessTime), Integer.MAX_VALUE);
			result.setResponseTime(responseTime);

		}
		return result;
	}

	private SSLContext acceptAllSSLCertificates() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext result = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();
		return result;
	}

	private HttpClientConnectionManager getConnectionManager(SSLContext sslContext) {
		final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create().setSslContext(sslContext).build();
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register(Constants.HTTP, PlainConnectionSocketFactory.INSTANCE)
				.register(Constants.HTTPS, sslSocketFactory)
				.build();
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		return connectionManager;
	}

}
