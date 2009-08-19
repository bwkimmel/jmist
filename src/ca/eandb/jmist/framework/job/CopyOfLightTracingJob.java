/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Emitter;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.Photon;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.Lens.Projection;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.gi.LightNode;
import ca.eandb.jmist.framework.gi.EyeNode;
import ca.eandb.jmist.framework.gi.PathNode;
import ca.eandb.jmist.framework.gi.PathNodeFactory;
import ca.eandb.jmist.framework.gi.PathUtil;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author brad
 *
 */
public final class CopyOfLightTracingJob extends AbstractParallelizableJob {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6940841062797196504L;

	private final Scene scene;

	private final ColorModel colorModel;

	private final Random random;

	private transient Raster raster;

	private final Display display;

	private final int photons;

	private final int tasks;

	private final int minPhotonsPerTask;

	private final int extraPhotons;

	private final int width;

	private final int height;

	private final boolean displayPartialResults;

	private transient int tasksProvided = 0;

	private transient int tasksSubmitted = 0;

	private transient int photonsSubmitted = 0;

	public CopyOfLightTracingJob(Scene scene, Display display, int width, int height, ColorModel colorModel, Random random, int photons, int tasks, boolean displayPartialResults) {
		this.scene = scene;
		this.display = display;
		this.colorModel = colorModel;
		this.random = random;
		this.photons = photons;
		this.tasks = tasks;
		this.minPhotonsPerTask = photons / tasks;
		this.extraPhotons = photons - minPhotonsPerTask;
		this.width = width;
		this.height = height;
		this.displayPartialResults = displayPartialResults;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
	 */
	public synchronized Object getNextTask() throws Exception {
		if (tasksProvided < tasks) {
			return tasksProvided++ < extraPhotons ? minPhotonsPerTask + 1
					: minPhotonsPerTask;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() throws Exception {
		return tasksSubmitted == tasks;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public synchronized void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) throws Exception {

		int taskPhotons = (Integer) task;
		Raster taskRaster = (Raster) results;

		monitor.notifyStatusChanged("Accumulating partial results...");

		photonsSubmitted += taskPhotons;
		if (displayPartialResults) {
			double alpha = (double) taskPhotons / (double) photonsSubmitted;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).times(
							1.0 - alpha).plus(
							taskRaster.getPixel(x, y).times(alpha)));
				}
			}
			display.setPixels(0, 0, raster);
		} else {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).plus(taskRaster.getPixel(x, y)));
				}
			}
		}

		monitor.notifyProgress(++tasksSubmitted, tasks);
		if (tasksSubmitted == tasks) {
			monitor.notifyStatusChanged("Ready to write results");
		} else {
			monitor.notifyStatusChanged("Waiting for partial results");
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		raster = colorModel.createRaster(width, height);
		if (displayPartialResults) {
			display.initialize(width, height, colorModel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#finish()
	 */
	@Override
	public void finish() throws Exception {
		if (!displayPartialResults) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster.setPixel(x, y, raster.getPixel(x, y).divide(photons));
				}
			}
			display.initialize(width, height, colorModel);
			display.setPixels(0, 0, raster);
		}
		display.finish();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#worker()
	 */
	public TaskWorker worker() throws Exception {
		return new Worker();
	}

	private final class Worker implements TaskWorker {

		/** Serialization version ID. */
		private static final long serialVersionUID = -7848301189373426210L;

		private transient PathNodeFactory nodes;

		private transient ThreadLocal<Raster> raster = new ThreadLocal<Raster>() {
			protected Raster initialValue() {
				return colorModel.createRaster(width, height);
			}
		};

		private synchronized void ensureInitialized() {
			if (nodes == null) {
				nodes = PathNodeFactory.create(scene, colorModel);
			}
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jdcp.job.TaskWorker#performTask(java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			int photons = (Integer) task;
			Raster raster = this.raster.get();
			raster.clear();

			ensureInitialized();

			for (int i = 0; i < photons; i++) {
				if (i % 1000 == 0) {
					if (!monitor.notifyProgress(i, photons)) {
						monitor.notifyCancelled();
						return null;
					}
				}

				Color sample = colorModel.sample(random);
				PathNode node = nodes.sampleLight(sample, random);
				while (node != null) {
					node.scatterToEye(raster, 1.0);
					node = node.expand(random);
				}
			}

			monitor.notifyProgress(photons, photons);
			monitor.notifyComplete();

			return raster;

		}

	}

}
