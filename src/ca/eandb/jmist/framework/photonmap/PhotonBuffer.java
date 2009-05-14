/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.photonmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author brad
 *
 */
public final class PhotonBuffer {

	private final ByteBuffer buffer;

	private static final int ELEMENT_SIZE = 20;

	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 4;
	private static final int OFFSET_Z = 8;
	private static final int OFFSET_POWER = 12;
	private static final int OFFSET_DIR = 16;
	private static final int OFFSET_PLANE = 18;

	public PhotonBuffer(int capacity) {
		buffer = ByteBuffer.allocateDirect(capacity * ELEMENT_SIZE);
		buffer.order(ByteOrder.nativeOrder());
	}

	public void moveTo(int index) {
		buffer.position(index * ELEMENT_SIZE);
	}

	public void store(float x, float y, float z, float power, short dir, short plane) {
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		buffer.putFloat(power);
		buffer.putShort(dir);
		buffer.putShort(plane);
	}

	public float getPosition(int index, int element) {
		return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_X + (element * 4));
	}

	public float getX(int index) {
		return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_X);
	}

	public float getY(int index) {
		return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_Y);
	}

	public float getZ(int index) {
		return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_Z);
	}

	public float getPower(int index) {
		return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_POWER);
	}

	public short getDir(int index) {
		return buffer.getShort(index * ELEMENT_SIZE + OFFSET_DIR);
	}

	public short getPlane(int index) {
		return buffer.getShort(index * ELEMENT_SIZE + OFFSET_PLANE);
	}

	public void setPower(int index, float power) {
		buffer.putFloat(index * ELEMENT_SIZE + OFFSET_POWER, power);
	}

	public void scalePower(int index, float scale) {
		int offset = index * ELEMENT_SIZE + OFFSET_POWER;
		buffer.putFloat(offset, scale * buffer.getFloat(offset));
	}

	public void setPlane(int index, short plane) {
		buffer.putShort(index * ELEMENT_SIZE + OFFSET_PLANE, plane);
	}

	public void copyPhoton(int src, int dst) {
		buffer.position(src * ELEMENT_SIZE);
		ByteBuffer slice = buffer.slice();
		slice.limit(ELEMENT_SIZE);

		buffer.position(dst * ELEMENT_SIZE);
		buffer.put(slice);
	}

}
