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

package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>Scene</code> that is based on another scene.
 * @author Brad Kimmel
 */
public abstract class SceneDecorator implements Scene {

	/** Serialization version ID. */
	private static final long serialVersionUID = 5875837932212956822L;
	
	/** The <code>Scene</code> that this scene is to be based on. */
	private final Scene inner;

	/**
	 * Initializes this <code>SceneDecorator</code>.
	 * @param inner The <code>Scene</code> that this scene is to be based on.
	 */
	protected SceneDecorator(Scene inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getLens()
	 */
	public Lens getLens() {
		return inner.getLens();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getLight()
	 */
	public Light getLight() {
		return inner.getLight();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getRoot()
	 */
	public SceneElement getRoot() {
		return inner.getRoot();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return inner.boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return inner.boundingSphere();
	}

}
