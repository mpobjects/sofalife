/*
 * Copyright 2016, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.stream.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.xml.stream.Location;

import org.apache.commons.lang3.Validate;

import com.mpobjects.formats.sofalife.spec.FieldSpec;
import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.spec.RecordSpec;
import com.mpobjects.formats.sofalife.stream.SofalifeStreamEventType;
import com.mpobjects.formats.sofalife.stream.SofalifeStreamException;
import com.mpobjects.formats.sofalife.stream.SofalifeStreamReader;
import com.mpobjects.formats.sofalife.util.ByteOrderUtils;

/**
 *
 */
public class SofalifeStreamReaderImpl implements SofalifeStreamReader {

	protected final FormatSpec formatSpec;

	protected final Reader inputReader;

	protected LocationImpl location;

	public SofalifeStreamReaderImpl(FormatSpec aFormatSpec, InputStream aInputStream) throws SofalifeStreamException {
		Validate.notNull(aFormatSpec, "FormatSpec cannot be null");
		Validate.notNull(aInputStream, "InputStream cannot be null");
		formatSpec = aFormatSpec;
		location = new LocationImpl(formatSpec.getId(), null);
		inputReader = getReader(aInputStream);
	}

	@Override
	public SofalifeStreamEventType getEventType() {
		// TODO Auto-generated method stub
		return SofalifeStreamEventType.UNDEFINED;
	}

	@Override
	public FieldSpec getFieldSpec() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordSpec getRecordSpec() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SofalifeStreamEventType next() {
		// TODO Auto-generated method stub
		return SofalifeStreamEventType.UNDEFINED;
	}

	/**
	 * Create an exception with optional location information
	 * 
	 * @param aMessage
	 * @param aCause
	 * @return
	 */
	protected SofalifeStreamException createException(String aMessage, Throwable aCause) {
		if (location != null) {
			return new SofalifeStreamException(aMessage, new LocationImpl(location), aCause);
		} else {
			return new SofalifeStreamException(aMessage, aCause);
		}
	}

	/**
	 * Get a suitable reader
	 * @param aInputStream
	 * @return
	 * @throws SofalifeStreamException
	 */
	protected Reader getReader(InputStream aInputStream) throws SofalifeStreamException {
		Charset charset = formatSpec.getEncoding();
		aInputStream = ByteOrderUtils.getInputStream(aInputStream, charset);
		try {
			charset = ByteOrderUtils.correctCharset(aInputStream, charset);
		} catch (IOException e) {
			throw new SofalifeStreamException("I/O Exception reading stream.", e);
		}
		return new InputStreamReader(aInputStream, charset);
	}

}
