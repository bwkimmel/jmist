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
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Ray3;

/**
 * Represents the properties of a ray-scatter event, which may be a ray
 * scattered from a material interaction, or emission from a light source.
 * @author Brad Kimmel
 */
public final class ScatteredRay {

  /** Describes the type of scattering. */
  public static enum Type {

    /** Scattering in all directions uniformly (more-or-less). */
    DIFFUSE,

    /**
     * Scattering concentrated in a given direction enough to make out
     * fuzzy reflections.
     */
    GLOSSY,

    /** Scattering highly concentrated in a given direction (mirror-like). */
    SPECULAR

  }

  /** The origin and direction of the scattered ray. */
  private final Ray3 scatteredRay;

  /** The <code>Color</code> applied by this scattering event. */
  private final Color color;

  /** The <code>Type</code> of scattering event (diffuse, glossy, specular) */
  private final Type type;

  /** The marginal probability of generating this scattering event. */
  private final double pdf;

  /**
   * A value indicating whether the scattered ray emits from the opposite
   * side of the surface from the incident ray.
   */
  private final boolean transmitted;

  /**
   * Creates a <code>ScatteredRay</code>.
   * @param scatteredRay The origin and direction of the scattered ray.
   * @param color The <code>Color</code> applied by this scattering event.
   * @param type The <code>Type</code> of scattering event (diffuse, glossy,
   *     specular)
   * @param pdf The marginal probability of generating this scattering event.
   * @param transmitted A value indicating whether the scattered ray emits
   *     from the opposite side of the surface from the incident ray.
   */
  public ScatteredRay(Ray3 scatteredRay, Color color, Type type, double pdf, boolean transmitted) {

    assert(scatteredRay != null);

    this.scatteredRay = scatteredRay;
    this.color = color;
    this.transmitted = transmitted;
    this.type = type;
    this.pdf = pdf;

  }

  /**
   * Gets the origin and direction of the scattered ray.
   * @return The origin and direction of the scattered ray.
   */
  public final Ray3 getRay() {
    return scatteredRay;
  }

  /**
   * Gets the <code>Color</code> applied by this scattering event.
   * @return The <code>Color</code> applied by this scattering event.
   */
  public final Color getColor() {
    return color;
  }

  /**
   * Gets the <code>Type</code> of scattering event (diffuse, glossy, specular).
   * @return The <code>Type</code> of scattering event (diffuse, glossy, specular).
   */
  public final Type getType() {
    return type;
  }

  /**
   * Gets the marginal probability of generating this scattering event.
   * @return The marginal probability of generating this scattering event.
   */
  public final double getPDF() {
    return pdf;
  }

  /**
   * Gets a value indicating whether the scattered ray emits from the opposite
   * side of the surface from the incident ray.
   * @return A value indicating whether the scattered ray emits from the
   *     opposite side of the surface from the incident ray.
   */
  public final boolean isTransmitted() {
    return transmitted;
  }

  /**
   * Transforms this <code>ScatteredRay</code>.
   * @param T The <code>AffineMatrix3</code> representing an affine
   *     transformation to apply to the ray.
   * @return The transformed <code>ScatteredRay</code>.
   */
  public ScatteredRay transform(AffineMatrix3 T) {
    return new ScatteredRay(scatteredRay.transform(T), color, type,
        pdf, transmitted);
  }

  /**
   * Creates a diffuse <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay diffuse(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.DIFFUSE, pdf, false);
  }

  /**
   * Creates a glossy <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay glossy(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.GLOSSY, pdf, false);
  }

  /**
   * Creates a specular <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay specular(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.SPECULAR, pdf, false);
  }

  /**
   * Creates a diffuse, transmitted <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay transmitDiffuse(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.DIFFUSE, pdf, true);
  }

  /**
   * Creates a glossy, transmitted <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay transmitGlossy(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.GLOSSY, pdf, true);
  }

  /**
   * Creates a specular, transmitted <code>ScatteredRay</code>.
   * @param ray The direction of the ray.
   * @param color The color to apply to the scattering event.
   * @param pdf The marginal probability of this scattering event.
   * @return The <code>ScatteredRay</code>.
   */
  public static ScatteredRay transmitSpecular(Ray3 ray, Color color, double pdf) {
    return new ScatteredRay(ray, color, Type.SPECULAR, pdf, true);
  }

  /**
   * Adjusts the probabilities to account for the fact that this scattered
   * ray may only be used with some non-unit probability (for example, if
   * many scattered rays were generated and then one was selected uniformly,
   * the probabilities should be adjusted by a factor of <code>1/N</code>).
   * @param sr The <code>ScatteredRay</code> to adjust.
   * @param probability The probability of this ray being chosen.
   * @return The adjusted <code>ScatteredRay</code>.
   */
  public static ScatteredRay select(ScatteredRay sr, double probability) {
    return new ScatteredRay(sr.getRay(), sr.getColor().divide(probability),
        sr.getType(), sr.getPDF() * probability, sr.isTransmitted());
  }

}
