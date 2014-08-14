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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * A node in a <code>Path</code> used for path-integral based rendering
 * algorithms.
 * @author Brad Kimmel
 */
public interface PathNode {

  Color getCumulativeWeight();

  double getGeometricFactor();

  double getPDF();
  
  double getReversePDF();

  boolean isSpecular();

  PathNode getParent();

  HPoint3 getPosition();

  boolean isAtInfinity();

  boolean isOnEyePath();

  boolean isOnLightPath();

  int getDepth();

  ScatteredRay sample(double ru, double rv, double rj);

  Color scatter(Vector3 v);

  double getCosine(Vector3 v);

  double getPDF(Vector3 v);

  double getReversePDF(Vector3 v);

  ScatteringNode expand(double ru, double rv, double rj);
  
  double getRU();
  
  double getRV();
  
  double getRJ();
  
  PathNode reverse(PathNode newParent, PathNode grandChild);

  PathInfo getPathInfo();
  
}
