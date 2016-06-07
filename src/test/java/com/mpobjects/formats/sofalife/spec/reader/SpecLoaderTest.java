/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec.reader;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.mpobjects.formats.sofalife.spec.FormatSpec;

/**
 *
 */
public class SpecLoaderTest {

	@Test
	public void testSpecLoader() throws Exception {
		InputStream is = SpecLoaderTest.class.getResourceAsStream("/example-spec.xml");
		SpecLoader ldr = new SpecLoader();
		FormatSpec spec = ldr.load(is);
		Assert.assertNotNull(spec);
	}

}
