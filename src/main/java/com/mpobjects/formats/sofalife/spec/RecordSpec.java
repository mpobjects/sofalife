/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.mpobjects.formats.sofalife.util.Utils;

/**
 * A record in the format.
 */
public class RecordSpec {
	protected List<FieldSpec> fields;

	protected int maxOccurance;

	protected int minOccurance;

	protected String name;

	protected String qualifier;

	protected List<RecordSpec> records;

	public RecordSpec(String aQualifier) {
		fields = new ArrayList<FieldSpec>();
		records = new ArrayList<RecordSpec>();
		minOccurance = 1;
		maxOccurance = -1;
		setQualifier(aQualifier);
		setName(Utils.convertToNCName(aQualifier));
	}

	/**
	 * @return fields defined in this record.
	 */
	public List<FieldSpec> getFields() {
		return fields;
	}

	/**
	 * The maximum subsequent occurrences of this record. If -1 there is no maximum.
	 * 
	 * @return
	 */
	public int getMaxOccurance() {
		return maxOccurance;
	}

	/**
	 * The minimum number of occurences of this record. If -1 there is no minimum.
	 * 
	 * @return
	 */
	public int getMinOccurance() {
		return minOccurance;
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
	 * The leading sequence of characters which defines this type of record.
	 * 
	 * @return
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * @return Child records
	 */
	public List<RecordSpec> getRecords() {
		return records;
	}

	public void setFields(List<FieldSpec> aFields) {
		fields = aFields;
	}

	public void setMaxOccurance(int aMaxOccuracnce) {
		maxOccurance = aMaxOccuracnce;
		if (maxOccurance < -1) {
			maxOccurance = -1;
		}
	}

	public void setMinOccurance(int aMinOccurance) {
		minOccurance = aMinOccurance;
		if (minOccurance < -1) {
			minOccurance = -1;
		}
	}

	public void setName(String aName) {
		Validate.notEmpty(aName, "Name cannot be empty");
		Validate.isTrue(Utils.isNCName(aName), "Not a valid NCName value: '%s'", aName);
		name = aName;
	}

	public void setQualifier(String aQualifier) {
		Validate.notEmpty(aQualifier, "Qualifier is required");
		qualifier = aQualifier;
	}

	public void setRecords(List<RecordSpec> aRecords) {
		records = aRecords;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		return sb.toString();
	}
}
