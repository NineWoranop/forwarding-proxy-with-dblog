package org.forwardingproxy.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.forwardingproxy.Constants;

public class MyPlainConnectionSocketFactory extends PlainConnectionSocketFactory {

	public static final MyPlainConnectionSocketFactory INSTANCE = new MyPlainConnectionSocketFactory();

	public static MyPlainConnectionSocketFactory getSocketFactory() {
		return INSTANCE;
	}

	public MyPlainConnectionSocketFactory() {
		super();
	}

	@Override
	public Socket createSocket(final HttpContext context) throws IOException {
		InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute(Constants.SOCKS_ADDRESS);
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
		return new Socket(proxy);
	}

}