package com.mpobjects.formats.sofalife.stream.impl;

import javax.xml.stream.Location;

public class LocationImpl implements Location {

	private int characterOffset = -1;

	private int columnNumber = -1;

	private int lineNumber = -1;

	private String publicId;

	private String systemId;

	public LocationImpl(Location aLocation) {
		publicId = aLocation.getPublicId();
		systemId = aLocation.getSystemId();
		lineNumber = aLocation.getLineNumber();
		columnNumber = aLocation.getColumnNumber();
		characterOffset = aLocation.getCharacterOffset();
	}

	public LocationImpl(String aPublicId, String aSystemId) {
		publicId = aPublicId;
		systemId = aSystemId;
	}

	@Override
	public int getCharacterOffset() {
		return characterOffset;
	}

	@Override
	public int getColumnNumber() {
		return columnNumber;
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public String getPublicId() {
		return publicId;
	}

	@Override
	public String getSystemId() {
		return systemId;
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

}
