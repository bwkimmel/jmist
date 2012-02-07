/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageWriterSpi extends ImageWriterSpi {

	/**
	 * 
	 */
	public OpenEXRImageWriterSpi() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param vendorName
	 * @param version
	 * @param names
	 * @param suffixes
	 * @param MIMETypes
	 * @param writerClassName
	 * @param outputTypes
	 * @param readerSpiNames
	 * @param supportsStandardStreamMetadataFormat
	 * @param nativeStreamMetadataFormatName
	 * @param nativeStreamMetadataFormatClassName
	 * @param extraStreamMetadataFormatNames
	 * @param extraStreamMetadataFormatClassNames
	 * @param supportsStandardImageMetadataFormat
	 * @param nativeImageMetadataFormatName
	 * @param nativeImageMetadataFormatClassName
	 * @param extraImageMetadataFormatNames
	 * @param extraImageMetadataFormatClassNames
	 */
	public OpenEXRImageWriterSpi(String vendorName, String version,
			String[] names, String[] suffixes, String[] MIMETypes,
			String writerClassName, Class[] outputTypes,
			String[] readerSpiNames,
			boolean supportsStandardStreamMetadataFormat,
			String nativeStreamMetadataFormatName,
			String nativeStreamMetadataFormatClassName,
			String[] extraStreamMetadataFormatNames,
			String[] extraStreamMetadataFormatClassNames,
			boolean supportsStandardImageMetadataFormat,
			String nativeImageMetadataFormatName,
			String nativeImageMetadataFormatClassName,
			String[] extraImageMetadataFormatNames,
			String[] extraImageMetadataFormatClassNames) {
		super(vendorName, version, names, suffixes, MIMETypes, writerClassName,
				outputTypes, readerSpiNames,
				supportsStandardStreamMetadataFormat,
				nativeStreamMetadataFormatName,
				nativeStreamMetadataFormatClassName,
				extraStreamMetadataFormatNames,
				extraStreamMetadataFormatClassNames,
				supportsStandardImageMetadataFormat,
				nativeImageMetadataFormatName,
				nativeImageMetadataFormatClassName,
				extraImageMetadataFormatNames,
				extraImageMetadataFormatClassNames);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.ImageWriterSpi#canEncodeImage(javax.imageio.ImageTypeSpecifier)
	 */
	@Override
	public boolean canEncodeImage(ImageTypeSpecifier type) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.ImageWriterSpi#createWriterInstance(java.lang.Object)
	 */
	@Override
	public ImageWriter createWriterInstance(Object extension)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

}
