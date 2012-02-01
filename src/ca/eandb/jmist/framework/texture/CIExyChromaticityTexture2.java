/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that renders the CIE xy chromaticity diagram.
 * This class is a singleton.
 * @author Brad Kimmel
 */
public final class CIExyChromaticityTexture2 implements Texture2 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -2115762100667409214L;

	/** The single instance of this class. */
	private static final CIExyChromaticityTexture2 INSTANCE = new CIExyChromaticityTexture2();
	
	/** The <code>Mask2</code> denoting the shape of the diagram. */
	private final Mask2 gamutMask;
	
	/**
	 * Gets the single instance of this class.
	 * @return The single instance of <code>CIExyChromaticityTexture2</code>.
	 */
	public static CIExyChromaticityTexture2 getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Creates a new <code>CIExyChromaticityTexture2</code>.  This constructor
	 * is private because this class is a singleton.
	 */
	private CIExyChromaticityTexture2() {
		List<Point2> vertices = new ArrayList<Point2>();
		for (int i = 0, n = ColorUtil.XYZ_WAVELENGTHS.size(); i < n; i++) {
			double X = ColorUtil.X_BAR.at(i);
			double Y = ColorUtil.Y_BAR.at(i);
			double Z = ColorUtil.Z_BAR.at(i);
			double x = X / (X + Y + Z);
			double y = Y / (X + Y + Z);
			vertices.add(new Point2(x, y));
		}
		
		gamutMask = new PolygonMask2(vertices, 100);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		ColorModel cm = lambda.getColorModel();
		if (gamutMask.opacity(p) > 0.5) {
			CIEXYZ xyz = new CIExyY(p.x(), p.y(), p.y()).toXYZ();
			return cm.fromXYZ(xyz).sample(lambda);
		} else {
			return cm.getBlack(lambda);
		}
	}

}
