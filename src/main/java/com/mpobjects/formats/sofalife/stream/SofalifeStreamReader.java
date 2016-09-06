/*
 * Copyright 2016, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.formats.sofalife.stream;

import com.mpobjects.formats.sofalife.spec.FieldSpec;
import com.mpobjects.formats.sofalife.spec.RecordSpec;

/**
 * A StAX-like reader of sofalife files.
 */
public interface SofalifeStreamReader extends SofalifeStreamConstants {
	/**
	 * Return the current event type
	 *
	 * @return the current event type
	 */
	int getEventType();

	/**
	 * Return the current {@link FieldSpec}. Only valid for field events.
	 *
	 * @return the active {@link FieldSpec}
	 */
	FieldSpec getFieldSpec();

	/**
	 * Returns the name of the current element. If it is a record event is returns {@link RecordSpec#getName()}, for a
	 * field event it returns {@link FieldSpec#getName()}. For other events it will return null.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Return the current {@link RecordSpec}. Only valid for record and field events.
	 *
	 * @return the active {@link RecordSpec}
	 */
	RecordSpec getRecordSpec();

	/**
	 * Returns the text value of the current event. For field events and record events it will return the read content.
	 * For a comment event is returns the comment without the defined prefix.
	 *
	 * @return the text value
	 */
	String getText();

	/**
	 * @return true if there is more to read
	 */
	boolean hasNext();

	/**
	 * Return the next event
	 *
	 * @return the event type read
	 */
	int next();
}
