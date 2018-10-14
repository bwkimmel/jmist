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
package ca.eandb.jmist.framework.tone;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * A factory for creating a <code>ToneMapper</code> from a sequence of
 * <code>CIEXYZ</code> color samples.
 * @author Brad Kimmel
 */
public interface ToneMapperFactory extends Serializable {

  /**
   * Creates a <code>ToneMapper</code> from a sequence of <code>CIEXYZ</code>
   * samples.
   * @param samples A sequence of <code>CIEXYZ</code> samples (null entries
   *     will be ignored).
   * @return A <code>ToneMapper</code>.
   */
  ToneMapper createToneMapper(Iterable<CIEXYZ> samples);

  /**
   * A <code>ToneMapperFactory</code> that always returns
   * <code>ToneMapper.IDENTITY</code>
   * @see ToneMapper#IDENTITY
   */
  public static final ToneMapperFactory IDENTITY_FACTORY = new ToneMapperFactory() {
    private static final long serialVersionUID = 827095046468759801L;
    public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
      return ToneMapper.IDENTITY;
    }
  };

}
