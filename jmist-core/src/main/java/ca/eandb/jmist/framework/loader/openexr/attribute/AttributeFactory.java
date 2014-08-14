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
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class AttributeFactory {
  
  private Map<String, AttributeReader> readers = new HashMap<String, AttributeReader>();
  
  private static AttributeFactory INSTANCE;
  
  private AttributeFactory() {
    registerClass(Box2f.class);
    registerClass(Box2i.class);
    registerClass(ChannelList.class);
    registerClass(Chromaticities.class);
    registerClass(CompressionMethod.class);
    registerClass(FloatAttribute.class);
    registerClass(LineOrder.class);
    registerClass(StringAttribute.class);
    registerClass(TileDescription.class);
    registerClass(V2f.class);
    registerClass(V2i.class);
    registerClass(V3f.class);
    registerClass(V3i.class);
  }
  
  private void registerClass(Class<?> clazz) {
    OpenEXRAttributeType type = clazz.getAnnotation(OpenEXRAttributeType.class);
    if (type == null) {
      throw new RuntimeException("OpenEXRAttributeType annotation not present");
    }
    Method method;
    try {
      method = clazz.getMethod("read", DataInput.class, int.class);
    } catch (SecurityException e) {
      throw new UnexpectedException("Cannot access read method");
    } catch (NoSuchMethodException e) {
      throw new UnexpectedException("No read method");
    }
    registerAttributeReader(type.value(), new MethodAttributeReader(method));
  }
  
  public static synchronized final AttributeFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AttributeFactory();      
    }
    return INSTANCE;
  }

  public Attribute create(String type, int size, DataInput data) throws IOException {
    AttributeReader reader = readers.get(type);
    return reader != null ? reader.read(data, size) : null;
  }
  
  public void registerAttributeReader(String type, AttributeReader reader) {
    readers.put(type, reader);
  }
  
}
