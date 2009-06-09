/**
 *
 */
package ca.eandb.jmist.util.matlab;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/**
 * An <code>ImageWriter</code> for writing images to MATLAB MAT-files.
 * @author Brad Kimmel
 */
public final class MatlabImageWriter extends ImageWriter {

	public MatlabImageWriter(ImageWriterSpi originatingProvider) {
		super(originatingProvider);
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageWriter#convertImageMetadata(javax.imageio.metadata.IIOMetadata, javax.imageio.ImageTypeSpecifier, javax.imageio.ImageWriteParam)
	 */
	@Override
	public IIOMetadata convertImageMetadata(IIOMetadata inData,
			ImageTypeSpecifier imageType, ImageWriteParam param) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageWriter#convertStreamMetadata(javax.imageio.metadata.IIOMetadata, javax.imageio.ImageWriteParam)
	 */
	@Override
	public IIOMetadata convertStreamMetadata(IIOMetadata inData,
			ImageWriteParam param) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageWriter#getDefaultImageMetadata(javax.imageio.ImageTypeSpecifier, javax.imageio.ImageWriteParam)
	 */
	@Override
	public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType,
			ImageWriteParam param) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageWriter#getDefaultStreamMetadata(javax.imageio.ImageWriteParam)
	 */
	@Override
	public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageWriter#write(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam)
	 */
	@Override
	public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param)
			throws IOException {

		Raster raster = null;

		if (image.hasRaster()) {
			raster = image.getRaster();
		} else {
			RenderedImage rendered = image.getRenderedImage();
			if (rendered != null && rendered instanceof BufferedImage) {
				raster = ((BufferedImage) rendered).getRaster();
			}
		}

		if (raster != null) {

			MatlabOutputStream out = new MatlabOutputStream((ImageOutputStream) super.output);

			assert(raster != null);

			int width = raster.getWidth();
			int height = raster.getHeight();
			int bands = raster.getNumBands();
			int size = height * width * bands;
			double[] column = new double[height];

			out.beginElement(MatlabDataType.COMPRESSED);
			out.beginArrayElement("image", MatlabArrayType.DOUBLE, MatlabDataType.DOUBLE, false, false, false, new int[]{ height, width, bands }, size);
			out.beginElement(MatlabDataType.DOUBLE, MatlabDataType.DOUBLE.size * size);

			for (int band = 0; band < bands; band++) {
				for (int x = 0; x < width; x++) {
					raster.getSamples(x, 0, 1, height, band, column);
					out.writeDoubles(column);
				}
			}

			out.endElement();
			out.endElement();
			out.endElement();

			out.flush();

		}

	}

}
