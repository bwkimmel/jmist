/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
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
			long samplesPerMeasurement, long samplesPerTask,
			CollectorSphere incidentCollector, CollectorSphere exitantCollector) {

		this.worker						= new PhotometerTaskWorker(incidentCollector, exitantCollector);
		this.specimens					= specimens.clone();
		this.wavelengths				= wavelengths.clone();
		this.samplesPerMeasurement		= samplesPerMeasurement;
		this.samplesPerTask				= samplesPerTask;
		this.totalTasks					= specimens.length
												* wavelengths.length
												* ((int) (samplesPerMeasurement / samplesPerTask) + ((samplesPerMeasurement % samplesPerTask) > 0 ? 1
														: 0));


	}

	public TransferMatrixJob(SurfaceScatterer[] specimens, double[] wavelengths,
			long samplesPerMeasurement, long samplesPerTask,
			CollectorSphere collector) {
		this(specimens, wavelengths, samplesPerMeasurement, samplesPerTask, collector, collector);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() {
		int numInSensors = worker.incidentCollector.sensors();
		int numOutSensors = worker.exitantCollector.sensors();
		this.sca = new int[wavelengths.length * specimens.length * numInSensors * numOutSensors];
		this.abs = new int[wavelengths.length * specimens.length * numInSensors];
		this.cast = new int[wavelengths.length * specimens.length * numInSensors];
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
		
		public TaskResult(int numInSensors, int numOutSensors) {
			sca = new int[numInSensors * numOutSensors];
			abs = new int[numInSensors];
			cast = new int[numInSensors];
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) {

		Task		info			= (Task) task;
		TaskResult	tr				= (TaskResult) results;
		int			numInSensors	= worker.incidentCollector.sensors();
		int			numOutSensors	= worker.exitantCollector.sensors();

		MathUtil.addRange(sca, info.measurementIndex * numInSensors * numOutSensors, tr.sca);
		MathUtil.addRange(abs, info.measurementIndex * numInSensors, tr.abs);
		MathUtil.addRange(cast, info.measurementIndex * numInSensors, tr.cast);
		
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
		
		int numInSensors = worker.incidentCollector.sensors();
		int numOutSensors = worker.exitantCollector.sensors();
		matlab.write("sca", sca, new int[]{ numOutSensors, numInSensors, wavelengths.length, specimens.length });
		matlab.write("abs", abs, new int[]{ numInSensors, wavelengths.length, specimens.length });
		matlab.write("cast", cast, new int[]{ numInSensors, wavelengths.length, specimens.length });
		matlab.write("wavelengths", wavelengths);
		
		writeCollectorSphere("incident", worker.incidentCollector, matlab);
		writeCollectorSphere("exitant", worker.exitantCollector, matlab);

		matlab.close();

	}
	
	/**
	 * Write the specifications for a <code>CollectorSphere</code> to the
	 * provided MATLAB target.
	 * @param name The name to use for the collector sphere.
	 * @param collector The <code>CollectorSphere</code> to record.
	 * @param matlab The <code>MatlabWriter</code> to write to.
	 * @throws IOException If an error occurs while writing to the provided
	 * 		<code>MatlabWriter</code>.
	 */
	private void writeCollectorSphere(String name, CollectorSphere collector, MatlabWriter matlab) throws IOException {
		
		int numSensors = collector.sensors();
		double[] polar = new double[numSensors];
		double[] azimuthal = new double[numSensors];
		double[] solidAngle = new double[numSensors];
		double[] projectedSolidAngle = new double[numSensors];
		                       
		for (int sensor = 0; sensor < numSensors; sensor++) {

			SphericalCoordinates exitantAngle = collector.getSensorCenter(sensor);
			
			polar[sensor] = exitantAngle.polar();
			azimuthal[sensor] = exitantAngle.azimuthal();
			solidAngle[sensor] = collector.getSensorSolidAngle(sensor);
			projectedSolidAngle[sensor] = collector.getSensorProjectedSolidAngle(sensor);

		}
		
		matlab.write(name + "_sensorPolarAngle", polar);
		matlab.write(name + "_sensorAzimuthalAngle", azimuthal);
		matlab.write(name + "_sensorSolidAngle", solidAngle);
		matlab.write(name + "_sensorProjectedSolidAngle", projectedSolidAngle);
		
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
		 * constructed to record hits to (incident direction).
		 */
		private final CollectorSphere incidentCollector;
		
		/**
		 * The prototype <code>CollectorSphere</code> from which clones are
		 * constructed to record hits to (exitant direction).
		 */
		private final CollectorSphere exitantCollector;

		/**
		 * Creates a new <code>PhotometerTaskWorker</code>.
		 * @param specimen The <code>SurfaceScatterer</code> to be measured.
		 * @param incidentCollector The prototype <code>CollectorSphere</code>
		 * 		from which clones are constructed to record hits to (incident
		 * 		direction).
		 * @param exitantCollector The prototype <code>CollectorSphere</code>
		 * 		from which clones are constructed to record hits to (exitant
		 * 		direction).
		 */
		public PhotometerTaskWorker(CollectorSphere incidentCollector, CollectorSphere exitantCollector) {
			this.incidentCollector = incidentCollector;
			this.exitantCollector = exitantCollector;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			Task				info			= (Task) task;
			Random				rng				= new SimpleRandom();
			final int			numInSensors	= incidentCollector.sensors();
			final int			numOutSensors	= exitantCollector.sensors();
			final TaskResult	result			= new TaskResult(numInSensors, numOutSensors);
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
					incidentCollector.record(in, new Callback() {
						public void record(int sensor) {
							result.cast[sensor]++;
							sensor0[0] = sensor;
						}
					});
				} while (sensor0[0] < 0);
				
				Vector3 v = info.specimen.scatter(SurfacePointGeometry.STANDARD, in, false, info.wavelength, rng);
				
				if (v != null) {
					exitantCollector.record(v, new Callback() {
						public void record(int sensor) {
							result.sca[sensor0[0] * numOutSensors + sensor]++;
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
