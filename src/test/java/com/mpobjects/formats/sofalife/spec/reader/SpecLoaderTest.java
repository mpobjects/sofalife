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

	@Test
	public void testMultiplicity() throws SpecificationException{
		SpecLoader ldr = new SpecLoader();
		int[] res = ldr.parseMultiplicity("1..*");
		Assert.assertEquals(1, res[0]);
		Assert.assertEquals(-1, res[1]);

		res = ldr.parseMultiplicity("5");
		Assert.assertEquals(5, res[0]);
		Assert.assertEquals(5, res[1]);

		res = ldr.parseMultiplicity("999..999");
		Assert.assertEquals(999, res[0]);
		Assert.assertEquals(999, res[1]);

		res = ldr.parseMultiplicity("*..*");
		Assert.assertEquals(0, res[0]);
		Assert.assertEquals(-1, res[1]);
		
		res = ldr.parseMultiplicity("*..5");
		Assert.assertEquals(0, res[0]);
		Assert.assertEquals(5, res[1]);

		try {
			ldr.parseMultiplicity("test");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
		
		try {
			ldr.parseMultiplicity("0..foo");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
		
		try {
			ldr.parseMultiplicity("");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
		
		try {
			ldr.parseMultiplicity("10..5");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
		
		try {
			ldr.parseMultiplicity("**..5");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
		
		try {
			ldr.parseMultiplicity("0..**");
			Assert.fail("Exception expected");
		} catch (SpecificationException e) {
		}
	}

}
