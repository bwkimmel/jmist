/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author brad
 *
 */
@OpenEXRAttributeType("chromaticities")
public final class Chromaticities implements Attribute {

	private final float redX;
	private final float redY;
	private final float greenX;
	private final float greenY;
	private final float blueX;
	private final float blueY;
	private final float whiteX;
	private final float whiteY;
	
	public Chromaticities(float redX, float redY, float greenX, float greenY, float blueX, float blueY, float whiteX, float whiteY) {
		this.redX = redX;
		this.redY = redY;
		this.greenX = greenX;
		this.greenY = greenY;
		this.blueX = blueX;
		this.blueY = blueY;
		this.whiteX = whiteX;
		this.whiteY = whiteY;
	}
	
	public static Chromaticities read(DataInput in, int size) throws IOException {
		return new Chromaticities(
				in.readFloat(), in.readFloat(), 
				in.readFloat(), in.readFloat(), 
				in.readFloat(), in.readFloat(), 
				in.readFloat(), in.readFloat());
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(redX);
		out.writeFloat(redY);
		out.writeFloat(greenX);
		out.writeFloat(greenY);
		out.writeFloat(blueX);
		out.writeFloat(blueY);
		out.writeFloat(whiteX);
		out.writeFloat(whiteY);
	}

}
