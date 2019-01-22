/*
 * Copyright 2015, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

/**
 *
 */
public class ByteOrderUtils {
	private static final Charset UTF_32;

	private static final Charset UTF_32BE;

	private static final Charset UTF_32LE;

	static {
		UTF_32 = createCharset("UTF-32");
		UTF_32LE = createCharset("UTF-32LE");
		UTF_32BE = createCharset("UTF-32BE");
	}

	private ByteOrderUtils() {
	}

	/**
	 * Corrects the charset in case a BOM was present. This will change UTF-16 to either UTF-16LE or UTF-16BE.
	 *
	 * @param aInput
	 * @param aCharset
	 * @return
	 * @throws IOException
	 */
	public static Charset correctCharset(InputStream aInput, Charset aCharset) throws IOException {
		if (aInput instanceof BOMInputStream == false) {
			return aCharset;
		}
		String bomCharset = ((BOMInputStream) aInput).getBOMCharsetName();
		if (bomCharset == null) {
			return aCharset;
		}
		// we accept the risk of unknown charsets
		return Charset.forName(bomCharset);
	}

	/**
	 * Get an input stream which corrects the byte order when applicable for the given charset.
	 *
	 * @param aInput
	 * @param aEncoding
	 * @return
	 */
	public static InputStream getInputStream(InputStream aInput, Charset aEncoding) {
		if (aInput == null || aEncoding == null) {
			return aInput;
		}
		ByteOrderMark[] boms = getByteOrderMarks(aEncoding);
		if (boms == null) {
			return aInput;
		}
		return new BOMInputStream(aInput, false, boms);
	}

	/**
	 * @param aEncoding
	 * @return
	 */
	static ByteOrderMark[] getByteOrderMarks(Charset aEncoding) {
		if (aEncoding == null) {
			return null;
		}
		if (StandardCharsets.UTF_8.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_8 };
		} else if (StandardCharsets.UTF_16.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE };
		} else if (StandardCharsets.UTF_16BE.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_16BE };
		} else if (StandardCharsets.UTF_16LE.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_16LE };
		} else if (UTF_32 != null && UTF_32.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
		} else if (UTF_32BE != null && UTF_32BE.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_32BE };
		} else if (UTF_32LE != null && UTF_32LE.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_32LE };
		}
		return null;
	}

	private static Charset createCharset(String aCode) {
		try {
			return Charset.forName(aCode);
		} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
			return null;
		}
	}
}
