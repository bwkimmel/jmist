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
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OpenEXRAttributeType("chlist")
public final class ChannelList implements Attribute {

  private static final Comparator<Channel> CHANNEL_COMPARATOR = new LexigraphicalChannelComparator();

  private final List<Channel> channels = new ArrayList<>();

  private final Map<String, Integer> lookup = new HashMap<>();

  private boolean isSorted = true;

  public ChannelList addChannel(Channel channel) {
    if (lookup.containsKey(channel.getName())) {
      throw new IllegalArgumentException("Duplicate channel");
    }
    if (!channels.isEmpty() && isSorted) {
      Channel last = channels.get(channels.size() - 1);
      if (CHANNEL_COMPARATOR.compare(last, channel) > 0) {
        isSorted = false;
      }
    }
    lookup.put(channel.getName(), channels.size());
    channels.add(channel);
    return this;
  }

  public List<Channel> channels() {
    ensureSorted();
    return Collections.unmodifiableList(channels);
  }

  private void ensureSorted() {
    if (!isSorted) {
      Collections.sort(channels, CHANNEL_COMPARATOR);
      lookup.clear();
      for (int i = 0, n = channels.size(); i < n; i++) {
        Channel channel = channels.get(i);
        lookup.put(channel.getName(), i);
      }
      isSorted = true;
    }
  }

  public boolean hasChannel(String name) {
    return lookup.containsKey(name);
  }

  public Channel getChannel(String name) {
    Integer index = lookup.get(name);
    return index != null ? channels.get(index) : null;
  }

  public static ChannelList read(DataInput in, int size) throws IOException {
    ChannelList cl = new ChannelList();
    while (true) {
      Channel c = Channel.read(in);
      if (c == null) {
        break;
      }
      cl.addChannel(c);
    }
    return cl;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    ensureSorted();
    for (Channel channel : channels) {
      channel.write(out);
    }
    out.writeByte(0);
  }

}
