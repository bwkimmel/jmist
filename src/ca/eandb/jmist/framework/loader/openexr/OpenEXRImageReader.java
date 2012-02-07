/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageReader extends ImageReader {

	/**
	 * @param originatingProvider
	 */
	public OpenEXRImageReader(ImageReaderSpi originatingProvider) {
		super(originatingProvider);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getHeight(int)
	 */
	@Override
	public int getHeight(int imageIndex) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getImageMetadata(int)
	 */
	@Override
	public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getImageTypes(int)
	 */
	@Override
	public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getNumImages(boolean)
	 */
	@Override
	public int getNumImages(boolean allowSearch) throws IOException {
		return 1;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getStreamMetadata()
	 */
	@Override
	public IIOMetadata getStreamMetadata() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#getWidth(int)
	 */
	@Override
	public int getWidth(int imageIndex) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.ImageReader#read(int, javax.imageio.ImageReadParam)
	 */
	@Override
	public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
