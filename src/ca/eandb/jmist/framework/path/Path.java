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

package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.job.bidi.PathMeasure;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.UnexpectedException;

/**
 * A sequence of scattering events from the eye or from a light source used by
 * path-integral based rendering algorithms.
 * @author Brad Kimmel
 */
public final class Path {

	private final PathNode lightTail;

	private final PathNode eyeTail;

	public Path(PathNode lightTail, PathNode eyeTail) {
		if (lightTail == null && eyeTail == null) {
			throw new IllegalArgumentException(
					"lightTail == null && eyeTail == null");
		}

		this.lightTail = lightTail;
		this.eyeTail = eyeTail;
	}

	public PathNode getLightTail() {
		return lightTail;
	}

	public PathNode getEyeTail() {
		return eyeTail;
	}

	public PathInfo getPathInfo() {
		return (lightTail != null)
				? lightTail.getPathInfo()
				: eyeTail.getPathInfo();
	}

	public int getEyePathLength() {
		return (eyeTail != null) ? eyeTail.getDepth() : -1;
	}

	public int getLightPathLength() {
		return (lightTail != null) ? lightTail.getDepth() : -1;
	}

	public int getLength() {
		return getLightPathLength() + getEyePathLength() + 1;
	}

	public Point2 getPointOnImagePlane() {
		if (eyeTail == null) {
			throw new UnexpectedException();
		} else if (eyeTail.getParent() == null) {
			EyeNode eyeNode = (EyeNode) eyeTail;

			return eyeNode.project(lightTail.getPosition());
		} else { // eyeTail != null && eyeTail.getParent() != null
			PathNode firstNode = eyeTail;
			while (firstNode.getDepth() > 1) {
				firstNode = firstNode.getParent();
			}

			EyeNode eyeNode = (EyeNode) firstNode.getParent();

			return eyeNode.project(firstNode.getPosition());
		}
	}

	public Path slice(double s1, double t1) {
		int s0 = getLightPathLength();
		int t0 = getEyePathLength();

		if (s1 + t1 > s0 + t0) {
			throw new IllegalArgumentException(
					"Slice attempts to lengthen path.");
		}
		if (s1 == s0 && t1 == t0) {
			return this;
		}

		PathNode newLightTail = lightTail;
		PathNode newEyeTail = eyeTail;
		PathNode grandChild;
		
		grandChild = null;
		while (s0 < s1) {
			if (grandChild == null) {
				PathNode parent = eyeTail.getParent();
				if (parent != null && parent.getParent() == newEyeTail) {
					grandChild = eyeTail;
				}
			} else {
				grandChild = grandChild.getParent();
			}
			newLightTail = newEyeTail.reverse(newLightTail, grandChild);
			if (newLightTail == null) {
				return null;
			}
			newEyeTail = newEyeTail.getParent();
			s0++; t0--;
		}

		grandChild = null;
		while (t0 < t1) {
			if (grandChild == null) {
				PathNode parent = lightTail.getParent();
				if (parent != null && parent.getParent() == newLightTail) {
					grandChild = lightTail;
				}
			} else {
				grandChild = grandChild.getParent();
			}
			newEyeTail = newLightTail.reverse(newEyeTail, grandChild);
			if (newEyeTail == null) {
				return null;
			}
			newLightTail = newLightTail.getParent();
			s0--; t0++;
		}

		while (s0-- > s1) {
			newLightTail = newLightTail.getParent();
		}

		while (t0-- > t1) {
			newEyeTail = newEyeTail.getParent();
		}

		return new Path(newLightTail, newEyeTail);
	}

//	public Path shift(int ds) {
//		if (ds > 0) {
//			PathNode newLightTail = lightTail;
//			PathNode newEyeTail = eyeTail;
//			while (ds-- > 0) {
//				newLightTail = reverse(newEyeTail, newLightTail);
//				newEyeTail = newEyeTail.getParent();
//			}
//			return new Path(newLightTail, newEyeTail);
//		} else if (ds < 0) {
//			PathNode newLightTail = lightTail;
//			PathNode newEyeTail = eyeTail;
//			while (ds++ < 0) {
//				newEyeTail = reverse(newLightTail, newEyeTail);
//				newLightTail = newLightTail.getParent();
//			}
//			return new Path(newLightTail, newEyeTail);
//		} else {
//			return this;
//		}
//	}

//	private PathNode reverse(PathNode node, PathNode newParent) {
//		throw new UnimplementedException();
//	}
//	
	public PathNode[] toPathNodes() {
		int k = getLength();
		PathNode[] nodes = new PathNode[k + 1];
		int i;
		
		if (lightTail != null) {
			i = lightTail.getDepth();
			for (PathNode lightNode = lightTail; lightNode != null;
					lightNode = lightNode.getParent()) {
				nodes[i--] = lightNode;
			}
		}
		
		if (eyeTail != null) {
			i = (lightTail != null) ? lightTail.getDepth() + 1 : 0; 
			for (PathNode eyeNode = eyeTail; eyeNode != null;
					eyeNode = eyeNode.getParent()) {
				nodes[i++] = eyeNode;
			}
		}
		
		return nodes;
	}
	
	public Color getUnweightedContribution() {
		if (lightTail != null && eyeTail != null) {
			return PathUtil.join(lightTail, eyeTail);
		} else if (lightTail == null && eyeTail instanceof ScatteringNode) {
			ScatteringNode scatNode = (ScatteringNode) eyeTail;
			return scatNode.getCumulativeWeight().times(
					scatNode.getSourceRadiance());
		} else {
			PathInfo pi = getPathInfo();
			return pi.getColorModel().getBlack(pi.getWavelengthPacket());
		}
	}
	
	public Color measure(PathMeasure m) {
		return m.evaluate(lightTail, eyeTail);
	}

}
