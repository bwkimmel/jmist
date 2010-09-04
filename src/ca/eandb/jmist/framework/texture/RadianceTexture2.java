/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public final class RadianceTexture2 implements Texture2 {
	
	/** Serialized version ID. */
	private static final long serialVersionUID = -763379510350283868L;
	
	private final RadiancePicture picture;
	
	public RadianceTexture2(RadiancePicture picture) {
		this.picture = picture;
	}
	
	public RadianceTexture2(File file) throws IOException {
		this(RadiancePicture.read(file));
	}
	
	public RadianceTexture2(URL url) throws IOException {
		this(RadiancePicture.read(url));		
	}
	
	public RadianceTexture2(InputStream stream) throws IOException {
		this(RadiancePicture.read(stream));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		
		double		u		= p.x() - Math.floor(p.x());
		double		v		= 1.0 - (p.y() - Math.floor(p.y()));
		int			w		= picture.getSizeX();
		int			h		= picture.getSizeY();
		int			x		= MathUtil.threshold((int) Math.floor(u * (double) w), 0, w - 1);
		int			y		= MathUtil.threshold((int) Math.floor(v * (double) h), 0, h - 1);
		ColorModel	cm		= lambda.getColorModel();

		switch (picture.getFormat()) {
			case RGBE:
				return cm.fromRGB(picture.getPixelRGB(x, y)).sample(lambda);
			case XYZE:
				return cm.fromXYZ(picture.getPixelXYZ(x, y)).sample(lambda);
			default:
				return cm.getBlack(lambda);
		}

	}

}
