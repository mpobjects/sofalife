/*
 * Copyright 2014, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.util;

/**
 *
 */
public final class Utils {

	private Utils() {
	}

	/**
	 * Remove all values from the input to conform to the NCName requirement.
	 *
	 * @param aValue
	 * @return
	 */
	public static String convertToNCName(String aValue) {
		if (aValue == null) {
			return null;
		}
		return aValue.replaceAll("[^a-zA-Z0-9.-]+", "_");
	}

	/**
	 * Verifies if the provided value is a valid NCName value.
	 *
	 * @param aValue
	 * @return
	 */
	public static boolean isNCName(String aValue) {
		if (aValue == null) {
			return true;
		}
		return aValue.matches("^[a-zA-Z0-9._-]+$");
	}
}
