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

import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PhotonMap {

	private final PhotonBuffer photons;
	private int leafStart;
	private int storedPhotons = 0;
	private final float[] bbox_min = { Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY };
	private final float[] bbox_max = { Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY };

	public PhotonMap(int capacity) {
		photons = new PhotonBuffer(capacity + 1);
		photons.moveTo(1);
	}

	public void store(Point3 position, Vector3 direction, double power) {
		float x = (float) position.x();
		float y = (float) position.y();
		float z = (float) position.z();
		photons.store(x, y, z, (float) power, direction.toCompactDirection(),
				(short) 0);
		if (x > bbox_max[0]) bbox_max[0] = x;
		if (y > bbox_max[1]) bbox_max[1] = y;
		if (z > bbox_max[2]) bbox_max[2] = z;
		if (x < bbox_min[0]) bbox_min[0] = x;
		if (y < bbox_min[1]) bbox_min[1] = y;
		if (z < bbox_min[2]) bbox_min[2] = z;
		storedPhotons++;
	}

	public void balance() {
		int[] pa1 = new int[storedPhotons + 1];
		int[] pa2 = new int[storedPhotons + 1];

		for (int i = 0; i <= storedPhotons; i++) {
			pa2[i] = i;
		}

		balanceSegment(pa1, pa2, 1, 1, storedPhotons);

		int d, j = 1, foo = 1;
		photons.copyPhoton(j, 0);
		for (int i = 1; i <= storedPhotons; i++) {
			d = pa1[j];
			pa1[j] = 0;
			if (d != foo) {
				photons.copyPhoton(d, j);
			} else {
				photons.copyPhoton(0, j);

				if (i < storedPhotons) {
					for (; foo <= storedPhotons; foo++) {
						if (pa1[foo] != 0) {
							break;
						}
					}
					photons.copyPhoton(foo, 0);
					j = foo;
				}
				continue;
			}
			j = d;
		}

		leafStart = storedPhotons / 2 - 1;
	}

	public void scalePhotons(double scale) {
		for (int i = 1; i <= storedPhotons; i++) {
			photons.scalePower(i, (float) scale);
		}
	}

	public double getIrradianceEstimate(Point3 position, Vector3 normal, double maxDistance, int numPhotons) {
		NearestPhotons np = new NearestPhotons();
		np.squaredDistance = new float[numPhotons + 1];
		np.index = new int[numPhotons + 1];
		np.x = (float) position.x();
		np.y = (float) position.y();
		np.z = (float) position.z();
		np.maximum = numPhotons;
		np.found = 0;
		np.gotHeap = false;
		np.squaredDistance[0] = (float) (maxDistance * maxDistance);

		locatePhotons(np, 1);

		if (np.found < 8) {
			return 0.0;
		}

		double irrad = 0.0;

		for (int i = 1; i < np.found; i++) {
			int index = np.index[i];
			Vector3 pdir = Vector3.fromCompactDirection(photons.getDir(index));
			if (pdir.dot(normal) < 0.0) {
				irrad += photons.getPower(index);
			}
		}

		irrad *= (1.0 / Math.PI) / np.squaredDistance[0];
		return irrad;
	}

	private void locatePhotons(NearestPhotons np, int index) {
		float dist1;

		if (index < leafStart) {
			short plane = photons.getPlane(index);
			float pos = photons.getPosition(index, plane);
			dist1 = np.getPosition(plane) - pos;

			if (dist1 > 0.0) {
				locatePhotons(np, 2 * index + 1);
				if (dist1 * dist1 < np.squaredDistance[0]) {
					locatePhotons(np, 2 * index);
				}
			} else {
				locatePhotons(np, 2 * index);
				if (dist1 * dist1 < np.squaredDistance[0]) {
					locatePhotons(np, 2 * index + 1);
				}
			}
		}

		dist1 = photons.getX(index) - np.x;
		float dist2 = dist1 * dist1;
		dist1 = photons.getY(index) - np.y;
		dist2 += dist1 * dist1;
		dist1 = photons.getZ(index) - np.z;
		dist2 += dist1 * dist1;

		if (dist2 < np.squaredDistance[0]) {
			if (np.found < np.maximum) {
				np.found++;
				np.squaredDistance[np.found] = dist2;
				np.index[np.found] = index;
			} else {
				int j, parent;

				if (!np.gotHeap) {
					float dst2;
					int phot;
					int half_found = np.found / 2;
					for (int k = half_found; k >= 1; k--) {
						parent = k;
						phot = np.index[k];
						dst2 = np.squaredDistance[k];
						while (parent <= half_found) {
							j = 2 * parent;
							if (j < np.found && np.squaredDistance[j] < np.squaredDistance[j + 1]) {
								j++;
							}
							if (dst2 >= np.squaredDistance[j]) {
								break;
							}
							np.squaredDistance[parent] = np.squaredDistance[j];
							np.index[parent] = np.index[j];
							parent = j;
						}
						np.squaredDistance[parent] = dst2;
						np.index[parent] = phot;
					}
					np.gotHeap = true;
				}

				parent = 1;
				j = 2;
				while (j <= np.found) {
					if (j < np.found && np.squaredDistance[j] < np.squaredDistance[j + 1]) {
						j++;
					}
					if (dist2 > np.squaredDistance[j]) {
						break;
					}
					np.squaredDistance[parent] = np.squaredDistance[j];
					np.index[parent] = np.index[j];
					parent = j;
					j *= 2;
				}

				np.index[parent] = index;
				np.squaredDistance[parent] = dist2;

				np.squaredDistance[0] = np.squaredDistance[1];
			}
		}
	}

	private void medianSplit(int[] p, int start, int end, int median, int axis) {
		int left = start;
		int right = end;

		while (right > left) {
			float v = photons.getPosition(p[right], axis);
			int i = left - 1;
			int j = right;
			for (;;) {
				while (photons.getPosition(p[++i], axis) < v);
				while (photons.getPosition(p[--j], axis) > v && j > left);
				if (i >= j) {
					break;
				}
				swap(p, i, j);
			}

			swap(p, i, right);
			if (i >= median) {
				right = i - 1;
			}
			if (i <= median) {
				left = i + 1;
			}
		}
	}

	private void swap(int[] p, int i, int j) {
		int tmp = p[i];
		p[i] = p[j];
		p[j] = tmp;
	}

	private void balanceSegment(int[] pbal, int[] porg, int index, int start, int end) {
		int median = 1;
		while ((4 * median) <= (end - start + 1)) {
			median += median;
		}

		if ((3 * median) <= (end - start + 1)) {
			median += median;
			median += start - 1;
		} else {
			median = end - median + 1;
		}

		short axis = 2;
		if ((bbox_max[0] - bbox_min[0]) > (bbox_max[1] - bbox_min[1]) && (bbox_max[0] - bbox_min[0]) > (bbox_max[2] - bbox_min[2])) {
			axis = 0;
		} else if ((bbox_max[1] - bbox_min[1]) > (bbox_max[2] - bbox_min[2])) {
			axis = 1;
		}

		medianSplit(porg, start, end, median, axis);

		pbal[index] = porg[median];
		photons.setPlane(pbal[index], axis);

		if (median > start) {
			if (start < median - 1) {
				float tmp = bbox_max[axis];
				bbox_max[axis] = photons.getPosition(pbal[index], axis);
				balanceSegment(pbal, porg, 2 * index, start, median - 1);
				bbox_max[axis] = tmp;
			} else {
				pbal[2 * index] = porg[start];
			}
		}

		if (median < end) {
			if (median + 1 < end) {
				float tmp = bbox_min[axis];
				bbox_min[axis] = photons.getPosition(pbal[index], axis);
				balanceSegment(pbal, porg, 2 * index + 1, median + 1, end);
				bbox_min[axis] = tmp;
			} else {
				pbal[2 * index + 1] = porg[end];
			}
		}
	}

	private final class NearestPhotons {
		private int maximum;
		private int found;
		private boolean gotHeap;
		private float x, y, z;
		private float[] squaredDistance;
		private int[] index;

		public float getPosition(int axis) {
			switch (axis) {
			case 0: return x;
			case 1: return y;
			case 2: return z;
			default: return Float.NaN;
			}
		}

	}

}
