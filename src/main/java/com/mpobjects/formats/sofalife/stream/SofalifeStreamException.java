package com.mpobjects.formats.sofalife.stream;

import javax.xml.stream.Location;

public class SofalifeStreamException extends Exception {

	private static final long serialVersionUID = 1L;

	protected Location location;

	public SofalifeStreamException(String aMessage) {
		super(aMessage);
	}

	public SofalifeStreamException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public Location getLocation() {
		return location;
	}
}
