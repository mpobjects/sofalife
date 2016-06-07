/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec;

import org.apache.commons.lang3.Validate;

import com.mpobjects.formats.sofalife.util.Utils;

/**
 * Specification for a single field in a record.
 */
public class FieldSpec {

	protected String description;

	protected int length;

	protected String name;

	protected boolean optional;

	public FieldSpec(String aName, int aLength) {
		setName(aName);
		setLength(aLength);
	}

	/**
	 * @return description for the field (if defined).
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the length of the field.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Name of the field, this will be used as element name in the generated XML. Should conform to the NCName XML data
	 * type.
	 * 
	 * @return the field name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Optional fields might not be present in the input, and will not be included in generated output. If a field is
	 * optional, all subsequent fields in the record should also be optional.
	 * 
	 * @return True if this field is not required to be present.
	 */
	public boolean isOptional() {
		return optional;
	}

	public void setDescription(String aDescription) {
		description = aDescription;
	}

	public void setLength(int aLength) {
		if (aLength <= 0) {
			throw new IllegalArgumentException("Length cannot be 0 or less.");
		}
		length = aLength;
	}

	public void setName(String aName) {
		Validate.notEmpty(aName, "Name cannot be empty");
		Validate.isTrue(Utils.isNCName(aName), "Not a valid NCName value: '%s'", aName);
		name = aName;
	}

	public void setOptional(boolean aOptional) {
		optional = aOptional;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("[");
		sb.append(length);
		sb.append("]");
		if (optional) {
			sb.append("?");
		}
		return sb.toString();
	}
}
