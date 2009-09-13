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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jdcp.job.AbstractParallelizableJob;
import ca.eandb.jdcp.job.TaskWorker;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.RasterUtil;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.job.MetropolisLightTransportJob.SeedTask.Worker;
import ca.eandb.jmist.framework.job.bidi.BidiPathStrategy;
import ca.eandb.jmist.framework.job.bidi.PathMeasure;
import ca.eandb.jmist.framework.job.mlt.PathMutator;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.random.RandomAdapter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author brad
 *
 */
public final class MetropolisLightTransportJob extends
		AbstractParallelizableJob {

	private final Scene scene;

	private final Display display;

	private final ColorModel colorModel;

	private final PathMutator mutator;

	private final PathMeasure measure;

	private final BidiPathStrategy strategy;

	private final Random random;

	private final int width;

	private final int height;

	private final int mutationsPerPixel;

	private final int tasks;

	private final boolean displayPartialResults;

	private transient Raster image = null;

	private transient int tasksProvided = 0;

	private transient int tasksSubmitted = 0;

	private transient int mutationsSubmitted = 0;
	
	private final List<PathSeed> seeds = new ArrayList<PathSeed>();

	/**
	 *
	 */
	public MetropolisLightTransportJob() {
		// TODO Auto-generated constructor stub
	}

	private final PathNode generateLightPath(long seed, WavelengthPacket lambda) {
		Random rnd = new RandomAdapter(new java.util.Random(seed));
		Light light = scene.getLight();
		PathInfo pathInfo = new PathInfo(scene, lambda);
		return strategy.traceLightPath(light, pathInfo, rnd);
	}

	private final PathNode generateEyePath(long seed, WavelengthPacket lambda) {
		Random rnd = new RandomAdapter(new java.util.Random(seed));
		Lens lens = scene.getLens();
		Point2 p = RandomUtil.canonical2(rnd);
		PathInfo pathInfo = new PathInfo(scene, lambda);
		return strategy.traceEyePath(lens, p, pathInfo, rnd);
	}

	private static final class PathSeed implements Serializable {
		private static final long serialVersionUID = 2446876045946528984L;
		public long lightPathSeed;
		public long lightPathLength;
		public long eyePathSeed;
		public long eyePathLength;
		public WavelengthPacket lambda;
	};

	private final Path generatePath(PathSeed seed) {
		PathNode lightTail = generateLightPath(seed.lightPathSeed, seed.lambda);
		PathNode eyeTail = generateEyePath(seed.eyePathSeed, seed.lambda);
		Path path = new Path(lightTail, eyeTail);
		return path.slice(seed.lightPathLength, seed.eyePathLength);
	}


	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
	 */
	public Object getNextTask() throws Exception {
		Task task = new Task();
		if (tasksProvided < tasks) {
			return tasksProvided++ < extraPasses ? minPassesPerTask + 1
					: minPassesPerTask;
		} else {
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public void submitTaskResults(Object task_, Object results,
			ProgressMonitor monitor) throws Exception {

		Task task = (Task) task_;
		Raster taskRaster = (Raster) results;

		monitor.notifyStatusChanged("Accumulating partial results...");

		passesSubmitted += taskPasses;
		if (displayPartialResults) {
			double alpha = (double) taskPasses / (double) passesSubmitted;
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
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#finish()
	 */
	@Override
	public void finish() throws Exception {
		if (!displayPartialResults) {
			display.initialize(width, height, colorModel);
			display.setPixels(0, 0, image);
		}

		display.finish();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.AbstractParallelizableJob#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		image = colorModel.createRaster(width, height);
		if (displayPartialResults) {
			display.initialize(width, height, colorModel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#worker()
	 */
	public TaskWorker worker() throws Exception {
		return new Worker();
	}

//	private static final class Task implements Serializable {
//		private static final long serialVersionUID = 4952461377760496702L;
//		public PathSeed seed = new PathSeed();
//		public int mutations;
//	};
	
	private static interface Task extends Serializable {
		Object perform(Object worker);
		void submit(Object results);
	};
	
	private final class SeedTask implements Task {
		
		public Object perform(Object worker) {
			List<PathSeed> seeds = new ArrayList<PathSeed>();
			return seeds;
		}
		public void submit(Object results) {
			List<PathSeed> seeds = (List<PathSeed>) results;
			MetropolisLightTransportJob.this.seeds.addAll(seeds);
		}
	}

	private final class Worker implements TaskWorker {

		private final ThreadLocal<Raster> raster = new ThreadLocal<Raster>() {
			protected Raster initialValue() {
				return colorModel.createRaster(width, height);
			}
		};

		private final Random random = new ThreadLocalRandom(
				MetropolisLightTransportJob.this.random);

		public Object performTask(Object task_, ProgressMonitor monitor) {
			Task task = (Task) task_;
			Color white = colorModel.getWhite(task.seed.lambda);

			Path x = generatePath(task.seed);
			for (int i = 0; i < task.mutations; i++) {
				x = mutate(x);
				record(x, raster.get(), white);
			}

			return raster.get();
		}

		private void record(Path x, Raster image, Color c) {
			Point2 p = x.getPointOnImagePlane();
			if (p != null) {
				RasterUtil.addPixel(image, p, c);
			}
		}

		private Path mutate(Path x) {
			Path y = mutator.mutate(x, random);
			double a = a(x, y);

			return RandomUtil.bernoulli(a, random) ? y : x;
		}

		private double a(Path x, Path y) {
			double fx = f(x);
			double fy = f(y);
			double txy = mutator.getTransitionPDF(x, y);
			double tyx = mutator.getTransitionPDF(y, x);

			return Math.min(1.0, (fy * tyx) / (fx * txy));
		}

		private double f(Path x) {
			return measure.evaluate(x.getLightTail(), x.getEyeTail()).luminance();
		}

	}

}
