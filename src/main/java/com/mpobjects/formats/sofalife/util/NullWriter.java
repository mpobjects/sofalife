/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.util;

import java.io.IOException;
import java.io.Writer;

/**
 * A memory efficient write-only writer.
 */
public final class NullWriter extends Writer {
	public NullWriter() {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void write(char[] aCbuf, int aOff, int aLen) throws IOException {
	}
}
