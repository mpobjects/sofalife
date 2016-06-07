/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife;

public enum ProcessingMode {
	/**
	 * If there is not enough data in a record the required fields will be assumed blank.
	 */
	LENIENT,
	/**
	 * If there is not enough data in a record for a non-optional field an exception will be thrown and processing will
	 * stop.
	 */
	STRICT,
	/**
	 * Also fail processing in case of extranous data at the end of a record.
	 */
	VERY_STRICT;
}
