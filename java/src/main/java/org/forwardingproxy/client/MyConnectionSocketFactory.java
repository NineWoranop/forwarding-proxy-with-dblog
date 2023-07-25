package org.forwardingproxy.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.forwardingproxy.Constants;

public class MyConnectionSocketFactory extends SSLConnectionSocketFactory {

	public MyConnectionSocketFactory(final SSLContext sslContext) {
		super(sslContext);
	}

	@Override
	public Socket createSocket(final HttpContext context) throws IOException {
		InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute(Constants.SOCKS_ADDRESS);
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
		return new Socket(proxy);
	}

}