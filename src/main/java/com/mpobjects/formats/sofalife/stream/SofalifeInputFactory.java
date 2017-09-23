/*
 * Copyright 2016, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.stream;

import java.io.InputStream;

import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.stream.impl.SofalifeInputFactoryImpl;

/**
 *
 */
public abstract class SofalifeInputFactory {
	public static SofalifeInputFactory newInstance() {
		return new SofalifeInputFactoryImpl();
	}

	/**
	 * Create a new stream reader
	 * 
	 * @param aFormatSpec
	 * @param aInputStream
	 * @return
	 * @throws SofalifeStreamException
	 *             Thrown if there is an issue opening the stream
	 * @throws NullPointerException
	 *             Thrown if either of the arguments is null
	 */
	public abstract SofalifeStreamReader createStreamReader(FormatSpec aFormatSpec, InputStream aInputStream) throws SofalifeStreamException;
}
