/**
 *
 */
package ca.eandb.jmist.framework.job;

import java.io.IOException;
import java.io.Serializable;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.measurement.CollectorSphere;
import ca.eandb.jmist.framework.measurement.CollectorSphere.Callback;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.scatter.SurfaceScatterer;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.matlab.MatlabWriter;
import ca.eandb.util.io.Archive;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author Brad Kimmel
 *
 */
public final class TransferMatrixJob extends AbstractParallelizableJob {

	/** Serialization version ID. */
	private static final long serialVersionUID = 843938200854073006L;

	/** The <code>TaskWorker</code> that performs the work for this job. */
	private final PhotometerTaskWorker worker;

	private final SurfaceScatterer[] specimens;
	private final double[] wavelengths;
	private final long samplesPerMeasurement;
	private final long samplesPerTask;
	private final int totalTasks;
	private transient int[] sca;
	private transient int[] abs;
	private transient int[] cast;
	private transient int nextMeasurementIndex = 0;
	private transient long outstandingSamplesPerMeasurement = 0;
	private transient int tasksReturned = 0;

	public TransferMatrixJob(SurfaceScatterer[] specimens, double[] wavelengths,
			long samplesPerMeasurement, long samplesPerTask, CollectorSphere collector) {

		this.worker						= new PhotometerTaskWorker(collector);
		this.specimens					= specimens.clone();
		this.wavelengths				= wavelengths.clone();
		this.samplesPerMeasurement		= samplesPerMeasurement;
		this.samplesPerTask				= samplesPerTask;
		this.totalTasks					= specimens.length
												* wavelengths.length
												* ((int) (samplesPerMeasurement / samplesPerTask) + ((samplesPerMeasurement % samplesPerTask) > 0 ? 1
														: 0));


	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() {
		int numSensors = worker.collector.sensors();
		this.sca = new int[wavelengths.length * specimens.length * numSensors * numSensors];
		this.abs = new int[wavelengths.length * specimens.length * numSensors];
		this.cast = new int[wavelengths.length * specimens.length * numSensors];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.eandb.jmist.framework.ParallelizableJob#getNextTask()
	 */
	public synchronized Object getNextTask() {

		if (outstandingSamplesPerMeasurement < samplesPerMeasurement) {

			Task task = this.getPhotometerTask(this.nextMeasurementIndex);

			if (++this.nextMeasurementIndex >= wavelengths.length * specimens.length) {
				this.outstandingSamplesPerMeasurement += this.samplesPerTask;
				this.nextMeasurementIndex = 0;
			}

			return task;

		} else {

			return null;

		}

	}

	private Task getPhotometerTask(int measurementIndex) {
		return new Task(
				this.getSpecimen(measurementIndex),
				this.getWavelength(measurementIndex),
				Math.min(samplesPerTask, samplesPerMeasurement - outstandingSamplesPerMeasurement),
				measurementIndex
		);
	}
	
	private SurfaceScatterer getSpecimen(int measurementIndex) {
		return this.specimens[measurementIndex / wavelengths.length];
	}

	private double getWavelength(int measurementIndex) {
		int specimenMeasurementIndex = measurementIndex % wavelengths.length;
		return this.wavelengths[specimenMeasurementIndex];
	}
	
	private static final class TaskResult implements Serializable {
		
		/** Serialization version ID. */
		private static final long serialVersionUID = -1374555802323806923L;
		
		public final int[] sca;
		public final int[] abs;
		public final int[] cast;
		
		public TaskResult(int numSensors) {
			sca = new int[numSensors * numSensors];
			abs = new int[numSensors];
			cast = new int[numSensors];
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) {

		Task		info		= (Task) task;
		TaskResult	tr			= (TaskResult) results;
		int			numSensors	= worker.collector.sensors();

		MathUtil.addRange(sca, info.measurementIndex * numSensors * numSensors, tr.sca);
		MathUtil.addRange(abs, info.measurementIndex * numSensors, tr.abs);
		MathUtil.addRange(cast, info.measurementIndex * numSensors, tr.cast);
		
		monitor.notifyProgress(++this.tasksReturned, this.totalTasks);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() {
		return this.tasksReturned >= this.totalTasks;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#finish()
	 */
	public void finish() throws IOException {

		MatlabWriter matlab = new MatlabWriter(createFileOutputStream("tm.mat"));
		
		int numSensors = worker.collector.sensors();
		matlab.write("sca", sca, new int[]{ numSensors, numSensors, wavelengths.length, specimens.length });
		matlab.write("abs", abs, new int[]{ numSensors, wavelengths.length, specimens.length });
		matlab.write("cast", cast, new int[]{ numSensors, wavelengths.length, specimens.length });
		matlab.write("wavelengths", wavelengths);
		
		double[] polar = new double[numSensors];
		double[] azimuthal = new double[numSensors];
		double[] solidAngle = new double[numSensors];
		double[] projectedSolidAngle = new double[numSensors];
		                       
		for (int sensor = 0; sensor < numSensors; sensor++) {

			SphericalCoordinates exitantAngle = worker.collector.getSensorCenter(sensor);
			
			polar[sensor] = exitantAngle.polar();
			azimuthal[sensor] = exitantAngle.azimuthal();
			solidAngle[sensor] = worker.collector.getSensorSolidAngle(sensor);
			projectedSolidAngle[sensor] = worker.collector.getSensorProjectedSolidAngle(sensor);

		}
		
		matlab.write("sensorPolarAngle", polar);
		matlab.write("sensorAzimuthalAngle", azimuthal);
		matlab.write("sensorSolidAngle", solidAngle);
		matlab.write("sensorProjectedSolidAngle", projectedSolidAngle);

		matlab.close();

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#archiveState(ca.eandb.util.io.Archive)
	 */
	@Override
	protected void archiveState(Archive ar) throws IOException, ClassNotFoundException {
		sca = (int[]) ar.archiveObject(sca);
		abs = (int[]) ar.archiveObject(abs);
		cast = (int[]) ar.archiveObject(cast);
		nextMeasurementIndex = ar.archiveInt(nextMeasurementIndex);
		outstandingSamplesPerMeasurement = ar.archiveLong(outstandingSamplesPerMeasurement);
		tasksReturned = ar.archiveInt(tasksReturned);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#worker()
	 */
	public TaskWorker worker() {
		return this.worker;
	}

	private static class Task implements Serializable {

		/** Serialization version ID. */
		private static final long serialVersionUID = -5989258164757195409L;
		
		public final SurfaceScatterer		specimen;
		public final double					wavelength;
		public final long					samples;
		public final int					measurementIndex;

		public Task(SurfaceScatterer specimen, double wavelength,
				long samples, int measurementIndex) {
			this.specimen			= specimen;
			this.wavelength			= wavelength;
			this.samples			= samples;
			this.measurementIndex	= measurementIndex;
		}

	}

	private static class PhotometerTaskWorker implements TaskWorker, Serializable {

		/** Serialization version ID. */
		private static final long serialVersionUID = -7665433785262998136L;
		
		/**
		 * The prototype <code>CollectorSphere</code> from which clones are
		 * constructed to record hits to.
		 */
		private final CollectorSphere collector;

		/**
		 * Creates a new <code>PhotometerTaskWorker</code>.
		 * @param specimen The <code>SurfaceScatterer</code> to be measured.
		 * @param collector The prototype <code>CollectorSphere</code> from
		 * 		which clones are constructed to record hits to.
		 */
		public PhotometerTaskWorker(CollectorSphere collector) {
			this.collector = collector;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			Task				info			= (Task) task;
			Random				rng				= new SimpleRandom();
			final int			numSensors		= collector.sensors();
			final TaskResult	result			= new TaskResult(numSensors);
			final long			progInterval	= MathUtil.clamp(info.samples / 1000, 1, 1000);
			long				progCountdown	= 1;
			final int[]			sensor0			= new int[]{ -1 };
			
			for (long i = 0; i < info.samples; i++) {
				
				if (--progCountdown == 0) {
					if (!monitor.notifyProgress((double) i / (double) info.samples)) {
						monitor.notifyCancelled();
						return null;
					}
					progCountdown = progInterval;
				}
				
				Vector3 in;
				sensor0[0] = -1;
				do {
					in = RandomUtil.uniformOnSphere(rng).toCartesian();
					collector.record(in, new Callback() {
						public void record(int sensor) {
							result.cast[sensor]++;
							sensor0[0] = sensor;
						}
					});
				} while (sensor0[0] < 0);
				
				Vector3 v = info.specimen.scatter(SurfacePointGeometry.STANDARD, in, false, info.wavelength, rng);
				
				if (v != null) {
					collector.record(v, new Callback() {
						public void record(int sensor) {
							result.sca[sensor0[0] * numSensors + sensor]++;
						}
					});
				} else {
					result.abs[sensor0[0]]++;
				}
				
			}
			
			monitor.notifyProgress(1.0);
			monitor.notifyComplete();
			
			return result;
			
		}

	}

}
