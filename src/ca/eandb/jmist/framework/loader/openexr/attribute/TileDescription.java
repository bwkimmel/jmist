/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * @author brad
 *
 */
@OpenEXRAttributeType("tiledesc")
public final class TileDescription implements Attribute {

	private final int xSize;
	
	private final int ySize;
	
	private final LevelMode levelMode;
	
	private final RoundingMode roundingMode;
	
	public enum LevelMode {
		ONE_LEVEL(0),
		MIPMAP_LEVELS(1),
		RIPMAP_LEVELS(2);
		
		private final int key;
		
		private LevelMode(int key) {
			this.key = key;
		}
		
		private static LevelMode fromKey(int key) {
			for (LevelMode mode : LevelMode.values()) {
				if (mode.key == key) {
					return mode;
				}
			}
			throw new IllegalArgumentException();
		}
		
		private static LevelMode fromMode(int mode) {
			return fromKey(mode & 0x0f);
		}
	}
	
	public enum RoundingMode {
		DOWN(0),
		UP(1);
		
		private final int key;
		
		private RoundingMode(int key) {
			this.key = key;
		}
		
		private static RoundingMode fromKey(int key) {
			for (RoundingMode mode : RoundingMode.values()) {
				if (mode.key == key) {
					return mode;
				}
			}
			throw new IllegalArgumentException();
		}
		
		private static RoundingMode fromMode(int mode) {
			return fromKey((mode & 0xf0) >> 4);
		}
	}
	
	public TileDescription(int xSize, int ySize, LevelMode levelMode, RoundingMode roundingMode) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.levelMode = levelMode;
		this.roundingMode = roundingMode;
	}
	
	/**
	 * @return the xSize
	 */
	public final int getXSize() {
		return xSize;
	}

	/**
	 * @return the ySize
	 */
	public final int getYSize() {
		return ySize;
	}

	/**
	 * @return the levelMode
	 */
	public final LevelMode getLevelMode() {
		return levelMode;
	}

	/**
	 * @return the roundingMode
	 */
	public final RoundingMode getRoundingMode() {
		return roundingMode;
	}

	private int getMode() {
		return levelMode.key | (roundingMode.key << 4);
	}
	
	public static TileDescription read(DataInput in, int size) throws IOException {
		int xSize = in.readInt();
		int ySize = in.readInt();
		int mode = in.readUnsignedByte();
		LevelMode levelMode = LevelMode.fromMode(mode);
		RoundingMode roundingMode = RoundingMode.fromMode(mode);
		return new TileDescription(xSize, ySize, levelMode, roundingMode);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(xSize);
		out.writeInt(ySize);
		out.writeByte(getMode());
	}

}
