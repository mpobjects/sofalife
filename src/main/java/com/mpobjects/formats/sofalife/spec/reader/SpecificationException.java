/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.spec.reader;

/**
 * Exception thrown in case of errors while parsing the specification.
 */
public class SpecificationException extends Exception {

	private static final long serialVersionUID = 1L;

	public SpecificationException(String aMessage) {
		super(aMessage);
	}

	public SpecificationException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

}
