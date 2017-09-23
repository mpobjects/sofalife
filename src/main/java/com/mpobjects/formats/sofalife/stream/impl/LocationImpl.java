package com.mpobjects.formats.sofalife.stream.impl;

import javax.xml.stream.Location;

public class LocationImpl implements Location {

	private String publicId;

	private String systemId;

	private int lineNumber;

	private int columnNumber;

	private int characterOffset;

	public LocationImpl(String aPublicId, String aSystemId) {
		publicId = aPublicId;
		systemId = aSystemId;
	}

	public void setCharacterOffset(int aCharacterOffset) {
		characterOffset = aCharacterOffset;
	}

	public void setColumnNumber(int aColumnNumber) {
		columnNumber = aColumnNumber;
	}

	public void setLineNumber(int aLineNumber) {
		lineNumber = aLineNumber;
	}

	public void incCharacterOffset(int aCharacterOffset) {
		characterOffset += aCharacterOffset;
	}

	public void incColumnNumber(int aColumnNumber) {
		columnNumber += aColumnNumber;
	}

	public void incLineNumber(int aLineNumber) {
		lineNumber += aLineNumber;
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public int getColumnNumber() {
		return columnNumber;
	}

	@Override
	public int getCharacterOffset() {
		return characterOffset;
	}

	@Override
	public String getPublicId() {
		return publicId;
	}

	@Override
	public String getSystemId() {
		return systemId;
	}

}
