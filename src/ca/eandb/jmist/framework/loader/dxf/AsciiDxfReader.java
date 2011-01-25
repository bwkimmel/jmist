/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;


/**
 * @author Brad
 *
 */
public final class AsciiDxfReader implements DxfReader {
	
	private static final class AsciiDxfException extends DxfException {
		
		/** Serialization version ID. */
		private static final long serialVersionUID = -360078728659801927L;

		/**
		 * @param message
		 * @param cause
		 */
		public AsciiDxfException(int lineNumber, String message, Throwable cause) {
			super(String.format("%d: %s", lineNumber, message), cause);
		}

		/**
		 * @param message
		 */
		public AsciiDxfException(int lineNumber, String message) {
			super(String.format("%d: %s", lineNumber, message));
		}
		
	}
	
	/**
	 * @author Brad
	 *
	 */
	private static final class AsciiDxfElement extends AbstractDxfElement {
		
		private final String valueLine;
		
		private final int lineNumber;

		/**
		 * @param groupCode
		 */
		public AsciiDxfElement(int groupCode, String valueLine, int lineNumber) {
			super(groupCode);
			this.valueLine = valueLine;
			this.lineNumber = lineNumber;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getBooleanValue()
		 */
		@Override
		public boolean getBooleanValue() {
			try {
				return Integer.parseInt(valueLine) != 0;
			} catch (NumberFormatException e) {
				throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getFloatValue()
		 */
		@Override
		public double getFloatValue() {
			try {
				return Double.parseDouble(valueLine);
			} catch (NumberFormatException e) {
				throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getIntegerValue()
		 */
		@Override
		public int getIntegerValue() {
			try {
				return Integer.parseInt(valueLine);
			} catch (NumberFormatException e) {
				throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getLongValue()
		 */
		@Override
		public long getLongValue() {
			try {
				return Long.parseLong(valueLine);
			} catch (NumberFormatException e) {
				throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getStringValue()
		 */
		@Override
		public String getStringValue() {
			return valueLine;
		}

	}

	private final LineNumberReader reader;
	
	private DxfElement currentElement;
	
	private boolean eof = false;
	
	public AsciiDxfReader(Reader reader) {
		this.reader = new LineNumberReader(reader);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfReader#getCurrentElement()
	 */
	@Override
	public synchronized DxfElement getCurrentElement() {
		return currentElement;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfReader#advance()
	 */
	@Override
	public synchronized void advance() throws DxfException {
		if (eof) {
			currentElement = null;
			return;
		}
		
		int ln = reader.getLineNumber();
		
		String groupCodeLine, valueLine;
		try {
			groupCodeLine = reader.readLine();
			valueLine = reader.readLine();
		} catch (IOException e) {
			throw new AsciiDxfException(ln, "Error reading DXF stream", e);
		}
	
		if (groupCodeLine == null || valueLine == null) {
			throw new AsciiDxfException(ln, "Unexpected end of file");
		}
		
		int groupCode;
		try {
			groupCode = Integer.parseInt(groupCodeLine);
		} catch (NumberFormatException e) {
			throw new AsciiDxfException(ln, "Invalid group code", e);
		}
		
		if (valueLine.equals("EOF")) {
			eof = true;
		}

		currentElement = new AsciiDxfElement(groupCode, valueLine, ln);
	}

}
