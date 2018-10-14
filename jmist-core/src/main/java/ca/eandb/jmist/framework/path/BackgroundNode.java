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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 */
public final class BackgroundNode extends AbstractScatteringNode {

  private final Vector3 direction;

  public BackgroundNode(PathNode parent, ScatteredRay sr, double ru, double rv, double rj) {
    super(parent, sr, ru, rv, rj);
    this.direction = sr.getRay().direction();
  }

  @Override
  public double getSourcePDF() {
    return 0;
  }

  @Override
  public double getSourcePDF(Vector3 v) {
    return 0.0;
  }

  @Override
  public Color getSourceRadiance() {
    return getBlack();
  }

  @Override
  public boolean isOnLightSource() {
    return false;
  }

  @Override
  public ScatteredRay sample(double ru, double rv, double rj) {
    return null;
  }

  @Override
  public double getCosine(Vector3 v) {
    return 1.0;
  }

  @Override
  public HPoint3 getPosition() {
    return direction;
  }

  @Override
  public Color scatter(Vector3 v) {
    return getBlack();
  }

  @Override
  public double getPDF(Vector3 v) {
    return 0;
  }

  @Override
  public double getReversePDF(Vector3 v) {
    return 0;
  }

  @Override
  public PathNode reverse(PathNode newParent, PathNode grandChild) {
    return null;
  }

}
