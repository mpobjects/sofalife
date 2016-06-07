/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife;

/**
 *
 */
public class ConverterException extends Exception {

	protected int lineNumber = -1;

	public ConverterException(int aLineNumber, String aMessage) {
		super(aMessage);
		lineNumber = aLineNumber;
	}

	public ConverterException(int aLineNumber, String aMessage, Throwable aCause) {
		super(aMessage, aCause);

	}

	/**
	 * Line number where the error occurred, or -1 if unknown.
	 * 
	 * @return
	 */
	public int getLineNumber() {
		return lineNumber;
	}
}
