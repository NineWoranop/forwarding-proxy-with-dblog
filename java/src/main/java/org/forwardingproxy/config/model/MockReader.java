package org.forwardingproxy.config.model;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.forwardingproxy.Constants;
import org.forwardingproxy.dto.HttpBody;
import org.forwardingproxy.dto.HttpHeaders;

public class MockReader implements Closeable {
	private long currentPosition;
	private long fileSize;
	private SeekableByteChannel sbc;
	private InputStream inputStream;
	private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
	private static final int BUFFER_SIZE = 8192;

	public MockReader(String file) throws IOException {
		File f = new File(file);
		if (!f.exists()) {
			throw new IOException("File not found");
		}
		this.currentPosition = 0;
		Path path = Paths.get(f.getAbsolutePath());
		this.sbc = Files.newByteChannel(path);
		this.inputStream = Channels.newInputStream(sbc);
		this.fileSize = sbc.size();
		if (fileSize > (long) MAX_BUFFER_SIZE) {
			throw new IOException("File is too large to read");
		}
	}

	private String readLine() throws IOException {
		byte[] bytes = readUntilNewLine();
		String result = new String(bytes, StandardCharsets.UTF_8);
		return result;
	}

	private boolean hasNext() {
		return currentPosition < fileSize;
	}

	private byte[] readUntilNewLine() throws IOException {
		int capacity = (int) (fileSize - currentPosition);
		byte[] buf = new byte[capacity];
		int nread = 0;
		int nByte;
		while (hasNext()) {
			nByte = inputStream.read();
			buf[nread] = (byte) nByte;
			nread += 1;
			this.currentPosition += 1;
			if (nByte == Constants.NEW_LINE_INT) {
				break;
			}
		}
		if (nread == 0) {
			buf = Arrays.copyOf(buf, 0);
		} else {
			nread -= 1;
			if (nread > 0) {
				nByte = buf[nread - 1];
				if (nByte == Constants.RETURN_INT) {
					nread -= 1;
				}
			}
			buf = Arrays.copyOf(buf, nread);
		}
		return buf;
	}

	private byte[] readUntilEOF() throws IOException {
		int capacity = (int) (fileSize - currentPosition);
		byte[] buf = new byte[capacity];
		int nread = 0;
		int n;
		for (;;) {
			// read to EOF which may read more or less than initialSize (eg: file
			// is truncated while we are reading)
			while ((n = inputStream.read(buf, nread, capacity - nread)) > 0)
				nread += n;

			// if last call to source.read() returned -1, we are done
			// otherwise, try to read one more byte; if that failed we're done too
			if (n < 0 || (n = inputStream.read()) < 0)
				break;

			// one more byte was read; need to allocate a larger buffer
			if (capacity <= MAX_BUFFER_SIZE - capacity) {
				capacity = Math.max(capacity << 1, BUFFER_SIZE);
			} else {
				if (capacity == MAX_BUFFER_SIZE)
					throw new OutOfMemoryError("Required array size too large");
				capacity = MAX_BUFFER_SIZE;
			}
			buf = Arrays.copyOf(buf, capacity);
			buf[nread++] = (byte) n;
		}
		return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
	}

	public Integer readHttpCode() throws IOException {
		if (hasNext()) {
			String firstLine = readLine();
			if (firstLine != null) {
				String[] values = firstLine.split(" ");
				if (values != null && values.length > 1) {
					return Integer.valueOf(values[1]);
				}
			}
		}
		return null;
	}

	public HttpHeaders readHttpHeaders() throws IOException {
		boolean hasHeaders = false;
		Map<String, String> headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		HttpHeaders result = new HttpHeaders(headers);
		String line = null;
		while (hasNext()) {
			line = readLine();
			if ("".equals(line)) {
				break;
			} else {
				String[] values = line.split(":", 2);
				String headerName = values[0];
				String headerValue = values[1];
				headers.put(headerName, headerValue);
				hasHeaders = true;
			}
		}
		if (hasHeaders) {
			return result;
		} else {
			return null;
		}
	}

	public HttpBody readHttpBody(String contentType) throws IOException {
		if (hasNext()) {
			byte[] content = readUntilEOF();
			HttpBody httpBody = new HttpBody(content, contentType);
			return httpBody;
		}
		return new HttpBody(new byte[0], contentType);
	}

	@Override
	public void close() throws IOException {
		try {
			if (sbc != null) {
				sbc.close();
			}
		} catch (Exception e) {
		}
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
		}
	}
}
