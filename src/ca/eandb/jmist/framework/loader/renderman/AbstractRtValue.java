/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
abstract class AbstractRtValue implements RtValue {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#realValue()
	 */
	@Override
	public double realValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Real value expected");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#intValue()
	 */
	@Override
	public int intValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Integer value expected");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#stringValue()
	 */
	@Override
	public String stringValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "String value expected");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Real array expected");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#intArrayValue()
	 */
	@Override
	public int[] intArrayValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Integer array expected");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtValue#stringArrayValue()
	 */
	@Override
	public String[] stringArrayValue() {
		throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "String array expected");
	}

}
