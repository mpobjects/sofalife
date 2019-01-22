/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec.reader;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpobjects.formats.sofalife.spec.FieldSpec;
import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.spec.RecordSpec;

/**
 * Loads a format specification from an XML conforming to the sofalife.xsd.
 */
public class SpecLoader {

	public static final String NAMESPACE = "http://system.mp-objects.com/schemas/sofalife";

	private static final String ATTR_COMMENT_PREFIX = "comment-prefix";

	private static final String ATTR_ENCODING = "encoding";

	private static final String ATTR_ID = "id";

	private static final String ATTR_LENGTH = "length";

	private static final String ATTR_MULTIPLICITY = "multiplicity";

	private static final String ATTR_NAME = "name";

	private static final String ATTR_OPTIONAL = "optional";

	private static final String ATTR_QUALIFIER = "qualifier";

	private static final String ATTR_VERSION = "version";

	private static final String ELM_DESCRIPTION = "description";

	private static final String ELM_FIELD = "field";

	private static final String ELM_FORMAT = "format";

	private static final String ELM_RECORD = "record";

	private static final Logger LOG = LoggerFactory.getLogger(SpecLoader.class);

	protected XMLInputFactory factory;

	public SpecLoader() {
		factory = XMLInputFactory.newInstance();
	}

	/**
	 * Load the specification from the input stream
	 *
	 * @param aInputStream
	 *            Stream containing the specification in XML (utf-8 encoding).
	 * @return
	 * @throws SpecificationException
	 */
	public FormatSpec load(InputStream aInputStream) throws SpecificationException {
		Validate.notNull(aInputStream, "Input stream cannot be null");

		XMLStreamReader reader;
		try {
			reader = factory.createXMLStreamReader(aInputStream, "utf-8");

			FormatSpec spec = null;
			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT && inNamespace(reader)) {
					if (ELM_FORMAT.equals(reader.getLocalName())) {
						if (spec != null) {
							throw new SpecificationException(addReaderLocation("Duplicate format specification.", reader.getLocation()));
						}
						spec = readFormatElement(ELM_FORMAT, reader);
					}
				}
			}

			return spec;
		} catch (XMLStreamException e) {
			throw new SpecificationException(e.getMessage(), e);
		}
	}

	protected String addReaderLocation(String aString, Location aLocation) {
		if (aLocation == null) {
			return aString;
		}
		return String.format("%s At position %d:%d.", aString, aLocation.getLineNumber(), aLocation.getColumnNumber());
	}

	protected boolean inNamespace(XMLStreamReader aReader) {
		QName name = aReader.getName();
		if (name == null) {
			return false;
		}
		return NAMESPACE.equals(name.getNamespaceURI());
	}

	protected int[] parseMultiplicity(String aVal) throws SpecificationException {
		if (StringUtils.isBlank(aVal)) {
			throw new SpecificationException("Multiplicity cannot be blank");
		}
		aVal = aVal.trim();
		Matcher match = Pattern.compile("(([0-9]+)|\\*)(\\.\\.(([0-9]+)|\\*))?").matcher(aVal);
		if (!match.matches()) {
			throw new SpecificationException(String.format("Not a valid multiplicity definition: %s", aVal));
		}
		int[] vals = new int[] { 0, -1 };
		vals[0] = NumberUtils.toInt(match.group(1), -1);
		if (match.group(4) == null) {
			vals[1] = vals[0];
		} else {
			vals[1] = NumberUtils.toInt(match.group(4), -1);
		}
		if (vals[0] > vals[1] && vals[1] != -1) {
			// range check
			throw new SpecificationException(String.format("Invalid multiplicity range: %s", aVal));
		}
		if (vals[0] == -1) {
			// "*" or "*..*" should be "0..*"
			vals[0] = 0;
		}
		return vals;
	}

	protected String readDescriptionElement(String aElementName, XMLStreamReader aReader) throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		while (aReader.hasNext()) {
			int event = aReader.next();
			if (event == XMLStreamConstants.END_ELEMENT && inNamespace(aReader)) {
				if (aElementName.equals(aReader.getLocalName())) {
					break;
				}
			} else if (event == XMLStreamConstants.CDATA || event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.SPACE) {
				sb.append(aReader.getText());
			}
		}
		return StringUtils.defaultIfBlank(sb.toString(), null);
	}

	protected FieldSpec readFieldElement(String aElementName, XMLStreamReader aReader) throws SpecificationException, XMLStreamException {
		FieldSpec spec = new FieldSpec(aReader.getAttributeValue(null, ATTR_NAME), NumberUtils.toInt(aReader.getAttributeValue(null, ATTR_LENGTH), 0));

		String val = aReader.getAttributeValue(null, ATTR_OPTIONAL);
		if (val != null) {
			spec.setOptional("1".equals(val) || "true".equals(val));
		}

		while (aReader.hasNext()) {
			int event = aReader.next();
			if (event == XMLStreamConstants.END_ELEMENT && inNamespace(aReader)) {
				if (aElementName.equals(aReader.getLocalName())) {
					return spec;
				}
			}
		}

		throw new SpecificationException("Unterminated " + aElementName + " element.");
	}

	protected FormatSpec readFormatElement(String aElementName, XMLStreamReader aReader) throws SpecificationException, XMLStreamException {
		FormatSpec spec = new FormatSpec();

		String val = aReader.getAttributeValue(null, ATTR_VERSION);
		if (!StringUtils.isEmpty(val)) {
			spec.setVersion(val);
		}

		val = aReader.getAttributeValue(null, ATTR_NAME);
		if (!StringUtils.isEmpty(val)) {
			spec.setName(val);
		}

		val = aReader.getAttributeValue(null, ATTR_ENCODING);
		if (!StringUtils.isEmpty(val)) {
			try {
				spec.setEncoding(val);
			} catch (Exception e) {
				throw new SpecificationException("Invalid charachter encoding: " + val, e);
			}
		}

		val = aReader.getAttributeValue(null, ATTR_ID);
		if (!StringUtils.isEmpty(val)) {
			spec.setId(val);
		}

		val = aReader.getAttributeValue(null, ATTR_COMMENT_PREFIX);
		if (!StringUtils.isBlank(val)) {
			spec.setCommentPrefix(val);
		}

		while (aReader.hasNext()) {
			int event = aReader.next();
			if (event == XMLStreamConstants.START_ELEMENT && inNamespace(aReader)) {
				if (ELM_RECORD.equals(aReader.getLocalName())) {
					RecordSpec record = readRecordElement(ELM_RECORD, aReader);
					if (record != null) {
						spec.getRecords().add(record);
					}
				} else if (ELM_DESCRIPTION.equals(aReader.getLocalName())) {
					spec.setDescription(readDescriptionElement(ELM_DESCRIPTION, aReader));
				}
			} else if (event == XMLStreamConstants.END_ELEMENT && inNamespace(aReader)) {
				if (aElementName.equals(aReader.getLocalName())) {
					return spec;
				}
			}
		}

		throw new SpecificationException("Unterminated " + aElementName + " element.");
	}

	protected RecordSpec readRecordElement(String aElementName, XMLStreamReader aReader) throws SpecificationException, XMLStreamException {
		RecordSpec spec = new RecordSpec(aReader.getAttributeValue(null, ATTR_QUALIFIER));

		String val = aReader.getAttributeValue(null, ATTR_NAME);
		if (val != null) {
			spec.setName(val);
		}
		setMultiplicity(spec, aReader);

		boolean optionalPart = false;

		while (aReader.hasNext()) {
			int event = aReader.next();
			if (event == XMLStreamConstants.START_ELEMENT && inNamespace(aReader)) {
				if (ELM_FIELD.equals(aReader.getLocalName())) {
					FieldSpec field = readFieldElement(ELM_FIELD, aReader);
					if (field != null) {
						optionalPart |= field.isOptional();
						if (optionalPart && !field.isOptional()) {
							LOG.warn("Previous field was declared optional. Field {} for record {} will also be optional.{}", field.getName(),
									spec.getQualifier(), addReaderLocation("", aReader.getLocation()));
							field.setOptional(true);
						}
						spec.getFields().add(field);
					}
				} else if (ELM_RECORD.equals(aReader.getLocalName())) {
					RecordSpec record = readRecordElement(ELM_RECORD, aReader);
					if (record != null) {
						spec.getRecords().add(record);
					}
				} else if (ELM_DESCRIPTION.equals(aReader.getLocalName())) {
					spec.setDescription(readDescriptionElement(ELM_DESCRIPTION, aReader));
				}
			} else if (event == XMLStreamConstants.END_ELEMENT && inNamespace(aReader)) {
				if (aElementName.equals(aReader.getLocalName())) {
					return spec;
				}
			}
		}

		throw new SpecificationException("Unterminated " + aElementName + " element.");
	}

	protected void setMultiplicity(RecordSpec aSpec, XMLStreamReader aReader) throws SpecificationException {
		String val = aReader.getAttributeValue(null, ATTR_MULTIPLICITY);
		if (val == null) {
			return;
		}

		int[] multiplicity;
		try {
			multiplicity = parseMultiplicity(val);
		} catch (SpecificationException e) {
			throw new SpecificationException(String.format("Invalid multiplicity attribute for record %s", aSpec.getQualifier()), e);
		}
		if (multiplicity != null) {
			aSpec.setMinOccurance(multiplicity[0]);
			aSpec.setMaxOccurance(multiplicity[1]);
		}
	}
}
