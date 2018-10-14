/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
 * Stores photons in a kd-tree data structure.  This implementation is based on
 * Appendix B of H.W. Jensen, "Realistic Image Synthesis using Photon Mapping".
 * @author Brad Kimmel
 */
public final class PhotonMap {

  /**
   * A compact array for storing photons.
   * TODO Use <code>PhotonBuffer</code> interface.
   */
  private final CompactPhotonBuffer photons;

  /**
   * The photons are stored in an array representing a balanced binary tree.
   * This value indicates the index of the first leaf node.
   */
  private int leafStart;

  /** The number of photons in the photon map. */
  private int storedPhotons = 0;

  /**
   * The coordinates of the corner of the bounding box that is closest to the
   * origin.
   */
  private final double[] bbox_min = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };

  /**
   * The coordinates of the corner of the bounding box that is farthest from
   * the origin.
   */
  private final double[] bbox_max = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };

  /**
   * Creates a new photon map with the capacity to store a given number of
   * photons.
   * @param capacity The number of photons to allocate storage for.
   */
  public PhotonMap(int capacity) {
    photons = new CompactPhotonBuffer(capacity + 1);
    photons.moveTo(1);
  }

  /**
   * Adds a photon to the photon map.
   * @param position The <code>Point3</code> representing the location of the
   *     photon.
   * @param direction The <code>Vector3</code> representing the direction of
   *     the photon.
   * @param power The power of the photon.
   */
  public void store(Point3 position, Vector3 direction, double power) {
    double x = position.x();
    double y = position.y();
    double z = position.z();
    photons.store(position, power, direction, (short) 0);
    if (x > bbox_max[0]) bbox_max[0] = x;
    if (y > bbox_max[1]) bbox_max[1] = y;
    if (z > bbox_max[2]) bbox_max[2] = z;
    if (x < bbox_min[0]) bbox_min[0] = x;
    if (y < bbox_min[1]) bbox_min[1] = y;
    if (z < bbox_min[2]) bbox_min[2] = z;
    storedPhotons++;
  }

  /**
   * Creates a balanced tree from the stored photons.  New photons may not be
   * added after this method is called.
   */
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

  /**
   * Scales the power of all photons by a given power.
   * @param scale The factor by which to scale the power of all the photons.
   */
  public void scalePhotons(double scale) {
    for (int i = 1; i <= storedPhotons; i++) {
      photons.scalePower(i, scale);
    }
  }

  /**
   * Estimate the irradiance ad a given surface point.
   * @param position The <code>Point3</code> representing the location of the
   *     surface point.
   * @param normal The <code>Vector3</code> representing the normal to the
   *     surface at the surface point.
   * @param maxDistance The maximum distance from <code>position</code> to
   *     search for photons.
   * @param numPhotons The maximum number of photons to use for the
   *     irradiance estimate.
   * @return An estimate of the irradiance at the given surface point.
   */
  public double getIrradianceEstimate(Point3 position, Vector3 normal, double maxDistance, int numPhotons) {
    NearestPhotons np = new NearestPhotons();
    np.squaredDistance = new double[numPhotons + 1];
    np.index = new int[numPhotons + 1];
    np.x = position.x();
    np.y = position.y();
    np.z = position.z();
    np.maximum = numPhotons;
    np.found = 0;
    np.gotHeap = false;
    np.squaredDistance[0] = (maxDistance * maxDistance);

    // locate the nearest photons.
    locatePhotons(np, 1);

    // if less than 8 photons return.
    if (np.found < 8) {
      return 0.0;
    }

    double irrad = 0.0;

    // sum irradiance from all photons.
    for (int i = 1; i < np.found; i++) {
      int index = np.index[i];
      // the following check can be omitted (for speed) if the scene does
      // not have any thin surfaces.
      Vector3 pdir = photons.getDir(index);
      if (pdir.dot(normal) < 0.0) {
        irrad += photons.getPower(index);
      }
    }

    irrad *= (1.0 / Math.PI) / np.squaredDistance[0];  // estimate of density
    return irrad;
  }

  /**
   * Searches the balanced tree for the nearest photons to a given point.
   * @param np A <code>NearestPhotons</code> object to receive the results
   *     and provide search parameters.
   * @param index The index of the node whose subtree to search.
   */
  private void locatePhotons(NearestPhotons np, int index) {
    double dist1;

    if (index < leafStart) {
      short plane = photons.getPlane(index);
      double pos = photons.getPosition(index, plane);
      dist1 = np.getPosition(plane) - pos;

      if (dist1 > 0.0) {  // if dist1 is positive search right plane
        locatePhotons(np, 2 * index + 1);
        if (dist1 * dist1 < np.squaredDistance[0]) {
          locatePhotons(np, 2 * index);
        }
      } else {      // if dist1 is negative search left plane
        locatePhotons(np, 2 * index);
        if (dist1 * dist1 < np.squaredDistance[0]) {
          locatePhotons(np, 2 * index + 1);
        }
      }
    }

    // compute squared distance between current photon and position from np
    dist1 = photons.getX(index) - np.x;
    double dist2 = dist1 * dist1;
    dist1 = photons.getY(index) - np.y;
    dist2 += dist1 * dist1;
    dist1 = photons.getZ(index) - np.z;
    dist2 += dist1 * dist1;

    if (dist2 < np.squaredDistance[0]) {
      // we found a photon -- insert it in the candidate list.
      if (np.found < np.maximum) {
        // heap is not full, use array
        np.found++;
        np.squaredDistance[np.found] = dist2;
        np.index[np.found] = index;
      } else {
        int j, parent;

        if (!np.gotHeap) {  // do we need to build the heap?
          // build heap
          double dst2;
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

        // insert new photon into max heap
        // delete largest element, insert new, and reorder the heap.

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

  /**
   * Splits the array of photons at the median along a given axis.
   * @param p An array of indices into the <code>CompactPhotonBuffer</code>.
   * @param start The index into <code>p</code> indicating the start of the
   *     range of photon indices to split.
   * @param end The index into <code>p</code> indicating the end of the range
   *     of photon indices to split.
   * @param median The index into <code>p</code> of the median.
   * @param axis The direction along which to split the photons (0 for
   *     x-axis, 1 for y-axis, 2 for z-axis).
   */
  private void medianSplit(int[] p, int start, int end, int median, int axis) {
    int left = start;
    int right = end;

    while (right > left) {
      double v = photons.getPosition(p[right], axis);
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

  /**
   * Swaps two elements of an array.
   * @param p The array containing the elements to swap.
   * @param i The index of the first element.
   * @param j The index of the second element.
   */
  private void swap(int[] p, int i, int j) {
    int tmp = p[i];
    p[i] = p[j];
    p[j] = tmp;
  }

  /**
   * Used in creating a balanced binary tree from the array of photons.
   * See Chapter 6 of H.W. Jensen, "Realistic Image Synthesis using Photon
   * Mapping".
   */
  private void balanceSegment(int[] pbal, int[] porg, int index, int start, int end) {

    //-------------------
    // compute new median
    //-------------------

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

    //-------------------------
    // find axis to split along
    //-------------------------

    short axis = 2;
    if ((bbox_max[0] - bbox_min[0]) > (bbox_max[1] - bbox_min[1]) && (bbox_max[0] - bbox_min[0]) > (bbox_max[2] - bbox_min[2])) {
      axis = 0;
    } else if ((bbox_max[1] - bbox_min[1]) > (bbox_max[2] - bbox_min[2])) {
      axis = 1;
    }

    //-----------------------------------------
    // partition photon block around the median
    //-----------------------------------------

    medianSplit(porg, start, end, median, axis);

    pbal[index] = porg[median];
    photons.setPlane(pbal[index], axis);

    //---------------------------------------------
    // recursively balance the left and right block
    //---------------------------------------------

    if (median > start) {
      // balance left segment
      if (start < median - 1) {
        double tmp = bbox_max[axis];
        bbox_max[axis] = photons.getPosition(pbal[index], axis);
        balanceSegment(pbal, porg, 2 * index, start, median - 1);
        bbox_max[axis] = tmp;
      } else {
        pbal[2 * index] = porg[start];
      }
    }

    if (median < end) {
      // balance right segment
      if (median + 1 < end) {
        double tmp = bbox_min[axis];
        bbox_min[axis] = photons.getPosition(pbal[index], axis);
        balanceSegment(pbal, porg, 2 * index + 1, median + 1, end);
        bbox_min[axis] = tmp;
      } else {
        pbal[2 * index + 1] = porg[end];
      }
    }
  }

  /**
   * Structure used to locate the nearest photons in the kd-tree.
   * @author brad
   */
  private final class NearestPhotons {
    private int maximum;
    private int found;
    private boolean gotHeap;
    private double x, y, z;
    private double[] squaredDistance;
    private int[] index;

    public double getPosition(int axis) {
      switch (axis) {
      case 0: return x;
      case 1: return y;
      case 2: return z;
      default: return Double.NaN;
      }
    }

  }

}
