/*
 * Copyright 2016, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.stream.impl;

import java.io.InputStream;

import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.stream.SofalifeInputFactory;
import com.mpobjects.formats.sofalife.stream.SofalifeStreamReader;

/**
 *
 */
public class SofalifeInputFactoryImpl extends SofalifeInputFactory {

	@Override
	public SofalifeStreamReader createStreamReader(FormatSpec aFormatSpec, InputStream aInputStream) {
		return new SofalifeStreamReaderImpl(aFormatSpec, aInputStream);
	}

}
