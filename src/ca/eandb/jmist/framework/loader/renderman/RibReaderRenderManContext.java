/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 * A <code>RenderManContext</code> that implements the <code>ReadArchive</code>
 * instruction using the provided <code>RibReader</code>.
 * @see ca.eandb.jmist.framework.loader.renderman.RibReader
 * @author Brad Kimmel
 */
public final class RibReaderRenderManContext extends DecoratorRenderManContext {
	
	/** The <code>RibReader</code> to use. */
	private final RibReader reader;

	/**
	 * Creates a new <code>RibReaderRenderManContext</code>.
	 * @param reader The <code>RibReader</code> to use.
	 * @param inner The <code>RenderManContext</code> that implements the
	 * 		remaining instructions.
	 */
	public RibReaderRenderManContext(RibReader reader, RenderManContext inner) {
		super(inner);
		this.reader = reader;
	}
	
	/**
	 * Creates a new <code>RibReaderRenderManContext</code>.
	 * @param inner The <code>RenderManContext</code> that implements the
	 * 		remaining instructions.
	 */
	public RibReaderRenderManContext(RenderManContext inner) {
		this(new RibReader(), inner);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.DecoratorRenderManContext#readArchive(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback, java.util.Map)
	 */
	@Override
	public void readArchive(RtToken name, RtArchiveCallback callback,
			Map<RtToken, RtValue> params) {
		try {
			FileReader f = new FileReader(name.toString());
			reader.read(f, this);
		} catch (FileNotFoundException e) {
			handler.apply(RtErrorType.NOFILE, RtErrorSeverity.ERROR,
					e.getMessage());
		}
	}

}
