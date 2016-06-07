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
public class XmlConverterUtf16Test {
	public static final Logger LOG = LoggerFactory.getLogger(XmlConverterUtf16Test.class);

	protected XmlConverter xmlConverter;

	@Before
	public void setUp() throws Exception {
		InputStream is = XmlConverterUtf16Test.class.getResourceAsStream("/test-utf16.xml");
		FormatSpec spec = new SpecLoader().load(is);
		xmlConverter = new XmlConverter(spec);
	}

	@Test
	public void testBe() throws Exception {
		StringWriter output = new StringWriter();
		xmlConverter.convert(XmlConverterUtf16Test.class.getResourceAsStream("/test-utf16be.txt"), output);
		Assert.assertTrue(output.toString().length() > 0);

		DocumentBuilderFactory domfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfact.newDocumentBuilder();
		Document doc = dombuilder.parse(new InputSource(new StringReader(output.toString())));

		XPathFactory xpathfact = XPathFactory.newInstance();
		XPath xpath = xpathfact.newXPath();
		Assert.assertEquals("⇒234567890", xpath.evaluate("/TEST/RECORD/DATA/text()", doc));
	}

	@Test
	public void testLe() throws Exception {
		StringWriter output = new StringWriter();
		xmlConverter.convert(XmlConverterUtf16Test.class.getResourceAsStream("/test-utf16le.txt"), output);
		Assert.assertTrue(output.toString().length() > 0);

		DocumentBuilderFactory domfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfact.newDocumentBuilder();
		Document doc = dombuilder.parse(new InputSource(new StringReader(output.toString())));

		XPathFactory xpathfact = XPathFactory.newInstance();
		XPath xpath = xpathfact.newXPath();
		Assert.assertEquals("⇒234567890", xpath.evaluate("/TEST/RECORD/DATA/text()", doc));
	}
}
