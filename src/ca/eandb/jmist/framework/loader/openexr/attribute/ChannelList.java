/**
 * 
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


/**
 * @author brad
 *
 */
@OpenEXRAttributeType("chlist")
public final class ChannelList implements Attribute {
	
	private static final Comparator<Channel> CHANNEL_COMPARATOR = new LexigraphicalChannelComparator();

	private final List<Channel> channels = new ArrayList<Channel>();
	
	private final Map<String, Integer> lookup = new HashMap<String, Integer>();
	
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
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		ensureSorted();
		for (Channel channel : channels) {
			channel.write(out);
		}
		out.writeByte(0);
	}

}
