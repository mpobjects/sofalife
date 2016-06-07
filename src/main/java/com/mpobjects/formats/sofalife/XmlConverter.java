/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpobjects.formats.sofalife.spec.FieldSpec;
import com.mpobjects.formats.sofalife.spec.FormatSpec;
import com.mpobjects.formats.sofalife.spec.RecordSpec;
import com.mpobjects.formats.sofalife.util.ByteOrderUtils;
import com.mpobjects.formats.sofalife.util.NullWriter;

/**
 * Reads the structured fixed length format and produces an XML according to the provided specification.
 */
public class XmlConverter {
	/**
	 * Local state, passed around between the methods.
	 */
	protected class ConverterState {
		protected String currentLine;

		protected LineNumberReader reader;

		public ConverterState(Reader aReader) {
			reader = new LineNumberReader(aReader);
		}

		public String getCurrentLine() {
			return currentLine;
		}

		public int getLineNumber() {
			return reader.getLineNumber();
		}

		public String nextLine() throws IOException {
			do {
				currentLine = reader.readLine();
				if (processingMode == ProcessingMode.VERY_STRICT) {
					// do not skip blank lines
					break;
				}
			} while (StringUtils.isBlank(currentLine) && currentLine != null);
			return getCurrentLine();
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(XmlConverter.class);

	protected XMLOutputFactory factory;

	protected FormatSpec formatSpec;

	protected ProcessingMode processingMode;

	protected boolean trimSpaces;

	public XmlConverter(FormatSpec aFormatSpec) {
		trimSpaces = true;
		processingMode = ProcessingMode.LENIENT;
		Validate.notNull(aFormatSpec, "Format is required");
		formatSpec = aFormatSpec;
		factory = XMLOutputFactory.newFactory();
	}

	/**
	 * Convert the input stream to an XML document written in the output writer.
	 *
	 * @param aInput
	 * @param aOutput
	 *            The output to write to. If null conversion will be simulated.
	 * @throws ConverterException
	 *             Throw during any kind of conversion error.
	 */
	public void convert(InputStream aInput, Writer aOutput) throws ConverterException {
		Validate.notNull(aInput, "Input is required");
		if (aOutput == null) {
			aOutput = new NullWriter();
		}

		XMLStreamWriter writer;
		ConverterState state = null;
		try {
			writer = factory.createXMLStreamWriter(aOutput);

			// TODO make optional?
			IndentingXMLStreamWriter indeted = new IndentingXMLStreamWriter(writer);
			indeted.setIndent("\t");
			writer = indeted;

			Charset charset = formatSpec.getEncoding();
			aInput = ByteOrderUtils.getInputStream(aInput, charset);
			charset = ByteOrderUtils.correctCharset(aInput, charset);

			state = new ConverterState(new InputStreamReader(aInput, charset));
			convert(state, writer);

			if (processingMode != ProcessingMode.LENIENT && state.getCurrentLine() != null) {
				throw new ConverterException(state.getLineNumber(), "Garbage data after end of format: " + state.getCurrentLine());
			}

		} catch (Exception e) {
			if (e instanceof ConverterException) {
				throw (ConverterException) e;
			}
			int lineNo = -1;
			if (state != null) {
				lineNo = state.getLineNumber();
			}
			throw new ConverterException(lineNo, e.getMessage(), e);
		}

	}

	public FormatSpec getFormatSpec() {
		return formatSpec;
	}

	public ProcessingMode getProcessingMode() {
		return processingMode;
	}

	/**
	 * Trim leading or trailing whitespace.
	 *
	 * @return True when whitespace is trimmed.
	 */
	public boolean isTrimSpaces() {
		return trimSpaces;
	}

	public void setProcessingMode(ProcessingMode aProcessingMode) {
		processingMode = aProcessingMode;
	}

	public void setTrimSpaces(boolean aTrimSpaces) {
		trimSpaces = aTrimSpaces;
	}

	protected void convert(ConverterState aState, XMLStreamWriter aWriter) throws XMLStreamException, ConverterException, IOException {
		aWriter.writeStartDocument();
		aWriter.writeStartElement(formatSpec.getName());

		// Read the first line
		aState.nextLine();
		processRecords(aState, aWriter, formatSpec.getRecords());

		aWriter.writeEndDocument();
	}

	protected void processFields(String aData, XMLStreamWriter aWriter, RecordSpec aRec, int aLineNumber) throws ConverterException, XMLStreamException {
		if (aRec.getFields().isEmpty()) {
			LOG.info("Record of type {} has no fields defined.", aRec.getQualifier());
			return;
		}
		for (FieldSpec field : aRec.getFields()) {
			LOG.debug("Processing field {}", field.getName());
			if (aData.length() == 0) {
				if (field.isOptional()) {
					return;
				}
				if (processingMode == ProcessingMode.STRICT || processingMode == ProcessingMode.VERY_STRICT) {
					throw new ConverterException(aLineNumber, String.format("Missing required field %s in record %s. On line %d.", field.getName(),
							aRec.getQualifier(), aLineNumber));
				}
			}
			String value;
			if (aData.length() < field.getLength()) {
				if (processingMode == ProcessingMode.STRICT || processingMode == ProcessingMode.VERY_STRICT) {
					throw new ConverterException(aLineNumber, String.format("Field %s in record %s is not long enough, received %d expected %d. On line %d.",
							field.getName(), aRec.getQualifier(), aData.length(), field.getLength(), aLineNumber));
				}
				LOG.warn("Received less data for field {} in record {} than expected. Received {} expected {}. On line {}.", field.getName(),
						aRec.getQualifier(), aData.length(), field.getLength(), aLineNumber);
				value = aData;
				aData = "";
			} else {
				value = aData.substring(0, field.getLength());
				aData = aData.substring(field.getLength());
			}

			value = sanitizeValue(value);
			if (value.isEmpty()) {
				aWriter.writeEmptyElement(field.getName());
			} else {
				aWriter.writeStartElement(field.getName());
				aWriter.writeCharacters(value);
				aWriter.writeEndElement();
			}
		}

		if (processingMode == ProcessingMode.VERY_STRICT && !aData.isEmpty()) {
			throw new ConverterException(aLineNumber, String.format("Extra data in record %s: '%s'", aRec.getQualifier(), aData.length()));
		}
	}

	protected void processRecord(ConverterState aState, XMLStreamWriter aWriter, RecordSpec aRec) throws ConverterException, XMLStreamException, IOException {
		LOG.debug("Processing record of type {} on line {}", aRec.getQualifier(), aState.getLineNumber());
		aWriter.writeStartElement(aRec.getName());

		String data = aState.getCurrentLine().substring(aRec.getQualifier().length());
		processFields(data, aWriter, aRec, aState.getLineNumber());
		aState.nextLine();

		if (!aRec.getRecords().isEmpty()) {
			LOG.debug("Processing child records for type {}", aRec.getQualifier());
			processRecords(aState, aWriter, aRec.getRecords());
		}

		aWriter.writeEndElement();
	}

	protected void processRecords(ConverterState aState, XMLStreamWriter aWriter, List<RecordSpec> aRecords) throws ConverterException, XMLStreamException,
			IOException {
		if (aRecords.isEmpty()) {
			// nothing to do
			return;
		}
		Iterator<RecordSpec> records = aRecords.iterator();

		// number of records of the same time processed in a row.
		int recCount = 0;
		RecordSpec currentRec = records.next();
		while (aState.getCurrentLine() != null && currentRec != null) {
			if (aState.getCurrentLine().startsWith(currentRec.getQualifier())) {
				if (currentRec.getMaxOccurance() > -1 && recCount > currentRec.getMaxOccurance()) {
					throw new ConverterException(aState.getLineNumber(), String.format("Read %d records of %s, but expected at most %d. At line %d.", recCount,
							currentRec.getQualifier(), currentRec.getMinOccurance(), aState.getLineNumber()));
				}
				processRecord(aState, aWriter, currentRec);
				++recCount;
			} else {
				if (currentRec.getMinOccurance() > 0 && recCount < currentRec.getMinOccurance()) {
					throw new ConverterException(aState.getLineNumber(), String.format("Read %d records of %s, but expected at least %d. At line %d.",
							recCount, currentRec.getQualifier(), currentRec.getMinOccurance(), aState.getLineNumber()));
				}
				if (records.hasNext()) {
					currentRec = records.next();
				} else {
					currentRec = null;
				}
				recCount = 0;
			}
		}

		if (aState.getCurrentLine() == null) {
			// Check for unexpected EOF
			while (records.hasNext()) {
				RecordSpec rec = records.next();
				if (rec.getMinOccurance() > 0 && recCount < rec.getMinOccurance()) {
					throw new ConverterException(-1, String.format("Unexpected EOF. Expected record with qualifier %s.", rec.getQualifier()));
				}
				recCount = 0;
			}
		}
	}

	/**
	 * Sanitizes the string. To be used before writing the XML characters.
	 *
	 * @param aValue
	 * @return
	 */
	protected String sanitizeValue(String aValue) {
		if (trimSpaces) {
			aValue = aValue.trim();
		}
		return aValue;
	}
}
