/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author brad
 *
 */
@OpenEXRAttributeType("chlist")
public final class ChannelList implements Attribute {

	private final List<Channel> channels = new ArrayList<Channel>();
	
	private final Map<String, Integer> lookup = new HashMap<String, Integer>();
	
	public ChannelList addChannel(Channel channel) {
		lookup.put(channel.getName(), channels.size());
		channels.add(channel);
		return this;
	}
	
	public List<Channel> channels() {
		return Collections.unmodifiableList(channels);
	}

	public int getChannelIndex(String name) {
		return lookup.get(name);
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
		for (Channel channel : channels) {
			channel.write(out);
		}
		out.writeByte(0);
	}

}
