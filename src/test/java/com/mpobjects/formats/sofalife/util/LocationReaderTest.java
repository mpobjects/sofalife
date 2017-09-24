package com.mpobjects.formats.sofalife.util;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class LocationReaderTest {

	@Test
	public void testRead() throws Exception {
		LocationReader reader = new LocationReader(new StringReader("1\n2\n3"), null, null);

		Assert.assertEquals(1, reader.getLocation().getLineNumber());
		Assert.assertEquals(1, reader.getLocation().getColumnNumber());
		Assert.assertEquals(0, reader.getLocation().getCharacterOffset());

		Assert.assertEquals('1', reader.read());
		Assert.assertEquals(1, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(1, reader.getLocation().getCharacterOffset());

		Assert.assertEquals('\n', reader.read());
		Assert.assertEquals(2, reader.getLocation().getLineNumber());
		Assert.assertEquals(1, reader.getLocation().getColumnNumber());
		Assert.assertEquals(2, reader.getLocation().getCharacterOffset());

		Assert.assertEquals('2', reader.read());
		Assert.assertEquals(2, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(3, reader.getLocation().getCharacterOffset());

		Assert.assertEquals('\n', reader.read());
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(1, reader.getLocation().getColumnNumber());
		Assert.assertEquals(4, reader.getLocation().getCharacterOffset());

		Assert.assertEquals('3', reader.read());
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(5, reader.getLocation().getCharacterOffset());

		Assert.assertEquals(-1, reader.read());
	}

	@Test
	public void testRead2() throws Exception {
		LocationReader reader = new LocationReader(new StringReader("1\n2\n3"), null, null);

		char[] buf = new char[2];
		Assert.assertEquals(2, reader.read(buf));
		Assert.assertEquals(2, reader.getLocation().getLineNumber());
		Assert.assertEquals(1, reader.getLocation().getColumnNumber());
		Assert.assertEquals(2, reader.getLocation().getCharacterOffset());

		Assert.assertEquals(2, reader.read(buf));
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(1, reader.getLocation().getColumnNumber());
		Assert.assertEquals(4, reader.getLocation().getCharacterOffset());

		Assert.assertEquals(1, reader.read(buf));
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(5, reader.getLocation().getCharacterOffset());
	}
	
	@Test
	public void testRead3() throws Exception {
		LocationReader reader = new LocationReader(new StringReader("1\n2\n3"), null, null);

		char[] buf = new char[5];
		Assert.assertEquals(5, reader.read(buf));
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(5, reader.getLocation().getCharacterOffset());
	}
	
	@Test
	public void testRead4() throws Exception {
		LocationReader reader = new LocationReader(new StringReader("1\r\n2\r\n3"), null, null);

		char[] buf = new char[7];
		Assert.assertEquals(7, reader.read(buf));
		Assert.assertEquals(3, reader.getLocation().getLineNumber());
		Assert.assertEquals(2, reader.getLocation().getColumnNumber());
		Assert.assertEquals(7, reader.getLocation().getCharacterOffset());
	}
}
