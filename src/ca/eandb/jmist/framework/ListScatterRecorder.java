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

package ca.eandb.jmist.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>ScatteredRayRecorder</code> that appends <code>ScatteredRay</code>s to a
 * <code>List</code>
 * @author brad
 */
public final class ListScatterRecorder implements ScatteredRayRecorder {

	/** The <code>List</code> to append <code>ScatteredRay</code>s to. */
	private final List<ScatteredRay> results;

	/**
	 * Creates a new <code>ListScatterRecorder</code>.
	 * @param results The <code>List</code> to append
	 * 		<code>ScatteredRay</code>s to
	 */
	public ListScatterRecorder(List<ScatteredRay> results) {
		this.results = results;
	}

	/**
	 * Creates a new <code>ListScatterRecorder</code>.  A <code>List</code>
	 * to store the <code>ScatteredRay</code>s is created automatically.
	 */
	public ListScatterRecorder() {
		this(new ArrayList<ScatteredRay>());
	}

	/**
	 * Obtains the <code>List</code> containing the
	 * <code>ScatteredRay</code>s.
	 * @return The <code>List</code> containing the.
	 * 		<code>ScatteredRay</code>s.
	 */
	public List<ScatteredRay> getScatterResults() {
		return results;
	}

	/**
	 * Clears the <code>List</code> of <code>ScatteredRay</code>s.
	 */
	public void clear() {
		results.clear();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ScatteredRayRecorder#record(ca.eandb.jmist.framework.ScatteredRay)
	 */
	@Override
	public void add(ScatteredRay sr) {
		results.add(sr);
	}

}
