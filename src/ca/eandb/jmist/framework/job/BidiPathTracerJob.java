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
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.gi.EyeNode;
import ca.eandb.jmist.framework.gi.LightNode;
import ca.eandb.jmist.framework.gi.PathNode;
import ca.eandb.jmist.framework.gi.PathNodeFactory;
import ca.eandb.jmist.framework.gi.PathUtil;
import ca.eandb.jmist.framework.gi.ScatteringNode;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author brad
 *
 */
public final class BidiPathTracerJob extends AbstractParallelizableJob {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6940841062797196504L;

	private final Scene scene;

	private final ColorModel colorModel;

	private final Random random;

	private transient Raster raster;

	private final Display display;

	private final int tasks;

	private final int width;

	private final int height;

	private final int eyePathsPerPixel;

	private final int lightPathsPerEyePath;

	private final int minPassesPerTask;

	private final int extraPasses;

	private final boolean displayPartialResults;

	private transient int tasksProvided = 0;

	private transient int tasksSubmitted = 0;

	private transient int passesSubmitted = 0;

	public BidiPathTracerJob(Scene scene, Display display, int width,
			int height, ColorModel colorModel, Random random,
			int eyePathsPerPixel, int lightPathsPerEyePath, int tasks,
			boolean displayPartialResults) {
		this.scene = scene;
		this.display = display;
		this.colorModel = colorModel;
		this.random = random;
		this.tasks = tasks;
		this.width = width;
		this.height = height;
		this.eyePathsPerPixel = eyePathsPerPixel;
		this.lightPathsPerEyePath = lightPathsPerEyePath;
		this.minPassesPerTask = eyePathsPerPixel / tasks;
		this.extraPasses = eyePathsPerPixel - minPassesPerTask * tasks;
		this.displayPartialResults = displayPartialResults;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#getNextTask()
	 */
	public synchronized Object getNextTask() throws Exception {
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
		return tasksSubmitted == tasks;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jdcp.job.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, ca.eandb.util.progress.ProgressMonitor)
	 */
	public synchronized void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) throws Exception {

		int taskPasses = (Integer) task;
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
					raster.setPixel(x, y, raster.getPixel(x, y).divide(eyePathsPerPixel));
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

			ensureInitialized();

			int		passes				= (Integer) task;
			Box2	bounds;
			double	x0, y0, x1, y1;
			double	w					= width;
			double	h					= height;
			int		numPixels			= width * height;
			int		samplesPerPixel		= passes * lightPathsPerEyePath;
			Raster	raster				= this.raster.get();

			raster.clear();

			for (int n = 0, y = 0; y < height; y++) {

				if (!monitor.notifyProgress(n, numPixels))
					return null;

				y0			= (double) y / h;
				y1			= (double) (y + 1) / h;

				for (int x = 0; x < width; x++, n++) {

					x0			= (double) x / w;
					x1			= (double) (x + 1) / w;

					bounds		= new Box2(x0, y0, x1, y1);

					for (int i = 0; i < passes; i++) {

						Point2 p			= RandomUtil.uniform(bounds, random);
						Color sample		= colorModel.sample(random);
						Color white			= colorModel.getWhite(sample.getWavelengthPacket());
						EyeNode eye			= nodes.sampleEye(p, white);
						PathNode eyeTail	= tracePath(eye, random);

						for (int j = 0; j < lightPathsPerEyePath; j++) {

							LightNode light		= nodes.sampleLight(sample, random);
							PathNode lightTail	= tracePath(light, random);

							Color score			= join(eyeTail, lightTail, raster);
							if (score != null) {
								raster.addPixel(x, y, score.divide(samplesPerPixel));
							}

						}

					}

				}

			}

			monitor.notifyProgress(numPixels, numPixels);
			monitor.notifyComplete();

			return raster;

		}

		private void joinLightPathToEye(PathNode tail, Raster raster) {
			double weight = 1.0 / (double) (width * height);
			PathNode node = tail;
			while (node.getDepth() > 0) {
				ScatteringNode scat = (ScatteringNode) node;
				scat.scatterToEye(raster, weight); // FIXME
				node = node.getParent();
			}
			LightNode light = (LightNode) node;
			light.scatterToEye(raster, weight); // FIXME
		}

		private Color joinInnerToInner(ScatteringNode eyeNode, ScatteringNode lightNode) {
			return PathUtil.join(eyeNode, lightNode); // FIXME multiply by weight
		}

		private Color joinInnerToLight(ScatteringNode eyeNode, LightNode light) {
			return PathUtil.join(eyeNode, light); // FIXME multiply by weight
		}

		private Color join(PathNode eyeTail, PathNode lightTail, Raster raster) {
			int n = 0;
			Color score = null;
			int eyePathLength = eyeTail.getDepth();
			int lightPathLength = lightTail.getDepth();
			int eyeDepth = eyePathLength;
			PathNode eyeNode = eyeTail;
			while (eyeDepth > 0) {
				int lightDepth = lightPathLength;
				PathNode lightNode = lightTail;
				while (lightDepth > 0) {
					int depth = eyeDepth + lightDepth + 1;
					int count = Math.max(2 + eyePathLength + lightPathLength - depth, 0);
					score = ColorUtil.add(score, ColorUtil.div(joinInnerToInner(
							(ScatteringNode) eyeNode,
							(ScatteringNode) lightNode), count));
					n++;
					lightNode = lightNode.getParent();
					lightDepth--;
				}

				int depth = eyeDepth + 1;
				int count = Math.max(2 + eyePathLength + lightPathLength - depth, 0);
				score = ColorUtil.add(score, ColorUtil.div(joinInnerToLight(
						(ScatteringNode) eyeNode, (LightNode) lightNode), count));
				n++;
				eyeNode = eyeNode.getParent();
				eyeDepth--;
			}
			joinLightPathToEye(lightTail, raster);
			return score;
		}

		private PathNode tracePath(PathNode node, Random random) {
			PathNode next = node.expand(random);
			while (next != null) {
				node = next;
				next = node.expand(random);
			}
			return node;
		}

	}

}
