/**
 *
 */
package org.jmist.toolkit.matlab;

import java.awt.image.Raster;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

import org.jmist.util.ArrayUtil;

/**
 * An <code>ImageWriter</code> for writing images to MATLAB MAT-files.
 * @author bkimmel
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

		if (image.hasRaster()) {

			MatlabOutputStream out = new MatlabOutputStream((ImageOutputStream) super.output);
			MatlabWriter writer = new MatlabWriter(out);
			Raster raster = image.getRaster();

			assert(raster != null);

			int width = raster.getWidth();
			int height = raster.getHeight();
			int bands = raster.getNumBands();
			double[] data = new double[width * height * bands];
			double[] column = new double[height];
			int position = 0;

			for (int band = 0; band < bands; band++) {
				for (int x = 0; x < width; x++) {
					raster.getSamples(x, 0, 1, height, band, column);
					ArrayUtil.setRange(data, position, column);
					position += column.length;
				}
			}

			writer.write("image", data, new int[]{ height, width, bands });
			writer.flush();

		}

	}

}
