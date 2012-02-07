/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageReaderSpi extends ImageReaderSpi {

	/**
	 * 
	 */
	public OpenEXRImageReaderSpi() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param vendorName
	 * @param version
	 * @param names
	 * @param suffixes
	 * @param MIMETypes
	 * @param readerClassName
	 * @param inputTypes
	 * @param writerSpiNames
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
	public OpenEXRImageReaderSpi(String vendorName, String version,
			String[] names, String[] suffixes, String[] MIMETypes,
			String readerClassName, Class[] inputTypes,
			String[] writerSpiNames,
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
		super(vendorName, version, names, suffixes, MIMETypes, readerClassName,
				inputTypes, writerSpiNames,
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
	 * @see javax.imageio.spi.ImageReaderSpi#canDecodeInput(java.lang.Object)
	 */
	@Override
	public boolean canDecodeInput(Object source) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.ImageReaderSpi#createReaderInstance(java.lang.Object)
	 */
	@Override
	public ImageReader createReaderInstance(Object extension) throws IOException {
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
