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
 * A <code>ScatterRecorder</code> that appends <code>ScatterResult</code>s to a
 * <code>List</code>
 * @author brad
 */
public final class ListScatterRecorder implements ScatterRecorder {

	/** The <code>List</code> to append <code>ScatterResult</code>s to. */
	private final List<ScatterResult> results;

	/**
	 * Creates a new <code>ListScatterRecorder</code>.
	 * @param results The <code>List</code> to append
	 * 		<code>ScatterResult</code>s to
	 */
	public ListScatterRecorder(List<ScatterResult> results) {
		this.results = results;
	}

	/**
	 * Creates a new <code>ListScatterRecorder</code>.  A <code>List</code>
	 * to store the <code>ScatterResult</code>s is created automatically.
	 */
	public ListScatterRecorder() {
		this(new ArrayList<ScatterResult>());
	}

	/**
	 * Obtains the <code>List</code> containing the
	 * <code>ScatterResult</code>s.
	 * @return The <code>List</code> containing the.
	 * 		<code>ScatterResult</code>s.
	 */
	public List<ScatterResult> getScatterResults() {
		return results;
	}

	/**
	 * Clears the <code>List</code> of <code>ScatterResult</code>s.
	 */
	public void clear() {
		results.clear();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ScatterRecorder#record(ca.eandb.jmist.framework.ScatterResult)
	 */
	@Override
	public void record(ScatterResult sr) {
		results.add(sr);
	}

}
