/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author brad
 *
 */
@OpenEXRAttributeType("chlist")
public final class ChannelList implements Attribute {

	private final List<Channel> channels = new ArrayList<Channel>();
	
	public ChannelList addChannel(Channel channel) {
		channels.add(channel);
		return this;
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
