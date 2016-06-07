/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.mpobjects.formats.sofalife.util.Utils;

/**
 * Specification of a structured fixed length format.
 */
public class FormatSpec {
	protected String description;

	protected Charset encoding;

	protected String id;

	protected String name;

	protected List<RecordSpec> records;

	protected String version;

	public FormatSpec() {
		setEncoding("utf-8");
		records = new ArrayList<RecordSpec>();
		setName("sofalife");
		setId("urn:sofalife:specification:unknown");
		setVersion("0.1");
	}

	/**
	 * @return Description of the format.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Character set the input will be read in. Defaults to utf-8.
	 */
	public Charset getEncoding() {
		return encoding;
	}

	/**
	 * @return Identifier of this format
	 */
	public String getId() {
		return id;
	}

	/**
	 * Name of the record, this will be used as element name in the generated XML. Should conform to the NCName XML data
	 * type.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Root records defined in the file.
	 * 
	 * @return
	 */
	public List<RecordSpec> getRecords() {
		return records;
	}

	/**
	 * Version of the specification.
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setDescription(String aDescription) {
		description = aDescription;
	}

	public void setEncoding(Charset aEncoding) {
		encoding = aEncoding;
	}

	public void setEncoding(String aEncoding) {
		setEncoding(Charset.forName(aEncoding));
	}

	public void setId(String aId) {
		id = aId;
	}

	public void setName(String aName) {
		Validate.notEmpty(aName, "Name cannot be empty.");
		Validate.isTrue(Utils.isNCName(aName), "Not a valid NCName value: '%s'", aName);
		name = aName;
	}

	public void setRecords(List<RecordSpec> aRecords) {
		records = aRecords;
	}

	public void setVersion(String aVersion) {
		version = aVersion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(" version=");
		sb.append(version);
		return sb.toString();
	}

}
