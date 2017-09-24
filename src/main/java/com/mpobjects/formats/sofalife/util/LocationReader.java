package com.mpobjects.formats.sofalife.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.stream.Location;

/**
 * {@link BufferedReader} which keeps track of the current read position
 *
 */
public class LocationReader extends BufferedReader {

	protected class ReaderLocation implements Location {
		private int characterOffset;

		private int columnNumber = 1;

		private int lineNumber = 1;

		private String publicId;

		private String systemId;

		private boolean skipLf;

		public ReaderLocation() {
		}

		public ReaderLocation(ReaderLocation aLocation) {
			publicId = aLocation.publicId;
			systemId = aLocation.systemId;
			lineNumber = aLocation.lineNumber;
			columnNumber = aLocation.columnNumber;
			characterOffset = aLocation.characterOffset;
			skipLf = aLocation.skipLf;
		}

		public int getCharacterOffset() {
			return characterOffset;
		}

		public int getColumnNumber() {
			return columnNumber;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public String getPublicId() {
			return publicId;
		}

		public String getSystemId() {
			return systemId;
		}
	}

	private ReaderLocation location;

	private ReaderLocation markedLocation;

	public LocationReader(Reader aIn, int aBufferSize, String aPublicId, String aSystemId) {
		super(aIn, aBufferSize);
		createLocation(aPublicId, aSystemId);
	}

	public LocationReader(Reader aIn, String aPublicId, String aSystemId) {
		super(aIn);
		createLocation(aPublicId, aSystemId);
	}

	/**
	 * Returns the location information since the last read action.
	 * 
	 * @return the current location
	 */
	public Location getLocation() {
		return location;
	}

	protected void createLocation(String aPublicId, String aSystemId) {
		location = new ReaderLocation();
		location.publicId = aPublicId;
		location.systemId = aSystemId;
	}

	@Override
	public String readLine() throws IOException {
		synchronized (lock) {
			int c = -1;
			if (location.skipLf) {
				c = read();
				if (c == '\n') {
					c = -2;
				}
			}
			location.skipLf = false;

			String line = super.readLine();
			if (c >= 0) {
				line = ((char) c) + line;
			}
			if (line != null) {
				// FIXME: this will be wrong when it was the last line
				location.lineNumber++;
				location.columnNumber = 1;
				location.characterOffset += line.length() + 1;
			}

			return line;
		}
	}

	@Override
	public int read() throws IOException {
		synchronized (lock) {
			int c = super.read();
			if (location.skipLf) {
				location.skipLf = false;
				if (c == '\n') {
					location.characterOffset++;
					c = super.read();
				}
			}
			switch (c) {
				case -1:
					break;
				case '\r':
					location.skipLf = true;
					// fall through
				case '\n':
					location.lineNumber++;
					location.columnNumber = 1;
					location.characterOffset++;
					break;
				default:
					location.columnNumber++;
					location.characterOffset++;
			}
			return c;
		}
	}

	@Override
	public int read(char[] aCbuf, int aOff, int aLen) throws IOException {
		synchronized (lock) {
			final int cnt = super.read(aCbuf, aOff, aLen);
			for (int i = aOff; i < aOff + cnt; ++i) {
				if (location.skipLf) {
					location.skipLf = false;
					if (aCbuf[i] == '\n') {
						continue;
					}
				}
				switch (aCbuf[i]) {
					case '\r':
						location.skipLf = true;
						// fall through
					case '\n':
						location.lineNumber++;
						location.columnNumber = 1;
						break;
					default:
						location.columnNumber++;
				}
			}
			if (cnt > 0) {
				location.characterOffset += cnt;
			}
			return cnt;
		}
	}

	@Override
	public long skip(long aN) throws IOException {
		if (aN < 0) {
			throw new IllegalArgumentException("skip() value is negative");
		}
		synchronized (lock) {
			final int bufSize = (int) Math.min(aN, 8192);
			char[] buf = new char[bufSize];
			long remaining = aN;
			while (remaining > 0) {
				int cnt = read(buf, 0, (int) Math.min(remaining, bufSize));
				if (cnt == -1) {
					break;
				}
				remaining -= cnt;
			}
			return aN - remaining;
		}
	}

	@Override
	public void mark(int aReadAheadLimit) throws IOException {
		synchronized (lock) {
			super.mark(aReadAheadLimit);
			markedLocation = new ReaderLocation(location);
		}
	}

	@Override
	public void reset() throws IOException {
		synchronized (lock) {
			super.reset();
			location = markedLocation;
		}
	}
}
