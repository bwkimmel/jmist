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
package ca.eandb.jmist.math;

/**
 * Provides constants and utility methods for physics.
 * @author Brad Kimmel
 */
public final class Physics {

  /** The speed of light in a vacuum (in meters per second). */
  public static final double SPEED_OF_LIGHT = 299452798.0;

  /** Planck's constant (in Joule-seconds). */
  public static final double PLANCK_CONSTANT = 6.62606876e-34;

  /** Boltzmann's constant (in Joules per Kelvin). */
  public static final double BOLTZMANN_CONSTANT = 1.3806503e-23;

  /** Stefan-Boltzmann constant (in Watts per meter squared per Kelvin^4). */
  public static final double STEFAN_BOLTZMANN_CONSTANT = 5.670400e-8;

  /**
   * This class contains only static utility methods,
   * and therefore should not be creatable.
   */
  private Physics() {}

}
