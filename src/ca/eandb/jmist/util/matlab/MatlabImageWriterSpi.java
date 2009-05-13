/**
 *
 */
package ca.eandb.jmist.util.matlab;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/**
 * An <code>ImageWriterSpi</code> that creates <code>ImageWriter</code>s
 * for writing images to MATLAB MAT-files.
 * @author Brad Kimmel
 */
public final class MatlabImageWriterSpi extends ImageWriterSpi {

	/**
	 * Creates a new <code>MatlabImageWriterSpi</code>.
	 */
	public MatlabImageWriterSpi() {
		super(VENDOR_NAME, VERSION, NAMES, SUFFIXES, MIME_TYPES,
				MatlabImageWriterSpi.class.getCanonicalName(),
				new Class[]{ ImageOutputStream.class }, null, false, null,
				null, null, null, false, null, null, null, null);
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.ImageWriterSpi#canEncodeImage(javax.imageio.ImageTypeSpecifier)
	 */
	@Override
	public boolean canEncodeImage(ImageTypeSpecifier type) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.ImageWriterSpi#createWriterInstance(java.lang.Object)
	 */
	@Override
	public ImageWriter createWriterInstance(Object extension) throws IOException {
		return new MatlabImageWriter(this);
	}

	/* (non-Javadoc)
	 * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(Locale locale) {
		return "jMIST Matlab Image Writer SPI";
	}

	/** Vendor name. */
	private static final String VENDOR_NAME = "jmist.org";

	/** Version. */
	private static final String VERSION = "0.1";

	/** The format names that this <code>ImageWriterSpi</code> handles. */
	private static final String[] NAMES = new String[] { "mat" };

	/**
	 * The suffixes for the files corresponding to the formats in
	 * {@link #NAMES}.
	 */
	private static final String[] SUFFIXES = new String[] { "mat" };

	/**
	 * The MIME types for the file formats corresponding to {@link #NAMES}.
	 */
	private static final String[] MIME_TYPES = new String[] { "application/x-matlab" };

}
