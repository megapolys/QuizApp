package com.example.servingwebcontent.spring.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CachedBodyServletInputStream extends ServletInputStream {

	private final InputStream cachedBodyInputStream;

	public CachedBodyServletInputStream(byte[] cachedBody) {
		this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
	}

	@Override
	public int read() throws IOException {
		return cachedBodyInputStream.read();
	}

	@Override
	public boolean isFinished() {
		try {
			return cachedBodyInputStream.available() == 0;
		} catch (IOException ex) {
			log.warn("Cannot check available InputStream", ex);
		}
		return false;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReady() {
		return true;
	}
}
