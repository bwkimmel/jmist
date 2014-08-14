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
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A collection of common functions implementing the <code>Function1</code>
 * interface.
 *  
 * @author Brad Kimmel
 */
public final class Functions {
  
  public static final Function1 SIN = new Function1() {  
    private static final long serialVersionUID = -5243730437574248501L;
    public double evaluate(double x) {
      return Math.sin(x);    
    }
  };
  
  public static final Function1 COS = new Function1() {
    private static final long serialVersionUID = -861360078453347872L;
    public double evaluate(double x) {
      return Math.cos(x);
    }
  };
  
  public static final Function1 EXP = new Function1() {
    private static final long serialVersionUID = -5109332754391306969L;
    public double evaluate(double x) {
      return Math.exp(x);
    }
  };

}
