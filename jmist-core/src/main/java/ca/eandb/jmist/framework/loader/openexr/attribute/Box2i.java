/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * @author brad
 *
 */
@OpenEXRAttributeType("box2i")
public final class Box2i implements Attribute {
	
	private final int xMin;
	private final int yMin;
	private final int xMax;
	private final int yMax;
	
	public Box2i(int xMin, int yMin, int xMax, int yMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	/**
	 * @return the xMin
	 */
	public int getXMin() {
		return xMin;
	}

	/**
	 * @return the yMin
	 */
	public int getYMin() {
		return yMin;
	}

	/**
	 * @return the xMax
	 */
	public int getXMax() {
		return xMax;
	}

	/**
	 * @return the yMax
	 */
	public int getYMax() {
		return yMax;
	}
	
	public int getXSize() {
		return xMax - xMin + 1;
	}
	
	public int getYSize() {
		return yMax - yMin + 1;
	}

	public static Box2i read(DataInput in, int size) throws IOException {
		return new Box2i(in.readInt(), in.readInt(), in.readInt(), in.readInt());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(xMin);
		out.writeInt(yMin);
		out.writeInt(xMax);
		out.writeInt(yMax);
	}
	
}
