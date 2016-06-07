/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.spec.reader.SpecLoader;

/**
 *
 */
public class XmlConverterTest {
	public static final Logger LOG = LoggerFactory.getLogger(XmlConverterTest.class);

	protected XmlConverter xmlConverter;

	@Before
	public void setUp() throws Exception {
		InputStream is = XmlConverterTest.class.getResourceAsStream("/geodis-pb-manifest.xml");
		FormatSpec spec = new SpecLoader().load(is);
		xmlConverter = new XmlConverter(spec);
	}

	@Test
	public void test() throws Exception {
		StringWriter output = new StringWriter();
		long time = System.currentTimeMillis();
		xmlConverter.convert(XmlConverterTest.class.getResourceAsStream("/geodis-pb-manifest.txt"), output);
		time = System.currentTimeMillis() - time;
		LOG.info("Manifest parsed in {}ms", time);
		Assert.assertTrue(output.toString().length() > 0);

		DocumentBuilderFactory domfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfact.newDocumentBuilder();
		Document doc = dombuilder.parse(new InputSource(new StringReader(output.toString())));

		XPathFactory xpathfact = XPathFactory.newInstance();
		XPath xpath = xpathfact.newXPath();
		Assert.assertEquals("TPBI0951026SE128QF0",
				xpath.evaluate("/VARIABLE_FIXED_LENGTH_FORMAT/A11/B01/B10[ORDERREFERENCENO='OREUSU2215533931GB']/TRACKINGNUMBER/text()", doc));

		Assert.assertEquals("1", xpath.evaluate("count(/VARIABLE_FIXED_LENGTH_FORMAT/Z10)", doc));
	}

	@Test
	public void testBom() throws Exception {
		StringWriter output = new StringWriter();
		long time = System.currentTimeMillis();
		xmlConverter.convert(XmlConverterTest.class.getResourceAsStream("/geodis-pb-manifest-bom.txt"), output);
		time = System.currentTimeMillis() - time;
		LOG.info("Manifest parsed in {}ms", time);
		Assert.assertTrue(output.toString().length() > 0);

		DocumentBuilderFactory domfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfact.newDocumentBuilder();
		Document doc = dombuilder.parse(new InputSource(new StringReader(output.toString())));

		XPathFactory xpathfact = XPathFactory.newInstance();
		XPath xpath = xpathfact.newXPath();
		Assert.assertEquals("TPBI0951026SE128QF0",
				xpath.evaluate("/VARIABLE_FIXED_LENGTH_FORMAT/A11/B01/B10[ORDERREFERENCENO='OREUSU2215533931GB']/TRACKINGNUMBER/text()", doc));

		Assert.assertEquals("1", xpath.evaluate("count(/VARIABLE_FIXED_LENGTH_FORMAT/Z10)", doc));
	}

	@Test
	public void testStrict() throws Exception {
		StringWriter output = new StringWriter();
		try {
			// don't allow blank lines
			xmlConverter.setProcessingMode(ProcessingMode.STRICT);
			xmlConverter.convert(XmlConverterTest.class.getResourceAsStream("/geodis-pb-manifest.txt"), output);
			Assert.fail("No exception thrown");
		} catch (ConverterException e) {
			LOG.info("Excepted exception: " + e.getMessage(), e);
			// should fail at garbage data at the end
			Assert.assertTrue(e.getMessage().contains("XXX strict will fail here"));
		}
	}

	@Test
	public void testVeryStrict() throws Exception {
		StringWriter output = new StringWriter();
		try {
			// don't allow blank lines
			xmlConverter.setProcessingMode(ProcessingMode.VERY_STRICT);
			xmlConverter.convert(XmlConverterTest.class.getResourceAsStream("/geodis-pb-manifest.txt"), output);
			Assert.fail("No exception thrown");
		} catch (ConverterException e) {
			LOG.info("Excepted exception: " + e.getMessage(), e);
			// should fail on the first blank line
			Assert.assertEquals(5, e.getLineNumber());
		}
	}
}
