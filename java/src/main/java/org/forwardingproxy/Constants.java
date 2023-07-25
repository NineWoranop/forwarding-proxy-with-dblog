package org.forwardingproxy;

import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_PLAIN_TEXT = "text/plain";
	public static final String BAD_REQUEST_BODY = "Bad request. Please check your request and settings.";
	public static final byte[] BAD_REQUEST_BODY_BYTES = BAD_REQUEST_BODY.getBytes();
	public static final String INTERNAL_SERVER_ERROR_BODY = "Please check your settings.";
	public static final byte[] INTERNAL_SERVER_ERROR_BODY_BYTES = INTERNAL_SERVER_ERROR_BODY.getBytes();
	public static final String INTERNAL_MOCK_ERROR_BODY = "Please check your mock files.";
	public static final byte[] INTERNAL_MOCK_ERROR_BODY_BYTES = INTERNAL_MOCK_ERROR_BODY.getBytes();
	public static final String HTTPHEADER_CONTENT_LENGTH = "Content-Length".toLowerCase();
	public static final String HTTPHEADER_TRANSFER_ENCODING = "Transfer-Encoding".toLowerCase();
	public static final List<String> IGNORE_HTTP_HEADERS = Arrays.asList(HTTPHEADER_CONTENT_LENGTH, HTTPHEADER_TRANSFER_ENCODING);
	public static final String ORIGIN = "{Origin}";
	public static final String EMPTY_STRING = "";
	public static final String NEW_LINE = "\n";
	public static final int NEW_LINE_INT = 10;
	public static final int RETURN_INT = 13;
	public static final int REFERENCE_ID_FIELD_SIZE = 100;
	public static final String COLON_SYMBOL = ":";
	public static final String COMMA_SYMBOL = ",";
	public static final String SLASH_SYMBOL = "/";
	public static final String OPENING_CURLY_BRACKET_SYMBOL = "{";
	public static final String CLOSING_CURLY_BRACKET_SYMBOL = "}";
	public static final String QUESTION_MARK_SYMBOL = "?";
	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public static final String SOCKS_ADDRESS = "socks.address";
}
