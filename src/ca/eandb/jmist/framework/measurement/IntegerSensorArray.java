/**
 * 
 */
package ca.eandb.jmist.framework.measurement;

import java.io.Serializable;
import java.util.Arrays;

import ca.eandb.jmist.framework.measurement.CollectorSphere.Callback;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
public final class IntegerSensorArray implements Callback, Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2445861778987000123L;
	
	private final long[] hits;
	
	public IntegerSensorArray(long[] hits) {
		this.hits = hits;
	}
	
	public IntegerSensorArray(int numSensors) {
		this(new long[numSensors]);
	}
	
	public IntegerSensorArray(CollectorSphere collector) {
		this(collector.sensors());
	}
	
	public void reset() {
		Arrays.fill(hits, 0);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere.Callback#record(int, java.lang.Object)
	 */
	public void record(int sensor, Object obj) {
		hits[sensor]++;
	}
	
	public long hits(int sensor) {
		return hits[sensor];
	}
	
	public void merge(IntegerSensorArray other) {
		MathUtil.add(hits, other.hits);
	}
	
	public int sensors() {
		return hits.length;
	}

}
