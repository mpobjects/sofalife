/*
 * Copyright 2015, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.input.BOMInputStream;

/**
 *
 */
public class ByteOrderUtils {
	private static Charset UTF_32;

	private static Charset UTF_32BE;

	private static Charset UTF_32LE;

	static {
		try {
			UTF_32 = Charset.forName("UTF-32");
		} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
		}
		try {
			UTF_32LE = Charset.forName("UTF-32LE");
		} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
		}
		try {
			UTF_32BE = Charset.forName("UTF-32BE");
		} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
		}
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
	@SuppressWarnings("deprecation")
	static ByteOrderMark[] getByteOrderMarks(Charset aEncoding) {
		if (aEncoding == null) {
			return null;
		}
		if (Charsets.UTF_8.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_8 };
		} else if (Charsets.UTF_16.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE };
		} else if (Charsets.UTF_16BE.equals(aEncoding)) {
			return new ByteOrderMark[] { ByteOrderMark.UTF_16BE };
		} else if (Charsets.UTF_16LE.equals(aEncoding)) {
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
}
