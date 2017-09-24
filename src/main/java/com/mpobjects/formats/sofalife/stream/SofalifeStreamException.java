package com.mpobjects.formats.sofalife.stream;

import javax.xml.stream.Location;

public class SofalifeStreamException extends Exception {

	private static final long serialVersionUID = 1L;

	protected Location location;

	public SofalifeStreamException(String aMessage) {
		super(aMessage);
	}

	public SofalifeStreamException(String aMessage, Location aLocation) {
		super(aMessage);
		location = aLocation;
	}

	public SofalifeStreamException(String aMessage, Location aLocation, Throwable aCause) {
		super(aMessage, aCause);
		location = aLocation;
	}

	public SofalifeStreamException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	/**
	 * Gets the location where the problem occurred. May be null when a location is not available
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}
}
